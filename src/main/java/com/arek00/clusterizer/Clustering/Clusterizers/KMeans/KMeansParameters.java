package com.arek00.clusterizer.Clustering.Clusterizers.KMeans;

import com.arek00.clusterizer.validators.NumberValidator;
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

        private float convergenceDelta = 0.0f;
        private int maxIterations = 1;

        /**
         * Number of maximum iteration needed to estimate clusters.
         * Number of actual done iterations depends on convergenceDelta.
         *
         * @param iterations
         * @return
         */
        public Builder maxIteration(int iterations) {
            NumberValidator.greaterThan("Number of iterations has to be greater than 0", 0, iterations);

            this.maxIterations = iterations;
            return this;
        }

        /**
         * Convergence delta used to determine if algorithm has converged.
         * Value in range [0;1]
         *
         * @param convergenceDelta
         * @return
         */
        public Builder convergenceDelta(float convergenceDelta) {
            NumberValidator.inRange("Convergence delta has to be in range [0;1]", 0.0d, 1.0d, convergenceDelta);

            this.convergenceDelta = convergenceDelta;
            return this;
        }

        public KMeansParameters build() {
            return new KMeansParameters(convergenceDelta, maxIterations);
        }
    }

}
