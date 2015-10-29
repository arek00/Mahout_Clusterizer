package com.arek00.clusterizer.Algorithms.MultidimensionalScaling;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.mahout.math.Matrix;

/**
 * Class that lets converts matrixes between Mahout matrixes and
 * Apache Commons Math3 Matrixes.
 */
public class MatrixConverter {


    /**
     * Get Apache Commons Math3 RealMatrix from Mahout Math Matrix
     *
     * @param matrix
     * @return
     */
    public static RealMatrix mahoutMatrixToRealMatrix(Matrix matrix) {

        RealMatrix realMatrix = new Array2DRowRealMatrix(matrix.rowSize(), matrix.columnSize());

        for(int row = 0; row < matrix.rowSize(); row++ ) {
            for(int column = 0; column < matrix.columnSize(); column++ ) {
                realMatrix.setEntry(row,column,matrix.get(row,column));
            }
        }

        return realMatrix;

    }

}
