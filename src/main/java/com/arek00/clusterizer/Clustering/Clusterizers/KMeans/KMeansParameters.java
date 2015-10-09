package com.arek00.clusterizer.Clustering.Clusterizers.KMeans;

import lombok.Getter;
import org.apache.hadoop.fs.Path;

/**
 * Value object for parameters to use KMeansClusterizer
 */
public class KMeansParameters {

    @Getter private Path vectors;
    @Getter private Path centroids;
    @Getter private float convergenceDelta;
    @Getter private int maxIterations;


    private KMeansParameters(String vectors, String centroids, float convergenceDelta, int iterations) {
        this.vectors = new Path(vectors);
        this.centroids = new Path(centroids);
        this.convergenceDelta = convergenceDelta;
        this.maxIterations = iterations;
    }

    public static class Builder {

        private String vectors;
        private String centroids;
        private float convergenceDelta;
        private int maxIterations;

        public Builder vectorsDirectory(String vectorsDirectory) {
            this.vectors = vectorsDirectory;
            return this;
        }

        public Builder centroidsDirectory(String centroidsDirectory) {
            this.centroids = centroidsDirectory;
            return this;
        }

        public Builder maxIteration(int iterations) {
            assert iterations > 0;

            this.maxIterations = iterations;
            return this;
        }

        public Builder convergenceDelta(float convergenceDelta) {
            assert convergenceDelta > 0;

            this.convergenceDelta = convergenceDelta;
            return this;
        }

        public KMeansParameters build() {
            return new KMeansParameters(vectors, centroids, convergenceDelta, maxIterations);
        }
    }

}
