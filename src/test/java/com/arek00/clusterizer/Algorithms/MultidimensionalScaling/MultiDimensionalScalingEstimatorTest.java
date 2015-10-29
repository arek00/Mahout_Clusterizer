package com.arek00.clusterizer.Algorithms.MultidimensionalScaling;

import com.arek00.clusterizer.Algorithms.MatrixUtils.MatrixConverter;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


public class MultiDimensionalScalingEstimatorTest {

    private static final Logger logger = LogManager.getLogger(MultiDimensionalScalingEstimatorTest.class);

    private static int citiesAmount = 4;
    private static Vector[] denmarkCities = new Vector[citiesAmount];
    private static MultiDimensionalScalingEstimator mdsEstimator =
            new MultiDimensionalScalingEstimator(new SquaredEuclideanDistanceMeasure(), 2);

    @BeforeClass
    public static void prepareTestData() {
//        −62.831 −32.97448
//        18.403 12.02697
//        −24.960 39.71091
//        69.388 −18.76340

        Vector Kobenhavn = new RandomAccessSparseVector(2);
        Kobenhavn.set(0, 62.831);
        Kobenhavn.set(1, 32.97448);

        Vector Aarhus = new RandomAccessSparseVector(2);
        Aarhus.set(0, -18.403);
        Aarhus.set(1, -12.02697);

        Vector Odense = new RandomAccessSparseVector(2);
        Odense.set(0, 24.960);
        Odense.set(1, -39.71091);

        Vector Aalborg = new RandomAccessSparseVector(2);
        Aalborg.set(0, -69.388);
        Aalborg.set(1, 18.7634);

        denmarkCities[0] = Kobenhavn;
        denmarkCities[1] = Aarhus;
        denmarkCities[2] = Odense;
        denmarkCities[3] = Aalborg;
    }



    @Test
    public void testMDSEstimation() throws Exception {
        RealMatrix mdsEstimatedPoints = mdsEstimator.MDSEstimation(denmarkCities);
        Matrix mdsPoints = MatrixConverter.realMatrixToMahoutMatrix(mdsEstimatedPoints);

        logger.info(mdsPoints.asFormatString());

    }
}