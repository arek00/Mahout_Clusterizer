package com.arek00.clusterizer.demos.display.SequenceFileUtils;

import com.arek00.clusterizer.demos.display.DistanceDisplay.Points.ClusterCenter;
import com.arek00.clusterizer.demos.display.DistanceDisplay.Points.ClusteredPoint;
import com.arek00.clusterizer.demos.display.DistanceDisplay.Points.DisplayedPointEntity;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileIterable;

import java.util.ArrayList;
import java.util.List;

public class SequenceFileExtractor {

    private static final Logger logger = LogManager.getLogger(SequenceFileExtractor.class);
    private static Configuration configuration = new Configuration();

    public static List<DisplayedPointEntity> getDataFromSequenceFile(Path sequenceFile) {
        List<DisplayedPointEntity> data = new ArrayList<DisplayedPointEntity>();
        SequenceFileIterable<Writable, Writable> iterable = getFileIterable(sequenceFile);

        iterable.spliterator()
                .forEachRemaining(pair -> {
                    addElementToList(pair, data);
                });

        String infoMessage = String.format("Added %d elements of %s type", data.size(), data.get(0).getClass().getName());
        logger.info(infoMessage);

        return data;
    }

    private static void addElementToList(Pair<Writable, Writable> pair, List<DisplayedPointEntity> list) {
        if(pair.getSecond() instanceof ClusterWritable) {
            list.add(new ClusterCenter((ClusterWritable)pair.getSecond()));
        }
        else {
            list.add(new ClusteredPoint(pair));
        }
    }

    private static SequenceFileIterable<Writable, Writable> getFileIterable(Path sequenceFile) {
        SequenceFileIterable<Writable, Writable> iterable =
                new SequenceFileIterable<Writable, Writable>(sequenceFile, configuration);

        return iterable;
    }


}
