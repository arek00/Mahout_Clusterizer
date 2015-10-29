package com.arek00.clusterizer.Algorithms.MatrixUtils;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.mahout.math.DenseMatrix;
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

    public static Matrix realMatrixToMahoutMatrix(RealMatrix matrix) {

        Matrix mahoutMatrix = new DenseMatrix(matrix.getRowDimension(), matrix.getColumnDimension());

        for(int row = 0; row < matrix.getRowDimension(); row++ ) {
            for(int column = 0; column < matrix.getColumnDimension(); column++ ) {
                mahoutMatrix.set(row,column,matrix.getEntry(row,column));
            }
        }

        return mahoutMatrix;
    }

}
