package com.arek00.clusterizer.Clustering.Clusterizers.StreamingKMeans;


import lombok.NonNull;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.lda.LDAPrintTopics;
import org.apache.mahout.clustering.streaming.mapreduce.StreamingKMeansDriver;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class StreamingKMeansClusterizer {

    private Configuration configuration;

    public StreamingKMeansClusterizer(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }

    public void runClustering(@NonNull Path vectors, @NonNull Path output, @NonNull StreamingKMeansParameters parameters)
            throws InterruptedException, ClassNotFoundException, ExecutionException, IOException {

        configureOptionsForWorkers(configuration, parameters);
        StreamingKMeansDriver.run(
                this.configuration,
                vectors,
                output
        );
    }

    private void configureOptionsForWorkers(Configuration configuration, StreamingKMeansParameters parameters)
            throws ClassNotFoundException {
        StreamingKMeansDriver.configureOptionsForWorkers(configuration,
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
