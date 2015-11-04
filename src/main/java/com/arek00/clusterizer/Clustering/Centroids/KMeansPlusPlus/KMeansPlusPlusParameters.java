package com.arek00.clusterizer.Clustering.Centroids.KMeansPlusPlus;


import com.arek00.clusterizer.validators.NumberValidator;
import lombok.Getter;

public class KMeansPlusPlusParameters {
    @Getter private int clustersNumber;
    @Getter private int iterationsNumber;

    private KMeansPlusPlusParameters(int k, int maxIterations) {
        this.clustersNumber = k;
        this.iterationsNumber = maxIterations;
    }


    public static class Builder {
        private int k = 1;
        private int maxIteration = 20;

        /**
         *
         * Set number of clusters algorithm must estimate
         * @param k
         * @return
         */
        public Builder setClustersNumber(int k) {
            NumberValidator.greaterOrEqual("Set minimum 1 cluster", 1, k);

            this.k = k;
            return this;
        }

        /**
         * Set maximum number of algorithm's iterations
         * Minimum value: 1
         *
         * @param maxIterations
         * @return
         */
        public Builder setIterationLimit(int maxIterations) {
            NumberValidator.greaterOrEqual("There has to be minimum 1 iteration of algorithm", 1, maxIterations);

            this.maxIteration = maxIterations;
            return this;
        }

        public KMeansPlusPlusParameters build() {
            return new KMeansPlusPlusParameters(this.k, this.maxIteration);
        }
    }
}
