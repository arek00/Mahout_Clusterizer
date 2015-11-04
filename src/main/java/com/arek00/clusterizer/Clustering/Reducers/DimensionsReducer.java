package com.arek00.clusterizer.Clustering.Reducers;

import com.arek00.clusterizer.Algorithms.MatrixUtils.MatrixConverter;
import com.arek00.clusterizer.Algorithms.MultidimensionalScaling.MultiDimensionalScalingEstimator;
import com.arek00.clusterizer.Clustering.SequenceFile.SequenceFileWriter;
import com.arek00.clusterizer.Utils.MahoutVectorConverter;
import com.google.common.collect.Lists;
import lombok.NonNull;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirIterable;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirValueIterable;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class DimensionsReducer {

    private Configuration configuration;

    public DimensionsReducer(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }


    public Path runReduction(Path vectorsDirectory, Path outputDirectory, int dimensions) throws IOException {
        List<Pair<Writable, Writable>> pairs = readFromSequenceFiles(vectorsDirectory);

        List<Vector> vectors =
                pairs.stream()
                        .map(pair -> ((VectorWritable) pair.getSecond()).get())
                        .collect(Collectors.toList());

        Vector[] array = vectors.toArray(new Vector[vectors.size()]);
        array = reduceTo(dimensions, array);

        for (int index = 0; index < pairs.size(); index++) {
            VectorWritable vectorWritable = ((VectorWritable) pairs.get(index).getSecond());
            vectorWritable.set(array[index]);
        }

        SequenceFileWriter writer = new SequenceFileWriter(configuration);

        Path reducedDirectory = new Path(outputDirectory, "reducedVectors");
        writer.writeToSequenceFile(pairs, new Path(reducedDirectory, "part-sequenceFile"), Text.class, VectorWritable.class);

        return reducedDirectory;
    }

    private List<Pair<Writable, Writable>> readFromSequenceFiles(Path inputDirectory) {
        SequenceFileDirIterable<Text, VectorWritable> iterable =
                new SequenceFileDirIterable<Text, VectorWritable>(inputDirectory, PathType.LIST,
                        PathFilters.partFilter(), configuration);

        List<Pair<Writable, Writable>> pairs = Lists.newArrayList();

        iterable.forEach(pair -> {
            pairs.add(new Pair<Writable, Writable>(pair.getFirst(), pair.getSecond()));
        });

        return pairs;
    }

    private Vector[] getVectorsArray(List<Pair<Writable, Writable>> pairs) {
        List<Vector> vectors =
                pairs.stream()
                        .map(pair -> ((VectorWritable) pair.getSecond()).get())
                        .collect(Collectors.toList());

        return vectors.toArray(new Vector[vectors.size()]);
    }


    public static Vector[] reduceTo(int dimensions, Vector[] points) {
        MultiDimensionalScalingEstimator mdsEstimator = new MultiDimensionalScalingEstimator(dimensions);

        RealMatrix reducesVectors = mdsEstimator.MDSEstimation(points);
        return realMatrixToVector(reducesVectors);
    }

    private static Vector[] realMatrixToVector(RealMatrix matrix) {
        Vector[] vectors = new Vector[matrix.getRowDimension()];

        for (int index = 0; index < matrix.getRowDimension(); index++) {
            vectors[index] = MahoutVectorConverter.getVector(matrix.getRow(index));
        }
        return vectors;
    }
}
