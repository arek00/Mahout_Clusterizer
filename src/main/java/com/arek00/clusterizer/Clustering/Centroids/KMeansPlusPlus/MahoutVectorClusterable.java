package com.arek00.clusterizer.Clustering.Centroids.KMeansPlusPlus;

import com.arek00.clusterizer.Utils.MahoutVectorConverter;
import lombok.NonNull;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.mahout.math.Vector;


public class MahoutVectorClusterable implements Clusterable {

    private double[] points;

    public MahoutVectorClusterable(@NonNull Vector vector) {
        initializePointsArray(vector);
    }


    private void initializePointsArray(@NonNull Vector vector) {
        int vectorSize = vector.size();
        this.points = MahoutVectorConverter.vectorToArray(vector);
    }

    @Override public double[] getPoint() {
        return points;
    }
}
