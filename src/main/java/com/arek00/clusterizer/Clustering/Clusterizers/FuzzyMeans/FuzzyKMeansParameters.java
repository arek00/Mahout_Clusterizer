package com.arek00.clusterizer.Clustering.Clusterizers.FuzzyMeans;

import com.arek00.clusterizer.validators.NumberValidator;

/**
 * Parameters mandatory to run FuzzyKMeans Clustering algorithm.
 * Use Builder to create an instance of this class.
 */
public class FuzzyKMeansParameters {

    private double convergenceDelta = 0.0f;
    private float m = 1.0f;
    private int maxIterations = 1;
    private boolean emittedMostLikely = true;


    public static class Builder {
        private double convergenceDelta = 0.0f;
        private double m = 1.0f;
        private int maxIterations = 1;
        private boolean emittedMostLikely = true;


        /**
         * Convergence delta used to determine if algorithm has converged.
         * Value in range [0;1]
         *
         * @param convergenceDelta
         * @return
         */
        public Builder convergenceDelta(double convergenceDelta) {
            NumberValidator.inRange("Convergence Delta has to be positive number.", 0.0d, 1.0d, convergenceDelta);

            this.convergenceDelta = convergenceDelta;
            return this;
        }


        /**
         * For m equal 2 weights are normalizing the coefficient linearly to 1, where weight
         * doesn't have much matter in creating clusters center points.
         * When m parameter comes to 1, center of clusters points comes closer to most weighted points.
         * Value in range (1;2]
         *
         * @param m
         * @return
         */
        public Builder fuzzyness(double m) {
            NumberValidator.greaterThan("Fuzzyness m parameter has to be greater than 1", 1.0d, m);

            this.m = m;
            return this;
        }

        /**
         * Number of maximum iterations to do in order to creating clusters centers.
         * Number of actual iteration done depends on convergence delta coefficient
         * Value in range (0;+inf) -> where "inf" means infinity
         *
         * @param iterations
         * @return
         */
        public Builder maxIterations(int iterations) {
            NumberValidator.greaterThan("Algorithm has to do at least one iteration.", 0, iterations);

            this.maxIterations = iterations;
            return this;
        }

        /**
         * True indicates that every clustering step should only
         * emit most likely cluster for each clustered point
         *
         * @param emmitMostLikely
         * @return
         */
        public Builder emmitMostLikely(boolean emmitMostLikely) {
            this.emittedMostLikely = emmitMostLikely;
            return this;
        }


    }

}
