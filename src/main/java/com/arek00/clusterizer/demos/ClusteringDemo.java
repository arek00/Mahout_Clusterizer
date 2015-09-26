package com.arek00.clusterizer.demos;

import com.arek00.clusterizer.ArticleUtils.ArticleRetriever;
import com.arek00.webCrawler.Entities.Articles.Article;
import com.arek00.webCrawler.Entities.Articles.IArticle;
import com.arek00.webCrawler.Serializers.ISerializer;
import com.arek00.webCrawler.Serializers.XMLSerializer;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileIterable;
import org.apache.mahout.vectorizer.DictionaryVectorizer;
import org.apache.mahout.vectorizer.DocumentProcessor;
import org.apache.mahout.vectorizer.common.PartialVectorMerger;
import org.apache.mahout.vectorizer.tfidf.TFIDFConverter;
import org.simpleframework.xml.core.ValueRequiredException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClusteringDemo {
    public final String OUTPUT_PATH = "/home/arek/clusterizer/output/";
    public final String SEQUENCE_PATH = OUTPUT_PATH + "sequence";
    public final String TOKENIZED_PATH;
    public final String TF_VECTOR_PATH;
    public final String ARTICLES_PATH = "/home/arek/articles/articles/";
    public final String TFIDF_PATH = OUTPUT_PATH + "tfidf/";


    private FileSystem fileSystem;
    private Configuration configuration;


    public static void main(String[] args) {
        ClusteringDemo demo = new ClusteringDemo();
        /**
         * Demo application makes clusters of articles from given path.
         * App use Canopy algorithm.
         *
         * Steps doing by app to clustering articles:
         *
         * - Create a sequence of articles: map where key will be a title, and content will be a value of mapped pair
         * -
         *
         */


        try {
            demo.createSequenceFile();
            demo.vectorizeDocuments();
            demo.createClusters();
            demo.printSequenceFile(new Path(demo.OUTPUT_PATH + "canopy_centroids/clusteredPoints/part-m-0"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ClusteringDemo() {
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TOKENIZED_PATH = OUTPUT_PATH + DocumentProcessor.TOKENIZED_DOCUMENT_OUTPUT_FOLDER;
        TF_VECTOR_PATH = OUTPUT_PATH + DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER;
    }


    private void initialize() throws IOException {
        this.configuration = new Configuration();
        this.fileSystem = FileSystem.get(configuration);
    }

    /**
     * Creating sequenceFiles from articles
     *
     * @throws Exception
     */
    public void createSequenceFile() throws Exception {
        SequenceFile.Writer writer = new SequenceFile.Writer(this.fileSystem, this.configuration, new Path(SEQUENCE_PATH), Text.class, Text.class);
        Iterator<IArticle> articles = deserializeArticles(getArticlesIterator(ARTICLES_PATH));

        while (articles.hasNext()) {
            IArticle article = articles.next();
            Text title = ArticleRetriever.retrieveTitle(article);
            Text content = ArticleRetriever.retrieveContent(article);

            writer.append(title, content);
        }

        writer.close();
    }

    public void vectorizeDocuments() throws InterruptedException, IOException, ClassNotFoundException {
        DocumentProcessor.tokenizeDocuments(
                new Path(SEQUENCE_PATH),
                StandardAnalyzer.class,
                new Path(TOKENIZED_PATH),
                this.configuration
        );


        DictionaryVectorizer.createTermFrequencyVectors(
                new Path(TOKENIZED_PATH),
                new Path(OUTPUT_PATH),
                DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER,
                this.configuration,
                1, 1, 0.0f,
                PartialVectorMerger.NO_NORMALIZING,
                true, 1, 100, false, false
        );

        Pair<Long[], List<Path>> documentFrequencies = TFIDFConverter.calculateDF(
                new Path(TF_VECTOR_PATH),
                new Path(TFIDF_PATH),
                this.configuration, 100
        );

        TFIDFConverter.processTfIdf(
                new Path(TF_VECTOR_PATH),
                new Path(TFIDF_PATH),
                this.configuration,
                documentFrequencies,
                1, 100,
                PartialVectorMerger.NO_NORMALIZING,
                false, false, false, 1
        );
    }


    public void createClusters() throws InterruptedException, IOException, ClassNotFoundException {
        String vectorsFolder = TFIDF_PATH + "tfidf-vectors/";
        String canopyCentroids = OUTPUT_PATH + "canopy_centroids";
        String clusterOutput = OUTPUT_PATH + "clusters";

        CanopyDriver.run(
                this.configuration,
                new Path(vectorsFolder),
                new Path(canopyCentroids),
                new EuclideanDistanceMeasure(),
                20, 5, true, 0, true
        );
    }

    public void printSequenceFile(Path path) {
        SequenceFileIterable<Writable, Writable> iterable =
                new SequenceFileIterable<Writable, Writable>(path, this.configuration);

        for(Pair<Writable, Writable> pair : iterable) {
            System.out.format("%10s -> %s\n", pair.getFirst(), pair.getSecond());
        }

    }

    private Iterator<File> getArticlesIterator(String articlesDirectory) {
        return FileUtils.iterateFiles(new File(articlesDirectory), new String[]{"xml"}, false);
    }

    private Iterator<IArticle> deserializeArticles(Iterator<File> serializedArticles) throws Exception {
        ISerializer serializer = new XMLSerializer();
        List<IArticle> articles = new ArrayList<IArticle>();

        while (serializedArticles.hasNext()) {
            File articleFile = serializedArticles.next();
            try {
                IArticle article = serializer.deserialize(Article.class, articleFile);
                articles.add(article);
            } catch (ValueRequiredException exception) {
                continue;
            }
        }

        System.out.println("Articles number: " + articles.size());
        return articles.iterator();
    }


}
