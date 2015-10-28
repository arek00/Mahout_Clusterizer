package com.arek00.clusterizer.demos.display.DistanceDisplay.Points;


import org.apache.mahout.math.Vector;

public interface DisplayedPointEntity {
    int getClusterId();

    double getDistanceFromZero();

    Vector getCenter();
}
