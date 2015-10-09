package com.arek00.clusterizer;


import com.arek00.clusterizer.ArticleUtils.ArticleExtractor;
import com.arek00.clusterizer.ArticleUtils.ArticlesDeserializer;
import com.arek00.clusterizer.Clustering.Clusterizers.KMeans.KMeansClusterizer;
import com.arek00.clusterizer.Clustering.Clusterizers.KMeans.KMeansParameters;
import com.arek00.clusterizer.Clustering.SequenceFile.SequenceFileWriter;
import com.arek00.clusterizer.Clustering.Tokenizers.StandardTokenizer;
import com.arek00.clusterizer.Clustering.Tokenizers.Tokenizer;
import com.arek00.clusterizer.Clustering.Vectorizers.TFIDFVectorizer;
import com.arek00.webCrawler.Entities.Articles.IArticle;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.mahout.common.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();

        Path articles = new Path("/home/arek/articles/articles");
        Path output = new Path("/home/arek/clusterizer/CanopyTest2");
        Path sequenceFile = new Path(output, "sequenceFile");
        Path tokenizedDirectory = new Path(output, "tokenizedFiles");
        Path vectorsDirectory = new Path(output, "vectors");

        List<Pair<Text, Text>> articlesPairs = getArticlesPairs(ArticlesDeserializer.fromDirectory(articles));

        SequenceFileWriter writer = new SequenceFileWriter(configuration);

        try {
            writer.writeToSequenceFile(articlesPairs, sequenceFile);
            Tokenizer tokenizer = new StandardTokenizer(configuration);
            tokenizer.tokenize(sequenceFile, tokenizedDirectory);

            TFIDFVectorizer vectorizer = new TFIDFVectorizer(configuration);
            vectorizer.vectorize(tokenizedDirectory, vectorsDirectory, );


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        KMeansClusterizer kmeans = new KMeansClusterizer();


    }


    private static List<Pair<Text, Text>> getArticlesPairs(Iterator<IArticle> articleIterator) {
        List<Pair<Text, Text>> articlesList = new ArrayList<>();

        articleIterator.forEachRemaining(article -> {
            articlesList.add(new Pair(ArticleExtractor.extractTitle(article),
                    ArticleExtractor.extractContent(article)));
        });


    }

}
