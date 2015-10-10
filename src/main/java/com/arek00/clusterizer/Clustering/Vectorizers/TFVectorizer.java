package com.arek00.clusterizer.Clustering.Vectorizers;

import com.arek00.clusterizer.validators.NumberValidator;
import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.vectorizer.DictionaryVectorizer;

import java.io.IOException;

/**
 * Create Term Frequency vectors from documents
 */
public class TFVectorizer {

    private Configuration configuration;

    public TFVectorizer(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Create Term Frequency vectors from tokenized documents.
     *
     * @param tokenizedDocumentsDirectory
     * @param output
     * @param parameters
     * @param chunkSizeInMB               - Size of chunk of data used in processing. Minimum 1MB, preferred over 100MB.
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Path createVectors(Path tokenizedDocumentsDirectory, Path output, TFParameters parameters, int chunkSizeInMB)
            throws InterruptedException, IOException, ClassNotFoundException {
        NumberValidator.greaterThan("Chunk size has to be greater number than 0", 0, chunkSizeInMB);


        DictionaryVectorizer.createTermFrequencyVectors(
                tokenizedDocumentsDirectory,
                output,
                DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER,
                this.configuration,
                parameters.getMinimumWordFrequency(),
                parameters.getMaxNGramSize(),
                parameters.getMinimumLLRValue(),
                parameters.getNormalizingPower(),
                true, 1, chunkSizeInMB,
                false, false
        );

        return new Path(output, DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER);
    }
}
