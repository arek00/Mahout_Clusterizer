package com.arek00.clusterizer.functionalityTests;

import junit.framework.Assert;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.clustering.iterator.ClusteringPolicy;
import org.apache.mahout.clustering.iterator.KMeansClusteringPolicy;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;


public class KMeansClusteringPolicyTest {
    private static Logger logger = LogManager.getLogger(KMeansClusteringPolicyTest.class);

    private static int vectorCardinality = 3;
    private static Vector vector = new SequentialAccessSparseVector(vectorCardinality);

    @Test
    public void selectionTest() {
        vector.set(1, 0.5);
        logger.info(vector.toString());
        ClusteringPolicy policy = new KMeansClusteringPolicy();
        vector = policy.select(vector);
        logger.info(vector.toString());

        assertTrue(vector.get(1) == 1.0d);

        vector.set(0, 3d);
        vector.set(1, -1d);
        vector = policy.select(vector);

        assertTrue(vector.get(0) == 1d);
        assertTrue(vector.get(1) == 0d);
        assertTrue(vector.get(2) == 0d);
    }

}
