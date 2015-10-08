package com.arek00.clusterizer.Clustering.Clusterizers.KMeans;

import com.arek00.clusterizer.ArticleUtils.ArticlesDeserializer;
import com.arek00.clusterizer.Clustering.SequenceFile.SequenceFileWriter;
import com.arek00.clusterizer.Clustering.Tokenizers.StandardTokenizer;
import com.arek00.clusterizer.Clustering.Tokenizers.Tokenizer;
import com.arek00.webCrawler.Entities.Articles.Article;
import com.arek00.webCrawler.Entities.Articles.IArticle;
import com.arek00.webCrawler.Serializers.ISerializer;
import com.arek00.webCrawler.Serializers.XMLSerializer;
import lombok.NonNull;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.kmeans.RandomSeedGenerator;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
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

/**
 * Cluster directory of articles with KMeans algorithm
 */
public class KMeansClusterizer {

    private Path outputPath;
    private Path sequenceFilePath;
    private Path tokenizedDocumentsDirectory;
    private Path tfVectorPath;
    private Path tfidfPath;


    private FileSystem fileSystem;
    private Configuration configuration;


    public KMeansClusterizer() {
        try {
            initializeConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runClustering(@NonNull final String articlesPath,
                              @NonNull final String outputDirectory,
                              @NonNull KMeansParameters parameters) throws Exception {

        doSetPaths(outputDirectory);
        clearDirectory(outputDirectory);
        createSequenceFile(articlesPath);
        vectorizeDocuments(parameters);
        createClusters(parameters);
    }


    private void initializeConfiguration() throws IOException {
        this.configuration = new Configuration();
        this.fileSystem = FileSystem.get(configuration);
    }

    private void doSetPaths(final String outputDirectory) {
        this.outputPath = new Path(outputDirectory);
        sequenceFilePath = new Path(outputPath, "sequence");
        tfidfPath = new Path(outputPath, "tfidf");
        tokenizedDocumentsDirectory = new Path(outputPath, DocumentProcessor.TOKENIZED_DOCUMENT_OUTPUT_FOLDER);
        tfVectorPath = new Path(outputPath, DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER);
    }

    private void clearDirectory(String path) {
        File directory = new File(path);

        try {
            if (directory.exists()) {
                FileUtils.cleanDirectory(directory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creating sequenceFiles from articles
     *
     * @throws Exception
     */
    private void createSequenceFile(final String documentsDirectory) throws Exception {
        SequenceFileWriter writer = new SequenceFileWriter(this.configuration);

        Iterator<IArticle> articles = ArticlesDeserializer.fromDirectory(documentsDirectory);
        writer.writeToSequenceFile(getArticlesPairs(articles), sequenceFilePath);
    }

    private List<Pair<Text, Text>> getArticlesPairs(Iterator<IArticle> articlesIterator) throws IOException {
        List<Pair<Text, Text>> pairs = new ArrayList<>();

        articlesIterator.forEachRemaining(
                article -> {
                    Text key = new Text(article.getTitle());
                    Text value = new Text(article.getContent());
                    pairs.add(new Pair<>(key, value));
                });

        return pairs;
    }

    private void vectorizeDocuments(final KMeansParameters parameters) throws InterruptedException, IOException, ClassNotFoundException {
        tokenizeDocuments();
        Pair<Long[], List<Path>> documentFrequencies = createTFVectors(parameters.getNgramSize());
        createTFIDFVector(documentFrequencies);
    }

    private void tokenizeDocuments() throws InterruptedException, IOException, ClassNotFoundException {
        System.out.println("Tokenizing documents");

        Tokenizer tokenizer = new StandardTokenizer(this.configuration);
        tokenizer.tokenize(sequenceFilePath, tokenizedDocumentsDirectory);
    }

    private Pair<Long[], List<Path>> createTFVectors(int maxNGramSize) throws InterruptedException, IOException, ClassNotFoundException {
        System.out.println("Creating TF Vectors");

        String dictionaryVectorizerTFVectors = DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER;
        int minimumWordFrequency = 1;
        float minimumLLRValue = 0.0f;
        float normalizingPower = PartialVectorMerger.NO_NORMALIZING;
        int chunkSizeInMb = 100;

        DictionaryVectorizer.createTermFrequencyVectors(
                tokenizedDocumentsDirectory,
                outputPath,
                dictionaryVectorizerTFVectors,
                this.configuration,
                minimumWordFrequency,
                maxNGramSize,
                minimumLLRValue,
                normalizingPower,
                true, 1,
                chunkSizeInMb,
                false, false
        );


        return TFIDFConverter.calculateDF(
                tfVectorPath,
                tfidfPath,
                this.configuration, 100
        );
    }

    private void createTFIDFVector(Pair<Long[], List<Path>> documentFrequencies) throws InterruptedException, IOException, ClassNotFoundException {

        TFIDFConverter.processTfIdf(
                tfVectorPath,
                tfidfPath,
                this.configuration,
                documentFrequencies,
                1, 100,
                PartialVectorMerger.NO_NORMALIZING,
                false, false, false, 1
        );
    }


    private void createClusters(KMeansParameters parameters) throws InterruptedException, IOException, ClassNotFoundException {
        Path vectorsFolder = new Path(tfidfPath, "tfidf-vectors");
        Path canopyCentroids = new Path(outputPath, "canopy_centroids");
        Path randomCentroids = new Path(outputPath, "random_centroids");
        Path clusterOutput = new Path(outputPath, "clusters");


        Path centroids = (parameters.isCanopyClustering()) ?
                createCanopyCentroids(vectorsFolder, canopyCentroids, parameters.getCanopyT1(), parameters.getCanopyT2()) :
                createRandomCentroids(vectorsFolder, randomCentroids, parameters.getKPoints());

        KMeansDriver.run(
                vectorsFolder,
                centroids,
                clusterOutput,
                0.01f,
                parameters.getIterationsNumber(),
                true, 0, false
        );
    }

    private Path createCanopyCentroids(Path vectors, Path canopyCentroids, double t1, double t2) throws InterruptedException, IOException, ClassNotFoundException {
        System.out.println("Run Canopy");
        CanopyDriver.run(
                this.configuration,
                vectors, canopyCentroids,
                new EuclideanDistanceMeasure(),
                t1, t2,
                true, 0, true);

        return new Path(canopyCentroids, "clusters-0-final");
    }

    private Path createRandomCentroids(Path vectors, Path output, int pointsNumber) throws IOException {
        return RandomSeedGenerator.buildRandom(this.configuration,
                vectors, output, pointsNumber,
                new EuclideanDistanceMeasure());
    }


    private Iterator<File> getArticlesIterator(String articlesDirectory) {
        return FileUtils.iterateFiles(new File(articlesDirectory), new String[]{"xml"}, false);
    }

    private Iterator<IArticle> deserializeArticles(Iterator<File> serializedArticles) throws Exception {
        ISerializer serializer = new XMLSerializer();
        List<IArticle> articles = new ArrayList<>();

        while (serializedArticles.hasNext()) {
            File articleFile = serializedArticles.next();
            try {
                IArticle article = serializer.deserialize(Article.class, articleFile);
                articles.add(article);
            } catch (ValueRequiredException ignored) {
            }
        }

        System.out.println("Articles number: " + articles.size());
        return articles.iterator();
    }
}
