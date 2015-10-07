package com.arek00.clusterizer.clusterizers;

import lombok.Getter;

/**
 * Value object for parameters to use KMeansClusterizer
 */
public class KMeansParameters {

    @Getter private boolean canopyClustering;
    @Getter private int ngramSize;
    @Getter private int kPoints; //to use random seed generator, doesn't need to be set when canopy is using
    @Getter private int iterationsNumber;
    @Getter private double canopyT1;
    @Getter private double canopyT2;


    /**
     * RandomSeedGenerator Constructor
     * @param ngramSize
     * @param kPoints
     */
    private KMeansParameters(int iterations, int ngramSize, int kPoints) {
        this(iterations, ngramSize, false, 0.0d, 0.0d, kPoints);
    }

    /**
     * Canopy Constructor
     * @param ngramSize
     * @param canopyClustering
     * @param t1
     * @param t2
     */
    private KMeansParameters(int iterations, int ngramSize, boolean canopyClustering, double t1, double t2) {
        this(iterations, ngramSize, canopyClustering, t1, t2, 0);
    }


    /**
     * Default Constructor
     * @param ngramSize
     * @param canopyClustering
     * @param t1
     * @param t2
     * @param kPoints
     */
    private KMeansParameters(int iterations, int ngramSize, boolean canopyClustering, double t1, double t2, int kPoints) {
        this.iterationsNumber = iterations;
        this.ngramSize = ngramSize;
        this.canopyClustering = canopyClustering;
        this.canopyT1 = t1;
        this.canopyT2 = t2;
        this.kPoints = kPoints;
    }

    public static class Builder {
        public static final int UNIGRAM = 1;
        public static final int BIGRAM = 2;
        public static final int TRIGRAM = 3;

        private boolean useCanopy = false;
        private int ngramSize = 1;
        private int kPointsNumber = 1;
        private int iterations = 1;
        private double t1 = 2.0d;
        private double t2 = 1.0d;

        public Builder useCanopy(boolean useCanopy) {
            this.useCanopy = useCanopy;
            return this;
        }

        /**
         * Set thresholds for canopy clustering if it needs to be used.
         * T1 > T2
         *
         * @param t1
         * @param t2
         * @return
         */
        public Builder canopyThresholds(double t1, double t2) {
            assert t1 > t2;

            this.t1 = t1;
            this.t2 = t2;
            return this;
        }

        /**
         * Set maximum n-gram's length.
         * Use one of final variable: UNIGRAM, BIGRAM, TRIGRAM.
         *
         * @param ngramSize
         * @return
         */
        public Builder ngramSize(int ngramSize) {
            assert ngramSize > 0 && ngramSize < 4;

            this.ngramSize = ngramSize;
            return this;
        }

        /**
         * Set number of points to generate by random generator seed.
         * Not required to set when canopy is chosen.
         * Number has to be more than zero
         * @param number
         * @return
         */
        public Builder kPointsNumber(int number) {
            assert number > 0;

            this.kPointsNumber = number;
            return this;
        }

        /**
         * Set number of iterations doing by KMeans algorithm
         * to estimate clusters
         * @param iterations
         * @return
         */
        public Builder iterationsNumber(int iterations) {
            assert iterations > 0;

            this.iterations = iterations;
            return this;
        }

        public KMeansParameters build() {
            if(useCanopy) {
                return new KMeansParameters(this.iterations, this.ngramSize, this.useCanopy, this.t1, this.t2);
            }
            else {
                return new KMeansParameters(this.iterations, this.ngramSize, this.kPointsNumber);
            }

        }


    }




}
