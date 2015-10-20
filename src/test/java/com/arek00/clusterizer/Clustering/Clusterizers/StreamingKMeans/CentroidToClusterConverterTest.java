package com.arek00.clusterizer.Clustering.Clusterizers.StreamingKMeans;

import com.google.common.collect.Lists;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.canopy.Canopy;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.kmeans.Kluster;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirValueIterable;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;


public class CentroidToClusterConverterTest {
    private static final Logger logger = LogManager.getLogger(CentroidToClusterConverterTest.class);

    public static Configuration configuration;
    public static Path clusterIn;
    public static CentroidToClusterConverter converter;
    public Collection<Cluster> clusters;


    @BeforeClass
    public static void init() {
        configuration = new Configuration();
        converter = new CentroidToClusterConverter(configuration, new SquaredEuclideanDistanceMeasure());
        clusterIn = new Path("/home/arek/clusterizer/streamingKMeansTest/streamingKMeans/clusters");
    }

    @Test
    public void  test() {
        clusters = Lists.newArrayList();
        configureWithClusterInfo(configuration, clusterIn, clusters);
    }

    public static void configureWithClusterInfo(Configuration conf, Path clusterPath, Collection<Cluster> clusters) {

        logger.info("Start configureWithClusterInfo method");
        int pairsCounter = 0;

        for (Writable value : new SequenceFileDirValueIterable<Writable>(clusterPath, PathType.LIST,
                PathFilters.partFilter(), conf)) {
            Class<? extends Writable> valueClass = value.getClass();

            logger.info("Class of value: " + valueClass.getName());

            if (valueClass.equals(ClusterWritable.class)) {
                ClusterWritable clusterWritable = (ClusterWritable) value;
                value = clusterWritable.getValue();
                valueClass = value.getClass();
            }
            if (valueClass.equals(Kluster.class)) {
                // get the cluster info
                clusters.add((Kluster) value);
            } else if (valueClass.equals(Canopy.class)) {
                // get the cluster info
                Canopy canopy = (Canopy) value;
                clusters.add(new Kluster(canopy.getCenter(), canopy.getId(), canopy.getMeasure()));
            } else {
                throw new IllegalStateException("Bad value class: " + valueClass);
            }

            ++pairsCounter;
        }

        logger.info("Counted " + pairsCounter + " from file: " + clusterIn);
    }


    @Test
    public void checkTypesInSequenceFile() {
        SequenceFileDirValueIterable<Writable> iterable = new SequenceFileDirValueIterable<Writable>(clusterIn, PathType.LIST, configuration);

        iterable.forEach(element -> {
            logger.info(element.getClass().getName());
        });
    }
}