package com.arek00.clusterizer.Clustering.Tokenizers;

import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.mahout.vectorizer.DocumentProcessor;

import java.io.IOException;

/**
 * Standard Document Processor used to tokenize documents.
 */
public class StandardTokenizer implements Tokenizer {

    private Configuration configuration;

    public StandardTokenizer(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Path tokenize(Path sequenceFile, Path tokenizeDirectory) throws InterruptedException, IOException, ClassNotFoundException {
        Path output = new Path(tokenizeDirectory, DocumentProcessor.TOKENIZED_DOCUMENT_OUTPUT_FOLDER);


        DocumentProcessor.tokenizeDocuments(
                sequenceFile,
                StandardAnalyzer.class,
                output,
                this.configuration
        );

        return output;
    }
}
