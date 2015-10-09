package com.arek00.clusterizer.Clustering.Centroids;

import com.arek00.clusterizer.validators.NumberValidator;
import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;

import java.io.IOException;

/**
 * Create centroids with Canopy Clustering algorithm.
 */
public class CanopyCentroids implements CentroidsGenerator {

    private Configuration configuration;
    private double T1, T2;

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
        NumberValidator.greaterThan("T1 has to be greater than T2", T1, T2);

        this.T1 = T1;
        this.T2 = T2;

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
                new EuclideanDistanceMeasure(),
                T1, T2,
                true, 0, true);
    return new Path(output, "clusters-0-final");

    }
}
