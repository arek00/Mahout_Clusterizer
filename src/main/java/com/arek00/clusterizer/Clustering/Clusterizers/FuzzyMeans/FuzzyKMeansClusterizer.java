package com.arek00.clusterizer.Clustering.Clusterizers.FuzzyMeans;

import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.fuzzykmeans.FuzzyKMeansDriver;
import org.apache.mahout.clustering.spectral.kmeans.SpectralKMeansDriver;

import java.io.IOException;

/**
 * Run FuzzyKMeans Clustering.
 */
public class FuzzyKMeansClusterizer {

    private Configuration configuration;

    public FuzzyKMeansClusterizer(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Run fuzzyKMeans algorithm job.
     *
     * @param vectors - vectors to clusterize
     * @param centroids - initial clusters' centroids points
     * @param output - directory to store results, should be clear before running clustering
     * @param parameters - instance of FuzzyKMeansParameters
     * @throws InterruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void runClustering(@NonNull Path vectors, @NonNull Path centroids, @NonNull Path output, @NonNull FuzzyKMeansParameters parameters) throws InterruptedException, IOException, ClassNotFoundException {
        FuzzyKMeansDriver.run(
                this.configuration,
                vectors,
                centroids,
                output,
                parameters.getConvergenceDelta(),
                parameters.getMaxIterations(),
                parameters.getM(),
                true,
                parameters.isEmittedMostLikely(),
                0, false
        );
    }

}
