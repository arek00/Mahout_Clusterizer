package com.arek00.clusterizer.Clustering.Centroids;

import com.arek00.clusterizer.Clustering.Centroids.CentroidsGenerator;
import com.arek00.clusterizer.validators.NumberValidator;
import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.kmeans.RandomSeedGenerator;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;

import java.io.IOException;

/**
 * Create random centroids.
 */
public class RandomSeedCentroids implements CentroidsGenerator {

    private Configuration configuration;
    private int kPoints = 2;
    private DistanceMeasure measure = new SquaredEuclideanDistanceMeasure();

    public RandomSeedCentroids(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Set amount of random generated k points.
     *
     * @param kPoints integer number more than 1.
     */
    public void setKPoints(int kPoints) {

        NumberValidator.greaterThan("Argument has to be greater than 1", 1, kPoints);
        this.kPoints = kPoints;
    }

    /**
     * Set distance measure method.
     * Default is SquaredEuclideanDistanceMeasure
     *
     * @param measure
     */
    public void setDistanceMeasure(@NonNull DistanceMeasure measure) {
        this.measure = measure;
    }


    /**
     * Create random centroids points from given vectors.
     * Save result in output directory.
     *
     * @param vectors Path of vectorized documents i.e tfidf-vectors
     * @param output
     */
    public Path generateCentroids(Path vectors, Path output) throws IOException {
        return RandomSeedGenerator.buildRandom(this.configuration,
                vectors,
                output,
                kPoints,
                measure
        );
    }

}
