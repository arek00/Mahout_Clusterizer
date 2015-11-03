package com.arek00.clusterizer.Clustering.Centroids.StreamingKMeans;


import com.arek00.clusterizer.Clustering.SequenceFile.SequenceFileWriter;
import com.google.common.collect.Lists;
import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.clustering.kmeans.Kluster;
import org.apache.mahout.clustering.streaming.mapreduce.CentroidWritable;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirValueIterable;
import org.apache.mahout.math.Centroid;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class CentroidToClusterConverter {

    private static final Logger logger = LogManager.getLogger(CentroidToClusterConverter.class);

    private Configuration configuration;
    private DistanceMeasure measure;

    public CentroidToClusterConverter(@NonNull Configuration configuration, @NonNull DistanceMeasure measure) {
        this.measure = measure;
        this.configuration = configuration;
    }

    /**
     * Convert directory contains created centroids.
     *
     * @param directory
     * @return
     */
    public Path convertDirectory(Path directory, Path outputDirectory) throws IOException {
        List<Centroid> centroids = readCentroids(directory);
        return saveClusters(outputDirectory, centroids.iterator());
    }

    private Path saveClusters(Path outputSequenceDirectory, Iterator<Centroid> centroidIterator) throws IOException {
        SequenceFileWriter writer = new SequenceFileWriter(this.configuration);
        Path generatedClustersFile = new Path(outputSequenceDirectory, "part-generatedClusters");

        List<Pair<Writable, Writable>> clusters = Lists.newArrayList();

        centroidIterator.forEachRemaining(centroid -> {
            Cluster cluster = convert(centroid, this.measure);
            Text index = new Text(Integer.toString(cluster.getId()));
            ClusterWritable clusterWritable = new ClusterWritable(cluster);

            Pair<Writable, Writable> clusterPair = new Pair<>(index, clusterWritable);
            clusters.add(clusterPair);
        });

        writer.writeToSequenceFile(clusters, generatedClustersFile, Text.class, ClusterWritable.class);
        return outputSequenceDirectory;
    }

    private List<Centroid> readCentroids(Path directory) {
        List<Centroid> centroids = Lists.newArrayList();

        SequenceFileDirValueIterable<Writable> directoryIterable =
                new SequenceFileDirValueIterable<Writable>
                        (directory, PathType.LIST, PathFilters.partFilter(), this.configuration);

        directoryIterable.forEach(sequenceValue -> {
            if (sequenceValue instanceof CentroidWritable) {
                centroids.add(((CentroidWritable) sequenceValue).getCentroid());
            }
        });

        return centroids;
    }

    private Cluster convert(Centroid centroid, DistanceMeasure measure) {
        Kluster cluster = new Kluster(centroid.getVector(), centroid.getIndex(), measure);

        return cluster;
    }
}
