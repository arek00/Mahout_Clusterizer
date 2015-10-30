package com.arek00.clusterizer.Algorithms.MatrixUtils;

import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import static org.junit.Assert.*;


public class Math3MatrixUtilsTest {

    @Test
    public void testGetClearMatrix() throws Exception {
        RealMatrix matrix = Math3MatrixUtils.getClearMatrix(2,2,1d);

        assertTrue(matrix.getEntry(0,0) == 1d);
        assertTrue(matrix.getEntry(1,0) == 1d);

    }
}