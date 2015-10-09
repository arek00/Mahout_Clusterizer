package com.arek00.clusterizer.Clustering.Clusterizers.KMeans;

import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import java.io.IOException;

/**
 * Cluster directory of articles with KMeans algorithm
 */
public class KMeansClusterizer {

    /**
     * Run kmeans clustering algorithm process.
     * Parameters object should contains all path of vectors and centroids,
     * also with max iteration parameter and convergence delta.
     *
     * @param output
     * @param parameters
     * @throws InterruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void runClustering(Path output, KMeansParameters parameters) throws InterruptedException, IOException, ClassNotFoundException {

        KMeansDriver.run(
                parameters.getVectors(),
                parameters.getCentroids(),
                output,
                parameters.getConvergenceDelta(),
                parameters.getMaxIterations(),
                true, 0, false
        );
    }
}
