package com.arek00.clusterizer.Clustering.SequenceFile;


import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.common.Pair;

import java.io.IOException;
import java.util.List;

public class SequenceFileWriter {

    private SequenceFile.Writer writer;
    private Configuration configuration;
    private FileSystem fileSystem;

    public SequenceFileWriter(@NonNull Configuration configuration) {
        this.configuration = configuration;

        try {
            this.fileSystem = FileSystem.get(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write pairs of Text to sequence file in given path.
     *
     * @param pairs
     * @param sequenceFile
     * @throws IOException Throws when couldn't write to given path.
     */
    public void writeToSequenceFile(List<Pair<Writable, Writable>> pairs, Path sequenceFile, Class keyClass, Class valueClass)
            throws IOException {
        writer = new SequenceFile.Writer(fileSystem, configuration, sequenceFile, keyClass, valueClass);

        pairs.stream()
                .forEach(pair -> {

                    try {
                        writer.append(pair.getFirst(), pair.getSecond());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        writer.close();
    }
}
