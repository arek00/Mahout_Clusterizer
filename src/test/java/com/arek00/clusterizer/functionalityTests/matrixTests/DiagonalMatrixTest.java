package com.arek00.clusterizer.functionalityTests.matrixTests;


import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class DiagonalMatrixTest {
    private static final Logger logger = LogManager.getLogger(DiagonalMatrixTest.class);
    private static final int matrixSize = 5;
    private static RealMatrix matrix = new DiagonalMatrix(matrixSize);


    @Test
    public void matrixShouldBeIdentityMatrix() {
        logger.info(matrix.toString());
    }

}
