package com.arek00.clusterizer.demos;

import org.apache.hadoop.conf.Configuration;
import org.apache.mahout.clustering.canopy.CanopyClusterer;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.display.*;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.vectorizer.SparseVectorsFromSequenceFiles;

public class KMeansClusteringDemo {

    public static void main(String[] args) {
        try {


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Configuration configuration;

    public KMeansClusteringDemo() {

    }

    private void initialize() {
        this.configuration = new Configuration();
    }


}
