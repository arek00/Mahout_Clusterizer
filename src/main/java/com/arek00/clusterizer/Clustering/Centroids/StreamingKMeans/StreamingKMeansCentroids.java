package com.arek00.clusterizer.Clustering.Centroids.StreamingKMeans;


import com.arek00.clusterizer.Clustering.Centroids.CentroidsGenerator;
import com.arek00.clusterizer.Utils.PathValidator;
import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.clustering.streaming.mapreduce.StreamingKMeansDriver;
import org.apache.mahout.common.distance.DistanceMeasure;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class StreamingKMeansCentroids implements CentroidsGenerator {
    private static final Logger logger = LogManager.getLogger(StreamingKMeansCentroids.class);

    private Configuration configuration;
    private StreamingKMeansParameters parameters;

    public StreamingKMeansCentroids(@NonNull Configuration configuration) {
        this.configuration = configuration;
        this.parameters = new StreamingKMeansParameters.Builder().build();
    }

    /**
     * Set parameters for StreamingKMeans algorthm.
     * If won't, default parameters will be used.
     *
     * @param parameters
     */
    public void setParameters(@NonNull StreamingKMeansParameters parameters) {
        this.parameters = parameters;
    }

    public Path generateCentroids(@NonNull Path vectors, @NonNull Path output)
            throws InterruptedException, ClassNotFoundException, ExecutionException, IOException {

        configureOptionsForWorkers(configuration, parameters);
        PathValidator.removePathIfExists(output);

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
