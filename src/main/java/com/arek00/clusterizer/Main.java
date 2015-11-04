package com.arek00.clusterizer;


import com.arek00.clusterizer.ArticleUtils.ArticleExtractor;
import com.arek00.clusterizer.ArticleUtils.ArticlesDeserializer;
import com.arek00.clusterizer.Clustering.Centroids.CanopyCentroids;
import com.arek00.clusterizer.Clustering.Centroids.CentroidsGenerator;
import com.arek00.clusterizer.Clustering.Centroids.KMeansPlusPlus.KMeansPlusPlusCentroids;
import com.arek00.clusterizer.Clustering.Centroids.KMeansPlusPlus.KMeansPlusPlusParameters;
import com.arek00.clusterizer.Clustering.Centroids.RandomSeedCentroids;
import com.arek00.clusterizer.Clustering.Centroids.StreamingKMeans.StreamingKMeansCentroids;
import com.arek00.clusterizer.Clustering.Clusterizers.Clusterizer;
import com.arek00.clusterizer.Clustering.Clusterizers.KMeans.KMeansClusterizer;
import com.arek00.clusterizer.Clustering.Clusterizers.KMeans.KMeansParameters;
import com.arek00.clusterizer.Clustering.Centroids.StreamingKMeans.StreamingKMeansParameters;
import com.arek00.clusterizer.Clustering.Executors.ClusteringTask;
import com.arek00.clusterizer.Clustering.Executors.Task;
import com.arek00.clusterizer.Clustering.Executors.VectoringTask;
import com.arek00.clusterizer.Clustering.Reducers.DimensionsReducer;
import com.arek00.clusterizer.Clustering.SequenceFile.SequenceFileWriter;
import com.arek00.clusterizer.Clustering.Tokenizers.StandardTokenizer;
import com.arek00.clusterizer.Clustering.Tokenizers.Tokenizer;
import com.arek00.clusterizer.Clustering.Vectorizers.TFIDFParameters;
import com.arek00.clusterizer.Clustering.Vectorizers.TFParameters;
import com.arek00.clusterizer.Clustering.Vectorizers.TFIDFVectorizer;
import com.arek00.clusterizer.Clustering.Vectorizers.TFVectorizer;
import com.arek00.webCrawler.Entities.Articles.IArticle;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.FileAlreadyExistsException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mahout.clustering.streaming.mapreduce.StreamingKMeansDriver;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.vectorizer.common.PartialVectorMerger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting application");

        Configuration configuration = new Configuration();

        Path articles = new Path("/home/arek/articles/interia_02_11");
        Path output = new Path("/home/arek/clusterizer/reducedDimensions");
        Path sequenceFile = new Path(output, "sequenceFile");

        KMeansParameters kMeansParameters = new KMeansParameters.Builder()
                .convergenceDelta(0.5f)
                .maxIteration(20)
                .build();


        KMeansPlusPlusParameters kMeansPPParameters =
                new KMeansPlusPlusParameters.Builder()
                .setClustersNumber(8)
                .setIterationLimit(200)
                .build();



        KMeansClusterizer clusterizer = new KMeansClusterizer(configuration);
        clusterizer.setParameters(kMeansParameters);
//
//        RandomSeedCentroids centroidsGenerator = new RandomSeedCentroids(configuration);
//        centroidsGenerator.setKPoints(20);
//        centroidsGenerator.setDistanceMeasure(new EuclideanDistanceMeasure());

//        CanopyCentroids centroidsGenerator = new CanopyCentroids(configuration);
//        centroidsGenerator.setCanopyThresholds(500, 100);
//        centroidsGenerator.setDistanceMeasure(new EuclideanDistanceMeasure());

        KMeansPlusPlusCentroids centroidsGenerator = new KMeansPlusPlusCentroids(configuration);
        centroidsGenerator.setParameters(kMeansPPParameters);

        DimensionsReducer reducer = new DimensionsReducer(configuration);

        List<Pair<Writable, Writable>> articlesPairs = getArticlesPairs(ArticlesDeserializer.fromDirectory(articles.toString()));
        SequenceFileWriter writer = new SequenceFileWriter(configuration);

        try {

            logger.info("Writing to articles to sequence file");

            writer.writeToSequenceFile(articlesPairs, sequenceFile, Text.class, Text.class);
            Task vectoringTask = new VectoringTask(sequenceFile, new Path(output,"vectors"), configuration);

            Path vectoringOutput = vectoringTask.execute();
            Path reducedVectors = reducer.runReduction(vectoringOutput, new Path(output, "reducedVectors"), 2);
            Path centroids = centroidsGenerator.generateCentroids(reducedVectors, new Path(output, "centroids"));
            Task clusteringTask = new ClusteringTask(clusterizer, reducedVectors, centroids, new Path(output, "clusters"));
            clusteringTask.execute();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static List<Pair<Writable, Writable>> getArticlesPairs(Iterator<IArticle> articleIterator) {
        List<Pair<Writable, Writable>> articlesList = new ArrayList<>();

        articleIterator.forEachRemaining(article -> {
            articlesList.add(new Pair<>(ArticleExtractor.extractTitle(article),
                    ArticleExtractor.extractContent(article)));
        });

        return articlesList;
    }

    private static void runClustering() {

    }

}
