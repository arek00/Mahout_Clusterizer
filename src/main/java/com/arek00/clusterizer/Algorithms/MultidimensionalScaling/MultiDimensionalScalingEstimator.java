package com.arek00.clusterizer.Algorithms.MultidimensionalScaling;


import com.arek00.clusterizer.Utils.VectorDistance;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.*;
import org.apache.mahout.math.Vector;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.Arrays;

public class MultiDimensionalScalingEstimator {

    private DistanceMeasure measure;


    public MultiDimensionalScalingEstimator(@NonNull DistanceMeasure measure) {
        this.measure = measure;
    }

    public Point2D[] MDSEstimation(@NonNull Vector[] points) {
        Matrix proximitiesMatrix = calculateProximitiesMatrix(points);
        Matrix bMatrix = applyDoubleCentering(proximitiesMatrix);
        return null;

    }

    /**
     * Prepare matrix of distances between points given in argument
     *
     * @param points
     */
    private Matrix calculateProximitiesMatrix(@NonNull Vector[] points) {
        int matrixSize = points.length;
        Matrix proximitiesMatrix = new SparseMatrix(matrixSize, matrixSize);
        int maxIteration = (int) Math.ceil(((double) matrixSize) / 2);

        for (int row = 0; row < maxIteration; row++) {
            for (int column = 0; column < matrixSize; column++) {

                if (row == column) {
                    proximitiesMatrix.set(column, row, 0d);
                } else {
                    double distance = VectorDistance.distance(points[row], points[column], measure);
                    proximitiesMatrix.set(column, row, distance);
                    proximitiesMatrix.set(row, column, distance);
                }
            }
        }

        return proximitiesMatrix;
    }

    private Matrix applyDoubleCentering(Matrix proximitiesMatrix) {
        int size = proximitiesMatrix.columnSize();
        Matrix jMatrix = estimateJMatrix(size);

        Matrix bMatrix = jMatrix.times(-0.5);
        bMatrix = bMatrix.times(proximitiesMatrix);
        bMatrix = bMatrix.times(jMatrix);
        return bMatrix;
    }

    private Matrix estimateJMatrix(int size) {
        Matrix identityMatrix = new DiagonalMatrix(1d, size);
        double nScalar = 1 / size;
        Matrix onesMatrix = new DenseMatrix(size, size);

        onesMatrix.assign(1d);

        Matrix jMatrix = identityMatrix.minus(onesMatrix.times(nScalar));
        return jMatrix;
    }

    /**
     * Return spatial configuration of points containing matrix of coordinates.
     *
     * @param bMatrix
     * @return
     */
    private Matrix getSpatialConfiguration (Matrix bMatrix) {

        RealMatrix realMatrix = MatrixConverter.mahoutMatrixToRealMatrix(bMatrix);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(realMatrix);


return null;


    }

    /**
     * Get eigenValues and vectors from eigen decomposition and set them in passed arguments.
     *
     * @param eigenDecomposition
     */
    private EigenPair[] setEigenValuesAndVectors (int mdsDimensions, int bMatrixSize, EigenDecomposition eigenDecomposition) {




    }

    private EigenPair[] getNMaxValues(int amount, EigenDecomposition decomposition) {
        EigenPair[] pairs = new EigenPair[amount];

        int[] largestValuesIndexes = getMaxValuesIndexes(amount, decomposition.getRealEigenvalues());



    }


    private int[] getMaxValuesIndexes(int amount, double[] eigenValues) {

        int[] largestValuesIndexes = new int[amount];

        for(int iteration = 0; iteration < eigenValues.length; iteration++) {
            if(iteration < eigenValues.length) {
                largestValuesIndexes[iteration] = iteration;
                continue;
            }

            int minIndex = getMinNumberIndex(largestValuesIndexes, eigenValues);

            if(eigenValues[minIndex] < eigenValues[iteration]) {
                largestValuesIndexes[minIndex] = iteration;
            }
        }

        return largestValuesIndexes;
    }


    /**
     * Get index of minimum value of array.
     *
     * @param indexes
     * @param eigenValues
     * @return
     */
    private int getMinNumberIndex(int[] indexes, double[] eigenValues) {
        int minIndex = indexes[0];

        for(int iteration = 1; iteration < indexes.length; iteration++) {
            if(eigenValues[indexes[iteration]] < eigenValues[minIndex] ) {
                minIndex = indexes[iteration];
            }
        }
        return minIndex;
    }


}


class EigenPair {
    @Getter private double eigenValue;
    @Getter private RealVector eigenVector;

    public EigenPair(double eigenValue, RealVector eigenVector) {
        this.eigenValue = eigenValue;
        this.eigenVector = eigenVector;
    }
}