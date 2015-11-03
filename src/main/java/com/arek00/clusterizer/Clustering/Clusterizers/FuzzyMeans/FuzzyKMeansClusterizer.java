package com.arek00.clusterizer.Clustering.Clusterizers.FuzzyMeans;

import com.arek00.clusterizer.Clustering.Clusterizers.Clusterizer;
import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.fuzzykmeans.FuzzyKMeansDriver;
import org.apache.mahout.clustering.spectral.kmeans.SpectralKMeansDriver;

import java.io.IOException;

/**
 * Run FuzzyKMeans Clustering.
 */
public class FuzzyKMeansClusterizer implements Clusterizer{

    private Configuration configuration;
    private FuzzyKMeansParameters parameters;

    public FuzzyKMeansClusterizer(@NonNull Configuration configuration) {
        this.configuration = configuration;
        this.parameters = new FuzzyKMeansParameters.Builder().build();
    }

    public void setParameters(@NonNull FuzzyKMeansParameters parameters) {
        this.parameters = parameters;
    }

    /**
     * Run fuzzyKMeans algorithm job.
     *
     * @param vectors - vectors to clusterize
     * @param centroids - initial clusters' centroids points
     * @param output - directory to store results, should be clear before running clustering
     * @param parameters - instance of FuzzyKMeansParameters
     * @throws InterruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Path runClustering(@NonNull Path vectors, @NonNull Path centroids, @NonNull Path output) throws InterruptedException, IOException, ClassNotFoundException {
        FuzzyKMeansDriver.run(
                this.configuration,
                vectors,
                centroids,
                output,
                parameters.getConvergenceDelta(),
                parameters.getMaxIterations(),
                parameters.getM(),
                true,
                parameters.isEmittedMostLikely(),
                0, false
        );

        return output;
    }
}
