package com.arek00.clusterizer;


import com.arek00.clusterizer.clusterizers.KMeansClusterizer;
import com.arek00.clusterizer.clusterizers.KMeansParameters;

public class Main {

    public static void main(String[] args) {
        String articles = "/home/arek/articles/articles";
        String output = "/home/arek/clusterizer/canopyTest";

        KMeansClusterizer kmeans = new KMeansClusterizer();
        KMeansParameters parameters =
                new KMeansParameters.Builder()
                        .ngramSize(KMeansParameters.Builder.BIGRAM)
                        .kPointsNumber(50)
                        .iterationsNumber(20)
                        .useCanopy(true)
                        .canopyThresholds(2000, 250)
                        .build();

        try {
            kmeans.runClustering(articles, output, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
