package com.arek00.clusterizer.demos.display.DistanceDisplay.Points;


import lombok.Getter;
import lombok.NonNull;
import org.apache.mahout.clustering.iterator.ClusterWritable;

public class ClusterCenter {

    @Getter private int clusterId;
    @Getter private double distanceFromZero;

    public ClusterCenter(@NonNull ClusterWritable clusterWritable) {


    }

}
