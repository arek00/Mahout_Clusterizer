package com.arek00.clusterizer;


import com.arek00.clusterizer.ArticleUtils.ArticleExtractor;
import com.arek00.clusterizer.ArticleUtils.ArticlesDeserializer;
import com.arek00.clusterizer.Clustering.Centroids.CanopyCentroids;
import com.arek00.clusterizer.Clustering.Clusterizers.KMeans.KMeansClusterizer;
import com.arek00.clusterizer.Clustering.Clusterizers.KMeans.KMeansParameters;
import com.arek00.clusterizer.Clustering.Clusterizers.StreamingKMeans.StreamingKMeansClusterizer;
import com.arek00.clusterizer.Clustering.SequenceFile.SequenceFileWriter;
import com.arek00.clusterizer.Clustering.Tokenizers.StandardTokenizer;
import com.arek00.clusterizer.Clustering.Tokenizers.Tokenizer;
import com.arek00.clusterizer.Clustering.Vectorizers.TFIDFParameters;
import com.arek00.clusterizer.Clustering.Vectorizers.TFParameters;
import com.arek00.clusterizer.Clustering.Vectorizers.TFIDFVectorizer;
import com.arek00.clusterizer.Clustering.Vectorizers.TFVectorizer;
import com.arek00.webCrawler.Entities.Articles.IArticle;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileAlreadyExistsException;
import org.apache.mahout.common.Pair;
import org.apache.mahout.vectorizer.common.PartialVectorMerger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class Main {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();

        Path articles = new Path("/home/arek/articles/yahooArticles");
        Path output = new Path("/home/arek/clusterizer/yahooArticlesTest");
        Path sequenceFile = new Path(output, "sequenceFile");
        Path tokenizedDirectory = new Path(output, "tokenizedFiles");
        Path tfVectorsDirectory = new Path(output, "tfVectors");
        Path tfidfVectorsDirectory = new Path(output, "tfidfVectors");
        Path centroidsDirectory = new Path(output, "centroids");
        Path kmeansDirectory = new Path(output, "kmeans");

        TFParameters tfParameters = new TFParameters.Builder()
                .maxNGramSize(1)
                .minimumLLRValue(0.1f)
                .minimumWordFrequency(1)
                .normalizingPower(PartialVectorMerger.NO_NORMALIZING)
                .build();

        TFIDFParameters tfidfParameters = new TFIDFParameters.Builder()
                .build();

        KMeansParameters kMeansParameters = new KMeansParameters.Builder()
                .convergenceDelta(0.5f)
                .maxIteration(300)
                .build();

        List<Pair<Text, Text>> articlesPairs = getArticlesPairs(ArticlesDeserializer.fromDirectory(articles.toString()));

        SequenceFileWriter writer = new SequenceFileWriter(configuration);
        KMeansClusterizer kmeans = new KMeansClusterizer(configuration);

        try {
            writer.writeToSequenceFile(articlesPairs, sequenceFile);
            Tokenizer tokenizer = new StandardTokenizer(configuration);
            Path tokenizedDocuments = tokenizer.tokenize(sequenceFile, tokenizedDirectory);

            TFVectorizer tfVectorizer = new TFVectorizer(configuration);
            Path tfVectors = tfVectorizer.createVectors(tokenizedDocuments, tfVectorsDirectory, tfParameters, 100);

            TFIDFVectorizer vectorizer = new TFIDFVectorizer(configuration);
            Path tfidfVectors = vectorizer.vectorize(tfVectors, tfidfVectorsDirectory, tfidfParameters, 100);

            CanopyCentroids centroids = new CanopyCentroids(configuration);
            centroids.setCanopyThresholds(500, 100);
            Path generatedCentroids = centroids.generateCentroids(tfidfVectors, centroidsDirectory);

//            StreamingKMeansClusterizer clusterizer = new StreamingKMeansClusterizer(configuration);
//            clusterizer.runClustering(tfidfVectors, new Path(output, "streamingKMeans"));


            try {
                kmeans.runClustering(tfidfVectors, generatedCentroids, kmeansDirectory, kMeansParameters);
            } catch (FileAlreadyExistsException e) {
                FileUtils.cleanDirectory(new File(kmeansDirectory.toString()));
                kmeans.runClustering(tfidfVectors, generatedCentroids, kmeansDirectory, kMeansParameters);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


    private static List<Pair<Text, Text>> getArticlesPairs(Iterator<IArticle> articleIterator) {
        List<Pair<Text, Text>> articlesList = new ArrayList<>();

        articleIterator.forEachRemaining(article -> {
            articlesList.add(new Pair<Text, Text>(ArticleExtractor.extractTitle(article),
                    ArticleExtractor.extractContent(article)));
        });

        return articlesList;
    }

}
