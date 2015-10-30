package com.arek00.clusterizer.Algorithms.MultidimensionalScaling;


import com.arek00.clusterizer.Algorithms.MatrixUtils.Math3MatrixUtils;
import com.arek00.clusterizer.Utils.VectorDistance;
import com.arek00.clusterizer.validators.NumberValidator;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.math3.linear.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.Vector;

import java.util.Arrays;

public class MultiDimensionalScalingEstimator {

    private static final Logger logger = LogManager.getLogger(MultiDimensionalScalingEstimator.class);

    private DistanceMeasure measure;
    private int dimensions;

    public MultiDimensionalScalingEstimator(int dimensions) {
        NumberValidator.greaterThan("Set number of dimensions greater than 0", 0, dimensions);
        this.measure = new EuclideanDistanceMeasure();
        this.dimensions = dimensions;

        logger.info("Number of dimensions to scale: " + dimensions);
    }

    public RealMatrix MDSEstimation(@NonNull Vector[] points) {

        logger.info("Run estimation.");

        RealMatrix proximitiesMatrix = calculateProximitiesMatrix(points);
        RealMatrix bMatrix = applyDoubleCentering(proximitiesMatrix);
        return getSpatialConfiguration(bMatrix);
    }

    /**
     * Prepare matrix of distances between points given in argument
     *
     * @param points
     */
    private RealMatrix calculateProximitiesMatrix(@NonNull Vector[] points) {

        logger.info("Start creating proximities matrix.");

        int matrixSize = points.length;
        RealMatrix proximitiesMatrix = new Array2DRowRealMatrix(matrixSize, matrixSize);
        int maxIteration = (int) Math.ceil(((double) matrixSize) / 2);

        for (int row = 0; row < matrixSize; row++) {
            for (int column = 0; column < matrixSize; column++) {

                if (row == column) {
                    proximitiesMatrix.setEntry(column, row, 0d);
                } else {
                    double distance = VectorDistance.distance(points[row], points[column], measure);
                    distance = Math.pow(distance, 2d);
                    proximitiesMatrix.setEntry(column, row, distance);
                    proximitiesMatrix.setEntry(row, column, distance);
                }
            }
        }

        logger.info("Proximities matrix estimated");

        return proximitiesMatrix;
    }

    private RealMatrix applyDoubleCentering(RealMatrix proximitiesMatrix) {
        logger.info("Applying double centering");

        int size = proximitiesMatrix.getColumnDimension();
        RealMatrix jMatrix = calculateJMatrix(size);

        logger.info("jMatrix estimated.");

        RealMatrix bMatrix = jMatrix.scalarMultiply(-0.5);
        bMatrix = bMatrix.multiply(proximitiesMatrix);
        bMatrix = bMatrix.multiply(jMatrix);

        logger.info("bMatrix estimated.");
        return bMatrix;
    }

    private RealMatrix calculateJMatrix(int size) {
        RealMatrix identityMatrix = Math3MatrixUtils.getIdentityMatrix(size, size, 1);
        double nScalar = 1d / size;
        RealMatrix onesMatrix = Math3MatrixUtils.getClearMatrix(size, size, 1);
        onesMatrix = onesMatrix.scalarMultiply(nScalar);
        RealMatrix jMatrix = identityMatrix.subtract(onesMatrix);

        return jMatrix;
    }

    /**
     * Return spatial configuration of points containing matrix of coordinates.
     *
     * @param bMatrix
     * @return
     */
    private RealMatrix getSpatialConfiguration(RealMatrix bMatrix) {
        EigenDecomposition eigenDecomposition = new EigenDecomposition(bMatrix);
        EigenPair[] pairs = getNMaxEigenValues(this.dimensions, eigenDecomposition);
        RealMatrix eigenVectorsMatrix = getEigenVectrosMatrix(bMatrix.getColumnDimension(), pairs, true);
        RealMatrix eigenLambdasMatrix = getEigenLambdasMatrix(this.dimensions, pairs, true);

        return eigenVectorsMatrix.multiply(eigenLambdasMatrix);
    }

    private RealMatrix getEigenVectrosMatrix(int elementsNumber, EigenPair[] pairs, boolean orderedAscend) {
        RealMatrix eigenVectorsMatrix = new Array2DRowRealMatrix(elementsNumber, this.dimensions);

        logger.info("Get Eigen Vectors Matrix. Creater matrix: " +
                eigenVectorsMatrix.getRowDimension() + "x" + eigenVectorsMatrix.getColumnDimension());

        int matrixStride = eigenVectorsMatrix.getColumnDimension();
        for(int column = 0; column < matrixStride; column++) {
            int arrayIndex = orderedAscend ? pairs.length -1 - column : column;

            eigenVectorsMatrix.setColumnVector(column, pairs[arrayIndex].getEigenVector());
        }

        return eigenVectorsMatrix;
    }

    private RealMatrix getEigenLambdasMatrix(int dimensions, EigenPair[] pairs, boolean orderedAscend) {
        RealMatrix lambdasMatrix = Math3MatrixUtils.getClearMatrix(dimensions, dimensions, 0);

        for(int iteration = 0; iteration < dimensions; iteration++) {
            int index = orderedAscend ? pairs.length - 1 - iteration : iteration;
            double squaredRootedLambda = Math.sqrt(pairs[index].getEigenValue());
            lambdasMatrix.setEntry(iteration,iteration, squaredRootedLambda);
        }

        lambdasMatrix.scalarMultiply(-1);

        return lambdasMatrix;
    }



    private EigenPair[] getNMaxEigenValues(int amount, EigenDecomposition decomposition) {
        EigenPair[] pairs = new EigenPair[amount];
        int[] largestValuesIndexes = getMaxValuesIndexes(amount, decomposition.getRealEigenvalues());
        final int[] currentIndex = {0};

        Arrays.stream(largestValuesIndexes)
                .forEach(index -> {
                    double eigenValue = decomposition.getRealEigenvalue(index);
                    RealVector eigenVector = decomposition.getEigenvector(index);
                    pairs[currentIndex[0]] = new EigenPair(eigenValue, eigenVector, index);
                    currentIndex[0]++;
                });

        Arrays.sort(pairs);

        return pairs;
    }


    private int[] getMaxValuesIndexes(int amount, double[] eigenValues) {

        int[] largestValuesIndexes = new int[amount];

        for (int iteration = 0; iteration < eigenValues.length; iteration++) {
            if (iteration < largestValuesIndexes.length) {
                largestValuesIndexes[iteration] = iteration;
                continue;
            }

            int minIndex = getMinNumberIndex(largestValuesIndexes, eigenValues);

            if (eigenValues[minIndex] < eigenValues[iteration]) {
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

        for (int iteration = 1; iteration < indexes.length; iteration++) {
            if (eigenValues[indexes[iteration]] < eigenValues[minIndex]) {
                minIndex = indexes[iteration];
            }
        }
        return minIndex;
    }


}


class EigenPair implements Comparable<EigenPair>{
    @Getter private double eigenValue;
    @Getter private RealVector eigenVector;
    @Getter private int arrayIndex;

    public EigenPair(double eigenValue, RealVector eigenVector, int arrayIndex) {
        this.eigenValue = eigenValue;
        this.eigenVector = eigenVector;
        this.arrayIndex = arrayIndex;
    }

    @Override
    public int compareTo(EigenPair o) {
        if(o.getEigenValue() > eigenValue) {
            return -1;
        }
        else if(o.getEigenValue() < eigenValue) {
            return 1;
        }
        return 0;
    }
}