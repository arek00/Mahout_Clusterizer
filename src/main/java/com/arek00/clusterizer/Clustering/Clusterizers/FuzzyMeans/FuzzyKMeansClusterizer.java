package com.arek00.clusterizer.Clustering.Clusterizers.FuzzyMeans;

import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.fuzzykmeans.FuzzyKMeansDriver;
import org.apache.mahout.clustering.spectral.kmeans.SpectralKMeansDriver;

/**
 * Run FuzzyKMeans Clustering.
 */
public class FuzzyKMeansClusterizer {

    private Configuration configuration;

    public FuzzyKMeansClusterizer(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }


    public void runClustering(Path vectors, Path centroids, Path output) {
        FuzzyKMeansDriver.run(
                this.configuration,
                vectors,
                centroids,
                output,

                )
    }

}
