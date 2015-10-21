package com.arek00.clusterizer.demos.display.DistanceDisplay.Points;


import com.arek00.clusterizer.Utils.VectorDistance;
import lombok.Getter;
import lombok.NonNull;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;

public class ClusterCenter implements DisplayedPointEntity {

    private static final DistanceMeasure measureMethod = new SquaredEuclideanDistanceMeasure();

    @Getter private int clusterId;
    @Getter private double distanceFromZero;

    public ClusterCenter(@NonNull ClusterWritable clusterWritable) {
        Cluster cluster = clusterWritable.getValue();

        this.clusterId = cluster.getId();
        this.distanceFromZero = VectorDistance.distanceFromZero(cluster.getCenter(), measureMethod);
    }

}
