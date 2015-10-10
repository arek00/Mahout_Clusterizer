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


    public void runClustering(Path vectors, Path centroids, Path output, FuzzyKMeansParameters parameters) throws InterruptedException, IOException, ClassNotFoundException {
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
