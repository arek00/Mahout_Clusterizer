package com.arek00.clusterizer;


import com.arek00.clusterizer.Clustering.Clusterizers.KMeans.KMeansClusterizer;
import com.arek00.clusterizer.Clustering.Clusterizers.KMeans.KMeansParameters;

public class Main {

    public static void main(String[] args) {
        String articles = "/home/arek/articles/articles";
        String output = "/home/arek/clusterizer/CanopyTest2";

        KMeansClusterizer kmeans = new KMeansClusterizer();
        KMeansParameters parameters =
                new KMeansParameters.Builder()
                        .ngramSize(KMeansParameters.Builder.UNIGRAM)
                        .kPointsNumber(50)
                        .iterationsNumber(20)
                        .useCanopy(true)
                        .canopyThresholds(1000, 500)
                        .build();

        try {
            kmeans.runClustering(articles, output, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
