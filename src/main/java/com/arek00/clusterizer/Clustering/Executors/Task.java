package com.arek00.clusterizer.Clustering.Executors;


import org.apache.hadoop.fs.Path;

public interface Task {

    public Path execute() throws Exception;
}
