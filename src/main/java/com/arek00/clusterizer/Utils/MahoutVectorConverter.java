package com.arek00.clusterizer.Utils;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

public class MahoutVectorConverter {

    public static Vector getVector(double[] point) {
        int dimensions = point.length;

        Vector vector = new RandomAccessSparseVector(dimensions);

        for (int index = 0; index < dimensions; index++) {
            if(point[index] != 0d) {
                vector.set(index, point[index]);
            }
        }

        return vector;
    }

    public static double[] vectorToArray(Vector vector) {
        int dimensions = vector.size();

        double[] array = new double[dimensions];

        vector.nonZeroes()
                .forEach(dimension -> {
                    array[dimension.index()] = dimension.get();
                });

        return array;
    }

}
