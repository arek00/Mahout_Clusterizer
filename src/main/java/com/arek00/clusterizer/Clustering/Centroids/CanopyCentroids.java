package com.arek00.clusterizer.Clustering.Centroids;

import com.arek00.clusterizer.validators.NumberValidator;
import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.lda.LDAPrintTopics;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;

import java.io.IOException;

/**
 * Create centroids with Canopy Clustering algorithm.
 */
public class CanopyCentroids implements CentroidsGenerator {

    private Configuration configuration;
    private double T1, T2;
    private DistanceMeasure measure = new SquaredEuclideanDistanceMeasure();

    public CanopyCentroids(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Set thresholds of Canopy algorithm.
     * T1 has to be greater than T2.
     * @param T1
     * @param T2
     */
    public void setCanopyThresholds(double T1, double T2) {
        NumberValidator.greaterThan("T1 has to be greater than T2", T2, T1);

        this.T1 = T1;
        this.T2 = T2;

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
     * Generate centroids with using Canopy Clustering algorithm.
     * Set thresholds before running.
     *
     * @param vectors Path where vectors are stored.
     * @param output
     * @throws Exception
     */
    @Override
    public Path generateCentroids(Path vectors, Path output) throws InterruptedException, IOException, ClassNotFoundException {
        CanopyDriver.run(
                this.configuration,
                vectors, output,
                measure,
                T1, T2,
                true, 0, true);
    return new Path(output, "clusters-0-final");

    }
}
