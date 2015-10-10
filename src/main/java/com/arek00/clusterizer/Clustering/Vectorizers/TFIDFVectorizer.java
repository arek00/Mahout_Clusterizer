package com.arek00.clusterizer.Clustering.Vectorizers;

import com.arek00.clusterizer.validators.NumberValidator;
import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.common.Pair;
import org.apache.mahout.vectorizer.DictionaryVectorizer;
import org.apache.mahout.vectorizer.tfidf.TFIDFConverter;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class TFIDFVectorizer {

    private Configuration configuration;

    public TFIDFVectorizer(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Create Term Frequency - Inversed Document Frequency vectors from
     * Term Frequency vectors.
     *
     * @param tfVectors - Term Frequency vectors directory
     * @param output - Directory to save created TFIDF vectors
     * @param parameters
     * @param chunkSizeInMB - Size of chunk of data used to processing. Minimum 1MB, preferred over 100MB
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Path vectorize(Path tfVectors, Path output, TFIDFParameters parameters, int chunkSizeInMB) throws InterruptedException, IOException, ClassNotFoundException {
        NumberValidator.greaterThan("Chunk Size has to be greater than 0", 0, chunkSizeInMB);

        Path tfidfPath = new Path(output, "tfidf");

        Pair<Long[], List<Path>> documentFrequencies = TFIDFConverter.calculateDF(
                tfVectors,
                tfidfPath,
                this.configuration,
                chunkSizeInMB
        );

        TFIDFConverter.processTfIdf(
                tfVectors,
                tfidfPath,
                this.configuration,
                documentFrequencies,
                1, 100,
                parameters.getNormalizingPower(),
                false, false, false, 1
        );

        return new Path(tfidfPath, "tfidf-vectors");
    }

}
