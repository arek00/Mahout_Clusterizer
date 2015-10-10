package com.arek00.clusterizer.Clustering.Vectorizers;

import com.arek00.clusterizer.validators.NumberValidator;
import lombok.Getter;
import org.apache.mahout.vectorizer.common.PartialVectorMerger;

/**
 * Parameters for TFIDF vectorizing.
 */
public class TFIDFParameters {

    @Getter private int minDF;
    @Getter private long maxDF;
    @Getter private float normalizingPower;

    private TFIDFParameters(int minDF, long maxDF, float normalizingPower) {
        this.minDF = minDF;
        this.maxDF = maxDF;
        this.normalizingPower = normalizingPower;
    }

    public static class Builder {
        private int minDF = 1;
        private long maxDF = 100;
        private float normalizingPower = PartialVectorMerger.NO_NORMALIZING;

        /**
         * Minimum percent of frequency in documents.
         * Value in range [1;100] and lesser than maxDF.
         * Default is 1.
         *
         * @param minDF
         * @return
         */
        public Builder minDF(int minDF) {
            NumberValidator.inRange("Minimum Document Frequency coefficient has to be in range [1;100]", 1, 100, minDF);
            NumberValidator.lesserThan("Minimum frequency has to be lesser than maximum frequency which equals " + maxDF,
                    maxDF, minDF);

            this.minDF = minDF;
            return this;
        }

        /**
         * Maximum percent of term frequency in documents.
         * Value in range [1;100] and greater than minDF
         * Default value equals 100.
         *
         * @param maxDF
         * @return
         */
        public Builder maxDF(long maxDF) {
            NumberValidator.inRange("Maximum Document Frequency coefficient has to be in range [1;100]", 1, 100, minDF);
            NumberValidator.greaterThan("Maximum frequency has to be lesser than maximum frequency which equals " + minDF,
                    minDF, maxDF);

            this.maxDF = maxDF;
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

        public TFIDFParameters build() {
            return new TFIDFParameters(minDF, maxDF, normalizingPower);
        }
    }

}
