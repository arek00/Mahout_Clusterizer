package com.arek00.clusterizer.Clustering.Vectorizers;

import com.arek00.clusterizer.validators.NumberValidator;
import lombok.Getter;
import org.apache.mahout.vectorizer.common.PartialVectorMerger;

/**
 * Parameters for vectorizing purposes.
 */
public class TFParameters {

    @Getter private int minimumWordFrequency = 1;
    @Getter private int maxNGramSize = 1;
    @Getter private float minimumLLRValue = 0.0f;
    @Getter private float normalizingPower = PartialVectorMerger.NO_NORMALIZING;


    private TFParameters(int minimumWordFrequency, int maxNGramSize, float minimumLLRValue, float normalizingPower) {
        this.minimumWordFrequency = minimumWordFrequency;
        this.maxNGramSize = maxNGramSize;
        this.minimumLLRValue = minimumLLRValue;
        this.normalizingPower = normalizingPower;
    }

    public static class Builder {
        int minimumWordFrequency = 1;
        int maxNGramSize = 1;
        float minimumLLRValue = 0.1f;
        float normalizingPower = PartialVectorMerger.NO_NORMALIZING;

        /**
         * Set minimum word's appearance frequency in documents to keep it in dictionary
         *
         * @param minimumWordFrequency - Value greater than 0
         * @return
         */
        public Builder minimumWordFrequency(int minimumWordFrequency) {
            NumberValidator.greaterThan("Minimum word frequency must be greater than zero.", 0, minimumWordFrequency);

            this.minimumWordFrequency = minimumWordFrequency;
            return this;
        }

        /**
         * Set maximum length of words sequence keep in dictionary.
         * When set 1, dictionary will have only individual words, otherwise
         * there will be keeping sentences up to three words in row.
         *
         * @param maxNGramSize
         * @return
         */
        public Builder maxNGramSize(int maxNGramSize) {
            NumberValidator.inRange("N gram has to be in range [1;3]", 1, 3, maxNGramSize);


            this.maxNGramSize = maxNGramSize;
            return this;
        }

        /**
         * Minimum threshold to prune ngrams
         *
         * @param minimumLLRValue
         * @return
         */
        public Builder minimumLLRValue(float minimumLLRValue) {
            NumberValidator.greaterThan("Minimum LLR Value has to be greater than 0", 0, minimumLLRValue);

            this.minimumLLRValue = minimumLLRValue;
            return this;
        }

        /**
         * Power coefficient of normalizing.
         * Default value is PartialVectorMerger.NO_NORMALIZING = -1.
         * Left default if it is not necessarily to change.
         *
         * @param normalizingPower
         * @return
         */
        public Builder normalizingPower(float normalizingPower) {

            this.normalizingPower = normalizingPower;
            return this;
        }

        public TFParameters build() {
            return new TFParameters(minimumWordFrequency, maxNGramSize, minimumLLRValue, normalizingPower);
        }
    }

}
