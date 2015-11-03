package com.arek00.clusterizer.Clustering.Executors;

import com.arek00.clusterizer.Clustering.Tokenizers.StandardTokenizer;
import com.arek00.clusterizer.Clustering.Tokenizers.Tokenizer;
import com.arek00.clusterizer.Clustering.Vectorizers.TFIDFParameters;
import com.arek00.clusterizer.Clustering.Vectorizers.TFIDFVectorizer;
import com.arek00.clusterizer.Clustering.Vectorizers.TFParameters;
import com.arek00.clusterizer.Clustering.Vectorizers.TFVectorizer;
import com.arek00.clusterizer.Utils.PathValidator;
import com.arek00.clusterizer.validators.NumberValidator;
import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import java.io.IOException;


public class VectoringTask implements Task {

    private static final String TOKENIZE_OUTPUT = "tokenizer_output";
    private static final String TF_OUTPUT = "tf_vectors";
    private static final String TFIDF_OUTPUT = "tfidf_vectors";

    private Path output;
    private Path sequenceFile;
    private Configuration configuration;
    private int chunkSize = 100;

    private TFParameters tfParameters;
    private TFIDFParameters tfidfParameters;


    public VectoringTask(@NonNull Path sequenceFile, @NonNull Path output, @NonNull Configuration configuration) {
        this.output = output;
        this.configuration = configuration;
        this.sequenceFile = sequenceFile;

        this.tfParameters = new TFParameters.Builder().build();
        this.tfidfParameters = new TFIDFParameters.Builder().build();
    }


    private void setTfParameters(@NonNull TFParameters parameters) {
        this.tfParameters = parameters;
    }

    private void setTfIdfParameters(@NonNull TFIDFParameters parameters) {
        this.tfidfParameters = parameters;
    }

    /**
     * Set chunk size in MB.
     * Minimum value equals 10MB.
     *
     * @param chunkSizeInMb
     */
    private void setChunkSize(int chunkSizeInMb) {
        NumberValidator.greaterOrEqual("Set chunk size equal or greater than 10 MB", 10, chunkSizeInMb);

        this.chunkSize = chunkSizeInMb;
    }


    /**
     *
     * @return - output path of TFIDF Vectorizer
     */
    @Override
    public Path execute() throws InterruptedException, IOException, ClassNotFoundException {

        Tokenizer tokenizer = new StandardTokenizer(this.configuration);
        Path tokenizeOutput = new Path(output, TOKENIZE_OUTPUT);
        PathValidator.removePathIfExists(tokenizeOutput);
        Path tokenizeDocuments = tokenizer.tokenize(sequenceFile, tokenizeOutput);

        TFVectorizer tfVectorizer = new TFVectorizer(this.configuration);
        Path tfVectorsOutputPath = new Path(output, TF_OUTPUT);
        PathValidator.removePathIfExists(tfVectorsOutputPath);
        Path tfVectors = tfVectorizer.createVectors(tokenizeDocuments, tfVectorsOutputPath, tfParameters, chunkSize);

        Path tfidfVectorsOutput = new Path(output, TFIDF_OUTPUT);
        PathValidator.removePathIfExists(tfidfVectorsOutput);
        TFIDFVectorizer tfidfVectorizer = new TFIDFVectorizer(configuration);
        return tfidfVectorizer.vectorize(tfVectors, tfidfVectorsOutput, tfidfParameters, chunkSize);
    }
}
