package com.arek00.clusterizer.Clustering.Clusterizers.StreamingKMeans;


import com.arek00.clusterizer.Clustering.SequenceFile.SequenceFileWriter;
import lombok.NonNull;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.SequenceFileRecordReader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.canopy.Canopy;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.clustering.kmeans.Kluster;
import org.apache.mahout.clustering.streaming.mapreduce.CentroidWritable;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileIterable;
import org.apache.mahout.math.Centroid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CentroidToClusterConverter {

    private static final Logger logger = LogManager.getLogger(CentroidToClusterConverter.class);

    private Configuration configuration;
    private FileSystem fileSystem;
    private DistanceMeasure measure;

    public CentroidToClusterConverter(@NonNull Configuration configuration, @NonNull DistanceMeasure measure) {
        this.measure = measure;
        this.configuration = configuration;

        try {
            this.fileSystem = FileSystem.get(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        List<Pair<Writable, Writable>> clusters;
        clusters = new ArrayList<Pair<Writable, Writable>>();

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
        File directoryFileInstance = new File(directory.toString());
        List<Centroid> centroids = new ArrayList<Centroid>();
        Iterator<File> sequenceFiles = FileUtils.iterateFiles(directoryFileInstance, null, false);

        sequenceFiles.forEachRemaining(file -> {
            getCentroidsFromFile(new Path(file.getAbsolutePath()))
                    .forEachRemaining(centroid -> {
                        centroids.add(centroid);
                    });
        });

        return centroids;
    }

    private Cluster convert(Centroid centroid, DistanceMeasure measure) {
        Kluster cluster = new Kluster(centroid.getVector(), centroid.getIndex(), measure);
        cluster.calculateConvergence(0.5);

        logger.info("Cluster is converged: " + cluster.isConverged());

        return cluster;
    }


    private Iterator<Centroid> getCentroidsFromFile(Path centroidsSequenceFile) {
        SequenceFileIterable<Writable, Writable> centroidsIterator =
                new SequenceFileIterable<Writable, Writable>(centroidsSequenceFile, this.configuration);

        List<Centroid> centroidList = new ArrayList<Centroid>();

        try {
            centroidsIterator.forEach(pair -> {
                if (pair.getSecond() instanceof CentroidWritable) {
                    Centroid centroid = ((CentroidWritable) pair.getSecond()).getCentroid();
                    centroidList.add(centroid);
                }
            });
        } catch(IllegalStateException e) {
            logger.error(centroidsSequenceFile + " is not a SequenceFile.");
            return Collections.<Centroid>emptyIterator();
        }

        return centroidList.iterator();
    }
}
