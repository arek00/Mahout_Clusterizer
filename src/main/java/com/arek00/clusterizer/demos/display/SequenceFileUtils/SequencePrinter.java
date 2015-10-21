package com.arek00.clusterizer.demos.display.SequenceFileUtils;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.classify.WeightedPropertyVectorWritable;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.clustering.streaming.mapreduce.CentroidWritable;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileIterable;
import org.apache.mahout.math.Centroid;
import org.apache.mahout.math.ConstantVector;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SequencePrinter {

    private static Configuration configuration = new Configuration();


    public static void printSequence(Path sequenceFilePath) {

        SequenceFileIterable<Writable, Writable> iterable =
                new SequenceFileIterable<Writable, Writable>(sequenceFilePath, SequencePrinter.configuration);

        StreamSupport.stream(iterable.spliterator(), false).
                forEach(pair -> {

                            if (pair.getSecond() instanceof ClusterWritable) {
                                System.out.format("Cluster number: %s: ", pair.getFirst());
                                printCluster((ClusterWritable) pair.getSecond());
                            } else if (pair.getSecond() instanceof WeightedPropertyVectorWritable) {
                                System.out.format("Cluster number %s: ", pair.getFirst());
                                printPoint((WeightedPropertyVectorWritable) pair.getSecond());
                            } else if (pair.getSecond() instanceof CentroidWritable) {
                                printCentroid((IntWritable) pair.getFirst(), (CentroidWritable) pair.getSecond());
                            } else {
                                System.out.format("First class: %s, Second class: %s", pair.getFirst().getClass().getName(),
                                        pair.getSecond().getClass().getName());
                            }

                            System.out.print("\n");
                        }
                );
    }


    private static void printPoint(WeightedPropertyVectorWritable point) {
        double distanceFromZero = calculateDistanceFromZeroPoint(point.getVector());
        Map<Text, Text> propertiesMap = point.getProperties();

        StringBuilder properties = new StringBuilder();

        propertiesMap.keySet().stream()
                .forEach(key -> {
                    properties
                            .append("Key: ")
                            .append(key.toString())
                            .append(" Value: ")
                            .append(propertiesMap.get(key));

                });

        System.out.format("Point's properties: Weight: %s, Distance from 0: %s, Other properties: %s", point.getWeight(), distanceFromZero, properties);

        if (point.getVector() instanceof NamedVector) {
            String pointName = ((NamedVector) point.getVector()).getName();

            System.out.format(" Point's Name: %s", pointName);
        }
    }

    private static double calculateDistanceFromZeroPoint(Vector vector) {
        Vector zeroVector = new ConstantVector(0d, vector.size());
        EuclideanDistanceMeasure measurement = new EuclideanDistanceMeasure();
        return measurement.distance(zeroVector, vector);
    }

    private static void printCluster(ClusterWritable clusterWritable) {
        Cluster cluster = clusterWritable.getValue();
        double distanceFromZero = calculateDistanceFromZeroPoint(cluster.getCenter());

        System.out.format("Cluster's properties: Cluster ID: %s, Cluster's distance from 0: %s, Is Converged: %s, " +
                        "\nCluster radius: %s",
                cluster.getId(), distanceFromZero, cluster.isConverged(), cluster.getRadius().toString());
    }


    private static void printCentroid(IntWritable index, CentroidWritable centroidWritable) {
        int centroidIndex = index.get();
        Centroid centroid = centroidWritable.getCentroid();

        double distanceFromZero = calculateDistanceFromZeroPoint(centroid.getVector());

        System.out.format("ID: %s. Centroid properties: Centroid weight: %s, Centroid distance from 0: %s", centroid.getIndex(), centroid.getWeight(), distanceFromZero);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
        } else {

            Arrays.stream(args).forEach(
                    (i) -> {
                        System.out.println("Print file: " + i);
                        printSequence(new Path(i));
                    });
        }
    }

    private static void printHelp() {
        String helpText = "Pass sequenceFile path in arguments to print them";

        System.out.println(helpText);
    }

}