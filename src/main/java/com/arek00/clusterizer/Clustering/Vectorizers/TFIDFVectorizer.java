package com.arek00.clusterizer.Clustering.Vectorizers;

import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.common.Pair;
import org.apache.mahout.vectorizer.DictionaryVectorizer;
import org.apache.mahout.vectorizer.common.PartialVectorMerger;
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

    public void vectorize(Path tokenizedDocumentsDirectory, Path output, TFIDFParameters parameters) throws InterruptedException, IOException, ClassNotFoundException {
        Path tfVectorPath = new Path(output, DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER);
        Path tfidfPath = new Path(output, "tfidf");


        DictionaryVectorizer.createTermFrequencyVectors(
                tokenizedDocumentsDirectory,
                output,
                DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER,
                this.configuration,
                parameters.getMinimumWordFrequency(),
                parameters.getMaxNGramSize(),
                parameters.getMinimumLLRValue(),
                parameters.getNormalizingPower(),
                true, 1,
                parameters.getChunkSizeInMb(),
                false, false
        );

        Pair<Long[], List<Path>> documentFrequencies = TFIDFConverter.calculateDF(
                new Path(output, tfVectorPath),
                tfidfPath,
                this.configuration,
                parameters.getChunkSizeInMb()
        );

        TFIDFConverter.processTfIdf(
                tfVectorPath,
                tfidfPath,
                this.configuration,
                documentFrequencies,
                1, 100,
                parameters.getNormalizingPower(),
                false, false, false, 1
        );

    }

}
