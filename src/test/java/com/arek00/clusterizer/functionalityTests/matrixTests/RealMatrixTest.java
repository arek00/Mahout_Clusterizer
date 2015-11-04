package com.arek00.clusterizer.functionalityTests.matrixTests;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

public class RealMatrixTest {
    private static final Logger logger = LogManager.getLogger(RealMatrixTest.class);
    private static RealMatrix matrix = new Array2DRowRealMatrix(3,4);

    @Test
    public void matrixDimensionsTest() {
        logger.info(matrix.toString());
    }


}
