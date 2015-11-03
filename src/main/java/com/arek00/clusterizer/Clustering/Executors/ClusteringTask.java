package com.arek00.clusterizer.Clustering.Executors;


import com.arek00.clusterizer.Clustering.Clusterizers.Clusterizer;
import com.arek00.clusterizer.Utils.PathValidator;
import lombok.NonNull;
import lombok.Setter;
import org.apache.hadoop.fs.Path;

public class ClusteringTask implements Task {

    @Setter @NonNull
    private Clusterizer clusterizer;
    @Setter @NonNull
    private Path output;
    @Setter @NonNull
    private Path vectors;
    @Setter @NonNull
    private Path centroids;

    public ClusteringTask(@NonNull Clusterizer clusterizer, @NonNull Path vectors,
                          @NonNull Path centroids, @NonNull Path output) {
        this.clusterizer = clusterizer;
        this.output = output;
        this.vectors = vectors;
        this.centroids = centroids;
    }

    @Override
    public Path execute() throws Exception {
        PathValidator.removePathIfExists(output);
        clusterizer.runClustering(vectors, centroids, output);
        return output;
    }
}
