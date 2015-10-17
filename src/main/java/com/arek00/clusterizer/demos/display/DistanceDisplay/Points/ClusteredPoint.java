package com.arek00.clusterizer.demos.display.DistanceDisplay.Points;


import lombok.Getter;
import lombok.NonNull;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.clustering.classify.WeightedPropertyVectorWritable;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.ConstantVector;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.Vector;

public class ClusteredPoint {

    private WeightedPropertyVectorWritable pointWritable;

    @Getter private int clusterId;
    @Getter private double weight;
    @Getter private double distanceFromZero;
    @Getter private String pointLabel = "";

    /**
     * Second element of Pair has to be WeightedPropertyVectorWritable.
     *
     * @param clusteredPointWritable
     */
    public ClusteredPoint(@NonNull Pair<Writable, Writable> clusteredPointWritable) {
        if(clusteredPointWritable.getSecond() instanceof WeightedPropertyVectorWritable) {
            initialize((IntWritable)clusteredPointWritable.getFirst(),
                    (WeightedPropertyVectorWritable)clusteredPointWritable.getSecond()
            );
        }
        else {
            throw new IllegalArgumentException("Given pair is not correct. Make sure that second element of pair is instance of" +
                    " WeightedProvertyVectorWritable class. In other case it is not correct clustering point instance.");
        }
    }


    private void initialize(IntWritable clusterIDWritable, WeightedPropertyVectorWritable vectorWritable) {
        this.clusterId = doGetClusterId(clusterIDWritable);
        this.distanceFromZero = doGetDistance(vectorWritable.getVector());
        this.weight = vectorWritable.getWeight();
        this.pointLabel = doGetPointLabel(vectorWritable.getVector());
    }

    private int doGetClusterId(IntWritable clusterId) {
        return clusterId.get();
    }

    private double doGetDistance(Vector pointVector) {
        Vector zeroVector = new ConstantVector(0d, pointVector.size());

        DistanceMeasure measure = new EuclideanDistanceMeasure();

        return measure.distance(pointVector, zeroVector);
    }

    private String doGetPointLabel(Vector vector) {
        if(vector instanceof NamedVector) {
            return ((NamedVector) vector).getName();
        }
        else {
            return " ";
        }
    }




}
