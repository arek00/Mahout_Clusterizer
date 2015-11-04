package com.arek00.clusterizer.Clustering.Centroids.KMeansPlusPlus;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


public class MahoutVectorClusterableTest {

    private static Vector vector = new RandomAccessSparseVector(4);

    @BeforeClass
    public static void initVector() {
        vector.set(0, 17d);
        vector.set(2, 0.1d);
        vector.set(3, 0.4d);
    }


    @Test
    public void testGetPoint() throws Exception {
        MahoutVectorClusterable vectorClusterable = new MahoutVectorClusterable(vector);

        double[] vectorClusterableArray = vectorClusterable.getPoint();

        assertTrue(vectorClusterableArray[0] == 17d);
        assertTrue(vectorClusterableArray[1] == 0d);
        assertTrue(vectorClusterableArray[2] == 0.1d);
        assertTrue(vectorClusterableArray[3] == 0.4d);
    }
}