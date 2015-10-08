package com.arek00.clusterizer.Clustering.Vectorizers;

import lombok.Getter;
import org.apache.mahout.vectorizer.common.PartialVectorMerger;

/**
 * Parameters for vectorizing purposes.
 */
public class TFIDFParameters {

    @Getter private int minimumWordFrequency = 1;
    @Getter private int maxNGramSize = 1;
    @Getter private float minimumLLRValue = 0.0f;
    @Getter private float normalizingPower = PartialVectorMerger.NO_NORMALIZING;
    @Getter private int chunkSizeInMb = 100;


    private TFIDFParameters(int minimumWordFrequency, int maxNGramSize, float minimumLLRValue, float normalizingPower, int chunkSizeInMb) {
        this.minimumWordFrequency = minimumWordFrequency;
        this.maxNGramSize = maxNGramSize;
        this.minimumLLRValue = minimumLLRValue;
        this.normalizingPower = normalizingPower;
        this.chunkSizeInMb = chunkSizeInMb;
    }

    public static class Builder {
        int minimumWordFrequency = 1;
        int maxNGramSize = 1;
        float minimumLLRValue = 0.0f;
        float normalizingPower = PartialVectorMerger.NO_NORMALIZING;
        int chunkSizeInMb = 100;

        public Builder minimumWordFrequency(int minimumWordFrequency) {
            assert minimumWordFrequency > 1;

            this.minimumWordFrequency = minimumWordFrequency;
            return this;
        }

        public Builder maxNGramSize(int maxNGramSize) {
            assert maxNGramSize > 0 && maxNGramSize < 4;

            this.maxNGramSize = maxNGramSize;
            return this;
        }

        public Builder minimumLLRValue(float minimumLLRValue) {
            assert minimumLLRValue > 0f;

            this.minimumLLRValue = minimumLLRValue;
            return this;
        }

        public Builder normalizingPower(float normalizingPower) {
            this.normalizingPower = normalizingPower;
            return this;
        }

        public Builder chunkSizeInMb(int chunkSize) {
            assert chunkSize > 100;
            this.chunkSizeInMb = chunkSize;

            return this;
        }

        public TFIDFParameters build() {
            return new TFIDFParameters(minimumWordFrequency, maxNGramSize, minimumLLRValue, normalizingPower, chunkSizeInMb);
        }
    }

}
