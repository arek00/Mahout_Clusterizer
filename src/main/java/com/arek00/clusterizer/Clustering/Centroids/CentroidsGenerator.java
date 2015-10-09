package com.arek00.clusterizer.Clustering.Centroids;

import org.apache.hadoop.fs.Path;

public interface CentroidsGenerator {

    public Path generateCentroids(Path vectors, Path output) throws Exception;
}
