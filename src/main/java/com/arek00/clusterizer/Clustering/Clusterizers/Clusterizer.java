package com.arek00.clusterizer.Clustering.Clusterizers;

import org.apache.hadoop.fs.Path;

public interface Clusterizer {

    public Path runClustering(Path vectors, Path startingCentroids, Path output) throws Exception;
}
