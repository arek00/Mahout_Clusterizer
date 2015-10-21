package com.arek00.clusterizer.Utils;


import lombok.NonNull;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.ConstantVector;
import org.apache.mahout.math.Vector;

public class VectorDistance {

    /**
     * Utility method to calculate distance between two vectors with
     * given measurement method.
     *
     * @param v1 - Vector 1
     * @param v2 - Vector2
     * @param measure - measurement method
     * @return
     */
    public static double distance(@NonNull Vector v1, @NonNull Vector v2, @NonNull DistanceMeasure measure) {
        return measure.distance(v1, v2);
    }

    /**
     * Calculate distance from 0 point to given vector
     * measured with given method.
     *
     * @param vector
     * @param measure
     * @return
     */
    public static double distanceFromZero(@NonNull Vector vector, @NonNull DistanceMeasure measure) {
        Vector zeroVector = new ConstantVector(0d, vector.size());
        return measure.distance(vector, zeroVector);
    }

}
