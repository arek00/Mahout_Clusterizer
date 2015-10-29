package com.arek00.clusterizer.functionalityTests.matrixTests;


import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.math.DenseMatrix;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.solver.EigenDecomposition;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

public class EigenValuesTest {

    private static final Logger logger = LogManager.getLogger(EigenValuesTest.class);

    private static final int matrixSize = 4;
    private static Matrix matrix = new DenseMatrix(matrixSize, matrixSize);

    @BeforeClass
    public static void initializeMatrix() {
//        5035.0625 −1553.0625 258.9375 −3740.938
//        −1553.0625 507.8125 5.3125 1039.938
//        258.9375 5.3125 2206.8125 −2471.062
//        −3740.9375 1039.9375 −2471.0625 5172.062

        logger.info("Initializing matrix");

        matrix.set(0, 0, 5035.0625);
        matrix.set(0, 1, -1553.0625);
        matrix.set(0, 2, 258.9375);
        matrix.set(0, 3, -3740.938);
        matrix.set(1, 0, -1553.0625);
        matrix.set(1, 1, 507.8125);
        matrix.set(1, 2, 5.3125);
        matrix.set(1, 3, 1039.938);
        matrix.set(2, 0, 258.9375);
        matrix.set(2, 1, 5.3125);
        matrix.set(2, 2, 2206.8125);
        matrix.set(2, 3, -2471.062);
        matrix.set(3, 0, -3740.9375);
        matrix.set(3, 1, 1039.9375);
        matrix.set(3, 2, -2471.0625);
        matrix.set(3, 3, 5172.062);

        logger.info("Matrix: ");
        logger.info(matrix.asFormatString());
    }

    @Test
    public void testEigenValues() {
        EigenDecomposition eigenDecomposition = new EigenDecomposition(matrix);
        String realValues = eigenDecomposition.getRealEigenvalues().asFormatString();
        String imagineValues = eigenDecomposition.getImagEigenvalues().asFormatString();

        logger.info("Eigen values decomposition test");
        logger.info("Real values");
        logger.info(realValues);
        logger.info("Imagine values");
        logger.info(imagineValues);


        String vMatrix = eigenDecomposition.getV().asFormatString();
        String dMatrix = eigenDecomposition.getD().asFormatString();

        logger.info("Decomposition matrixes");
        logger.info("V Matrix");
        logger.info(vMatrix);
        logger.info("D Matrix");
        logger.info(dMatrix);


        Array2DRowRealMatrix realMatrix = new Array2DRowRealMatrix(matrixSize, matrixSize);
        setRealMatrix(realMatrix, matrix);
        logger.info("Real Matrix");
        logger.info(realMatrix.toString());

        org.apache.commons.math3.linear.EigenDecomposition decomposition =
                new org.apache.commons.math3.linear.EigenDecomposition(realMatrix);

        double[] realValuesArray = decomposition.getRealEigenvalues();

        logger.info("Real values");

        Arrays.stream(realValuesArray)
                .forEach(value -> {
                    logger.info(value);
                });

        RealVector vector = decomposition.getEigenvector(0);

        logger.info(vector.toString());

    }


    private void setRealMatrix(RealMatrix realMatrix, Matrix matrix) {
        for(int row = 0; row < matrixSize; row++) {
            for(int column = 0; column < matrixSize; column++) {
                realMatrix.setEntry(row, column, matrix.get(row, column));
            }
        }
    }



}
