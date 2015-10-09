package com.arek00.clusterizer.Clustering.Tokenizers;

import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * Interface for tokenizers.
 */
public interface Tokenizer {
    public Path tokenize(Path sequenceFile, Path tokenizeDirectory) throws InterruptedException, IOException, ClassNotFoundException;
}
