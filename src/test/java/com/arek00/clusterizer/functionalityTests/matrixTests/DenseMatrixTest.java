package com.arek00.clusterizer.functionalityTests.matrixTests;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.math.DenseMatrix;
import org.apache.mahout.math.Matrix;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class DenseMatrixTest {

    private static final Logger logger = LogManager.getLogger(DenseMatrixTest.class);

    private static int size = 5;
    private static Matrix matrix = new DenseMatrix(size, size);

    @Test
    public void assigningTest() {
        logger.info("Assigning test");
        logger.info(matrix.asFormatString());
        matrix.assign(1d);

        assertTrue(matrix.get(0, 0) == 1d);
        assertTrue(matrix.get(2, 3) == 1d);

        logger.info(matrix.asFormatString());
    }


    @Before
    public void prepareMatrix() {
        matrix.assign(1d);
    }

    @Test
    public void multiplyingTest() {

        logger.info("Multiplying test");
        logger.info(matrix.asFormatString());

        Matrix temporaryMatrix = matrix.times(2d);

        logger.info(matrix.asFormatString());
        logger.info(temporaryMatrix.asFormatString());
    }

}
