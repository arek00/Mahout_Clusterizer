package com.arek00.clusterizer.Clustering.Clusterizers.KMeans;

import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.kmeans.KMeansDriver;

import java.io.IOException;

/**
 * Cluster directory of articles with KMeans algorithm
 */
public class KMeansClusterizer {

    private Configuration configuration;

    public KMeansClusterizer(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Run kmeans clustering algorithm process.
     * Parameters object should contains all path of vectors and centroids,
     * also with max iteration parameter and convergence delta.
     *
     * @param vectors    - input path to vectorized set of data destined to clustering
     * @param centroids  - set of initial points from which algorithm start its job
     * @param output     - directory to save results, should be empty
     * @param parameters - instance of KMeansParameters class
     * @throws InterruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void runClustering(
            @NonNull Path vectors, @NonNull Path centroids, @NonNull Path output, @NonNull KMeansParameters parameters)
            throws InterruptedException, IOException, ClassNotFoundException {

        KMeansDriver.run(
                configuration,
                vectors,
                centroids,
                output,
                parameters.getConvergenceDelta(),
                parameters.getMaxIterations(),
                true, 0, false
        );
    }
}
