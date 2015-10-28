package com.arek00.clusterizer.demos.display.DistanceDisplay.Points;


import com.arek00.clusterizer.Utils.VectorDistance;
import lombok.Getter;
import lombok.NonNull;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;
import org.apache.mahout.math.Vector;

public class ClusterCenter implements DisplayedPointEntity {

    private static final DistanceMeasure measureMethod = new SquaredEuclideanDistanceMeasure();

    @Getter private int clusterId;
    @Getter private double distanceFromZero;
    @Getter private Cluster cluster;

    public ClusterCenter(@NonNull ClusterWritable clusterWritable) {
        this.cluster = clusterWritable.getValue();

        this.clusterId = cluster.getId();
        this.distanceFromZero = VectorDistance.distanceFromZero(cluster.getCenter(), measureMethod);
    }

    @Override
    public Vector getCenter() {
        return cluster.getCenter();
    }
}
