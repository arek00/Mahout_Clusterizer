package com.arek00.clusterizer.Algorithms.MultidimensionalScaling;


import com.arek00.clusterizer.Utils.VectorDistance;
import lombok.NonNull;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.*;
import org.apache.mahout.math.hadoop.decomposer.EigenVector;
import org.apache.mahout.math.solver.EigenDecomposition;

import java.awt.geom.Point2D;

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

    private double[] getEigenValues(Matrix bMatrix) {
        EigenDecomposition decomposition = new EigenDecomposition(bMatrix);
        decomposition.getRealEigenvalues();
        return null;


    }


}
