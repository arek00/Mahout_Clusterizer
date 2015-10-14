package com.arek00.clusterizer.Clustering.Clusterizers.Spectral;

import lombok.Getter;

public class SpectralParameters {

    @Getter private int numDims;
    @Getter private int clusters;
    @Getter private double convergenceDelta;
    @Getter private int maxIterations;
    @Getter private boolean ssvd;

    private SpectralParameters(int numDims, int clusters, double convergenceDelta, int maxIterations, boolean ssvd) {
        this.numDims = numDims;
        this.clusters = clusters;
        this.convergenceDelta = convergenceDelta;
        this.maxIterations = maxIterations;
        this.ssvd = ssvd;
    }


    public static class Builder {
        private int numDims;
        private int clusters;
        private double convergenceDelta;
        private int maxIterations;
        private boolean ssvd;


        public Builder numDims(int numDims) {
            this.numDims = numDims;
            return this;
        }

        public Builder clusters(int clusters) {
            this.clusters = clusters;
            return this;
        }

        public Builder convergenceDelta(double convergenceDelta) {
            this.convergenceDelta = convergenceDelta;
            return this;
        }

        public Builder maxIterations(int maxIterations) {
            this.maxIterations = maxIterations;
            return this;
        }

        public Builder SSVD(boolean ssvd) {
            this.ssvd = ssvd;
            return this;
        }

        public SpectralParameters build() {
            return new SpectralParameters(numDims, clusters, convergenceDelta, maxIterations, ssvd);
        }

    }


}
