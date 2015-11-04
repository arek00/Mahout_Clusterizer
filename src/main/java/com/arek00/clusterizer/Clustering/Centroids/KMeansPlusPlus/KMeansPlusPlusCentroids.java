package com.arek00.clusterizer.Clustering.Centroids.KMeansPlusPlus;

import com.arek00.clusterizer.Clustering.Centroids.CentroidsGenerator;
import com.arek00.clusterizer.Clustering.SequenceFile.SequenceFileWriter;
import com.arek00.clusterizer.Utils.MahoutVectorConverter;
import com.arek00.clusterizer.Utils.PathValidator;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.clustering.kmeans.Kluster;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirValueIterable;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;
import java.util.List;

/**
 * Generate centroids using KMeans++ algorithm
 */
public class KMeansPlusPlusCentroids implements CentroidsGenerator {
    private static final Logger logger = LogManager.getLogger(KMeansPlusPlusCentroids.class);

    private Configuration configuration;
    @Setter @NonNull private KMeansPlusPlusParameters parameters;

    public KMeansPlusPlusCentroids(@NonNull Configuration configuration) {
        this.configuration = configuration;
        this.parameters = new KMeansPlusPlusParameters.Builder().build();
    }

    @Override
    public Path generateCentroids(Path vectors, Path output) throws Exception {
        PathValidator.removePathIfExists(output);

        KMeansPlusPlusClusterer<MahoutVectorClusterable> clusterer =
                new KMeansPlusPlusClusterer<MahoutVectorClusterable>(parameters.getClustersNumber(), parameters.getIterationsNumber());

        List<MahoutVectorClusterable> points = getVectorsFromDirectory(vectors);
        List<CentroidCluster<MahoutVectorClusterable>> clusters = clusterer.cluster(points);
        return saveClustersInOutputPath(output, clusters);

    }


    private List<MahoutVectorClusterable> getVectorsFromDirectory(@NonNull Path vectors) {
        SequenceFileDirValueIterable<VectorWritable> dirIterable =
                new SequenceFileDirValueIterable<VectorWritable>(vectors, PathType.LIST, PathFilters.partFilter(), configuration);

        List<MahoutVectorClusterable> clusterablePoints = Lists.newArrayList();

        dirIterable.forEach(vectorWritable -> {
            clusterablePoints.add(new MahoutVectorClusterable(vectorWritable.get()));
        });

        return clusterablePoints;
    }

    private Path saveClustersInOutputPath(Path output, List<CentroidCluster<MahoutVectorClusterable>> clustersList) {
        Path clustersDirectory = new Path(output, "centroids");
        Path clustersFile = new Path(clustersDirectory, "part-clusters");

        SequenceFileWriter writer = new SequenceFileWriter(configuration);
        List<Cluster> clusters = getClustersFromCentroidCluster(clustersList);
        List<Pair<Writable, Writable>> pairs = Lists.newArrayList();


        for (int index = 0; index < clusters.size(); index++) {
            IntWritable key = new IntWritable(index);
            ClusterWritable value = new ClusterWritable(clusters.get(index));
            Pair<Writable, Writable> pair = new Pair<>(key, value);
            pairs.add(pair);
        }

        try {
            writer.writeToSequenceFile(pairs, clustersFile, IntWritable.class, ClusterWritable.class);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return clustersDirectory;
    }

    private List<Cluster> getClustersFromCentroidCluster(List<CentroidCluster<MahoutVectorClusterable>> clusterList) {
        List<Cluster> mahoutClusters = Lists.newArrayList();

        for (int index = 0; index < clusterList.size(); index++) {
            Cluster cluster = clusterableToMahoutVector(index, clusterList.get(index));
            mahoutClusters.add(cluster);
        }

        return mahoutClusters;

    }

    private Cluster clusterableToMahoutVector(int clusterId, CentroidCluster<MahoutVectorClusterable> centroidCluster) {
        double[] point = centroidCluster.getCenter().getPoint();
        Vector center = MahoutVectorConverter.getVector(point);

        Kluster cluster = new Kluster(center, clusterId, new SquaredEuclideanDistanceMeasure());
        logger.info("Cluster " + clusterId + ": " + centroidCluster.getPoints().size() + " points");

        centroidCluster.getPoints()
                .forEach(centroidPoint -> {
                    Vector pointVector = MahoutVectorConverter.getVector(centroidPoint.getPoint());
                    cluster.observe(pointVector);
                });

        cluster.computeParameters();


        return cluster;
    }
}
