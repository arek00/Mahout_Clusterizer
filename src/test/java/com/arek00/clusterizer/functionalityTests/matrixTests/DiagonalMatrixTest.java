package com.arek00.clusterizer.functionalityTests.matrixTests;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.math.DiagonalMatrix;
import org.apache.mahout.math.Matrix;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class DiagonalMatrixTest {
    private static final Logger logger = LogManager.getLogger(DiagonalMatrixTest.class);
    private static final int matrixSize = 5;
    private static Matrix matrix = new DiagonalMatrix(1d, matrixSize);


    @Test
    public void matrixShouldBeIdentityMatrix() {
        assertTrue(matrix.get(0,1) != 1d);
        assertTrue(matrix.get(2,2) == 1d);
        logger.info(matrix.asFormatString());
    }

}
