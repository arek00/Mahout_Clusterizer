package com.arek00.clusterizer.demos.display.SequenceFileUtils;


import com.arek00.clusterizer.demos.ClusteredPoint;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.classify.WeightedPropertyVectorWritable;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileIterable;
import org.apache.mahout.math.NamedVector;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
                forEach(i -> {
//
//                            ClusterWritable clusterWritable = (ClusterWritable) i.getSecond();
//                            Cluster cluster = clusterWritable.getValue();
//
//                            System.out.format("Cluster vector ID: %s Center: %s \n", cluster.getId(), cluster.getCenter());
//

                            WeightedPropertyVectorWritable vectorWritable = (WeightedPropertyVectorWritable) i.getSecond();
                            String vectorClassName = vectorWritable.getVector().getClass().toString();
                            NamedVector vector = (NamedVector) vectorWritable.getVector();

                            System.out.format("Vector name: %s\n", vector.getName());
//
//                            System.out.format("Cluster nr: %s -> %s\n", i.getFirst().getClass().toString(), i.getSecond().getClass().toString());
//                            System.out.format("Cluster nr: %s -> %s\n", i.getFirst(), i.getSecond());
                        }
                );
    }

    public static List<ClusteredPoint> getPoints(Path sequenceFilePath) {

        List<ClusteredPoint> pointsList;
        SequenceFileIterable<Writable, Writable> iterable =
                new SequenceFileIterable<Writable, Writable>(sequenceFilePath, SequencePrinter.configuration);

        pointsList = StreamSupport.stream(iterable.spliterator(), false).
                map(i -> {
                    return new ClusteredPoint(i.getFirst().toString(),
                            i.getSecond().toString());
                }).collect(Collectors.toList());

        System.out.format("Created %d points.", pointsList.size());

        return pointsList;
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