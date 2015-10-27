package com.arek00.clusterizer.functionalityTests.distanceTests;

import com.arek00.clusterizer.Utils.VectorDistance;
import com.google.common.collect.Lists;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.classify.WeightedPropertyVectorWritable;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirValueIterable;
import org.apache.mahout.math.ConstantVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class ClustersDistanceTest {

    private static final Logger logger = LogManager.getLogger(ClustersDistanceTest.class);
    private static List<Cluster> clusters;
    private static List<Vector> clusteredPoints;
    private static Configuration configuration = new Configuration();

    @BeforeClass
    public static void init() {
        readClusters();
        readPoints();
    }

    private static void readClusters() {
        String clusterResource = getPath("/clusteredData/kmeans/clusters-1-final");
        Path clustersFile = new Path(clusterResource);

        logger.info("Read directory: " + clusterResource);

        SequenceFileDirValueIterable<ClusterWritable> clustersIterable =
                new SequenceFileDirValueIterable<ClusterWritable>
                        (clustersFile, PathType.LIST, PathFilters.partFilter(), configuration);

        clusters = Lists.newArrayList();
        clustersIterable.forEach(clusterWritable -> {
            clusters.add(clusterWritable.getValue());
        });

        logger.info("Read " + clusters.size() + " clusters");
    }

    private static void readPoints() {
        String pointsResource = getPath("/clusteredData/kmeans/clusteredPoints");

        Path pointsFile = new Path(pointsResource);

        logger.info("Read directory: " + pointsResource);

        SequenceFileDirValueIterable<WeightedPropertyVectorWritable> vectorsIterable =
                new SequenceFileDirValueIterable<WeightedPropertyVectorWritable>
                        (pointsFile, PathType.LIST, PathFilters.partFilter(), configuration);

        clusteredPoints = Lists.newArrayList();

        vectorsIterable.forEach(vectorWritable -> {
            clusteredPoints.add(vectorWritable.getVector());
        });

        logger.info("Read " + clusteredPoints.size() + " points");
    }


    private static String getPath(String resourceName) {
        URL url = ClustersDistanceTest.class.getResource(resourceName);
        URI uri = null;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            logger.error("Couldn't create uri.");
            e.printStackTrace();
        }

        java.nio.file.Path path = Paths.get(uri);
        return path.toString();
    }

    public double clustersDistanceFromZero(Cluster cluster) {
        int zeroVectorSize = clusters.get(0).getCenter().size();
        Vector zeroVector = new ConstantVector(0d, zeroVectorSize);

        return VectorDistance.distance(cluster.getCenter(),
                zeroVector, new SquaredEuclideanDistanceMeasure());
    }

    public double clustersDistanceFromEachOthers(Cluster cluster1, Cluster cluster2) {
        return VectorDistance.distance(cluster1.getCenter(), cluster2.getCenter(),
                new SquaredEuclideanDistanceMeasure());
    }


    @Test
    public void testDistanceBetweenClustersAndZeroPoint() {
        int clustersCount = clusters.size();

        for(Cluster cluster : clusters)  {
            for(Cluster clusterToCompare : clusters) {

                if(cluster == clusterToCompare) {
                    continue;
                }

                double clusterToZero = clustersDistanceFromZero(cluster);
                double comparingClusterToZero = clustersDistanceFromZero(clusterToCompare);
                double distanceFromClusters = clustersDistanceFromEachOthers(cluster, clusterToCompare);

                assertTrue(Math.abs(clusterToZero - comparingClusterToZero) != distanceFromClusters);

                logger.info("Cluster A: " + cluster.getId() + " Cluster B: " + clusterToCompare.getId());
                logger.info("Distance from A to B: " + distanceFromClusters);
                logger.info("Distance from A to 0: " + clusterToZero);
                logger.info("Distance from B to 0: " + comparingClusterToZero);
            }

        }


    }

}
