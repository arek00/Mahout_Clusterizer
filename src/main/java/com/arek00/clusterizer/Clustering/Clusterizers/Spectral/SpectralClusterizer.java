package com.arek00.clusterizer.Clustering.Clusterizers.Spectral;


import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.spectral.kmeans.SpectralKMeansDriver;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;

import java.io.IOException;

public class SpectralClusterizer {


    private Configuration configuration;

    public SpectralClusterizer(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }


    public void runClustering(Path inputVectors, Path outputDirectory, SpectralParameters parameters) throws InterruptedException, IOException, ClassNotFoundException {

        SpectralKMeansDriver.run(this.configuration,
                inputVectors,
                outputDirectory,
                parameters.getNumDims(),
                parameters.getClusters(),
                new EuclideanDistanceMeasure(),
                parameters.getConvergenceDelta(),
                parameters.getMaxIterations(),
                new Path(outputDirectory, "temp"),
                parameters.isSsvd()
                );

    }
}
