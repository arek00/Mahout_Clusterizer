package com.arek00.clusterizer.Clustering.Clusterizers.StreamingKMeans;


import lombok.NonNull;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.clustering.lda.LDAPrintTopics;
import org.apache.mahout.clustering.streaming.cluster.BallKMeans;
import org.apache.mahout.clustering.streaming.mapreduce.StreamingKMeansDriver;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.neighborhood.UpdatableSearcher;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class StreamingKMeansClusterizer {
    private static final Logger logger = LogManager.getLogger(StreamingKMeansClusterizer.class);

    private Configuration configuration;

    public StreamingKMeansClusterizer(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }

    public Path runClustering(@NonNull Path vectors, @NonNull Path output, @NonNull StreamingKMeansParameters parameters)
            throws InterruptedException, ClassNotFoundException, ExecutionException, IOException {

        configureOptionsForWorkers(configuration, parameters);
        StreamingKMeansDriver.run(
                this.configuration,
                vectors,
                output
        );

        DistanceMeasure measure = null;
        try {
            measure = (DistanceMeasure) Class.forName(parameters.getMeasureClass()).newInstance();
        } catch (InstantiationException e) {
            logger.error("Couldn't instantiate DistanceMeasure object: " + measure.getClass().getCanonicalName());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            logger.error("IlegalAccess during instantiating DistanceMeasure object.");
            e.printStackTrace();
        }
        CentroidToClusterConverter converter = new CentroidToClusterConverter(configuration, measure);

        Path convertedClustersPath = new Path(output, "clusters");
        Path generatedClusters = converter.convertDirectory(output, convertedClustersPath);

        return generatedClusters;
    }

    private void configureOptionsForWorkers(Configuration configuration, StreamingKMeansParameters parameters)
            throws ClassNotFoundException {
        StreamingKMeansDriver.configureOptionsForWorkers(
                configuration,
                parameters.getNumClusters(),
                parameters.getEstimatedNumMapClusters(),
                parameters.getEstimatedDistanceCutOff(),
                parameters.getMaxNumIteration(),
                parameters.getTrimFraction(),
                parameters.isRandomInit(),
                parameters.isIgnoreWeights(),
                parameters.getTestProbability(),
                parameters.getNumBallKMeans(),
                parameters.getMeasureClass(),
                parameters.getSearcherClass(),
                parameters.getSearchSize(),
                parameters.getNumProjections(),
                parameters.getMethod(),
                parameters.isReduceStreamingKMeans()
        );
    }


}
