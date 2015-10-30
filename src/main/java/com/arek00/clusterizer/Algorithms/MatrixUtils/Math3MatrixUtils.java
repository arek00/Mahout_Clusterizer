package com.arek00.clusterizer.Algorithms.MatrixUtils;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class Math3MatrixUtils {

    public static RealMatrix getIdentityMatrix(int rows, int columns, double diagonalValue) {
        RealMatrix matrix = new Array2DRowRealMatrix(rows, columns);

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (row == column) {
                    matrix.setEntry(row, column, diagonalValue);
                } else {
                    matrix.setEntry(row, column, 0);
                }
            }
        }

        return matrix;
    }

    /**
     * Get matrix with same values on each entry.
     *
     * @param rows
     * @param columns
     * @param value
     * @return
     */
    public static RealMatrix getClearMatrix(int rows, int columns, double value) {
        RealMatrix matrix = new Array2DRowRealMatrix(rows, columns);

        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                matrix.setEntry(row, column, value);
            }
        }

        return matrix;
    }


}
