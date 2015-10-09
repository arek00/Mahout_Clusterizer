package com.arek00.clusterizer.Clustering.Clusterizers.KMeans;

import lombok.Getter;
import org.apache.hadoop.fs.Path;

/**
 * Value object for parameters to use KMeansClusterizer
 */
public class KMeansParameters {

    @Getter private float convergenceDelta;
    @Getter private int maxIterations;


    private KMeansParameters(float convergenceDelta, int iterations) {
        this.convergenceDelta = convergenceDelta;
        this.maxIterations = iterations;
    }

    public static class Builder {

        private float convergenceDelta;
        private int maxIterations;

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
            return new KMeansParameters(convergenceDelta, maxIterations);
        }
    }

}
