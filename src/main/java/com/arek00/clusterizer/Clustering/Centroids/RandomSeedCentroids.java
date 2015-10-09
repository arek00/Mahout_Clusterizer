package com.arek00.clusterizer.Clustering.Centroids;

import com.arek00.clusterizer.validators.NumberValidator;
import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.kmeans.RandomSeedGenerator;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;

import java.io.IOException;

/**
 * Create random centroids.
 */
public class RandomSeedCentroids implements CentroidsGenerator{

    private Configuration configuration;
    private int kPoints = 2;

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
     * Create random centroids points from given vectors.
     * Save result in output directory.
     *
     * @param vectors Path of vectorized documents i.e tfidf-vectors
     * @param output
     */
    public Path generateCentroids(Path vectors, Path output) throws IOException {
        return RandomSeedGenerator.buildRandom(this.configuration, vectors, output, kPoints, new EuclideanDistanceMeasure());
    }

}
