package com.arek00.clusterizer.demos;

import com.arek00.clusterizer.ArticleUtils.ArticleExtractor;
import com.arek00.clusterizer.ArticleUtils.ArticlesDeserializer;
import com.arek00.webCrawler.Entities.Articles.IArticle;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.vectorizer.DictionaryVectorizer;

import java.io.IOException;
import java.util.List;

public class KMeansClusteringDemo {

    private final static String DOCUMENTS_PARAM = "-d";
    private final static String OUTPUT_PARAM = "-o";

    public static void main(String[] args) {
        if (args.length < 4) {
            showHelpText();
        } else {

            String documentsDirectory = "", outputDirectory = "";
            int iteration = 0;
            while (iteration < args.length) {
                if (args[iteration].equals(DOCUMENTS_PARAM)) {
                    documentsDirectory = args[iteration + 1];
                }
                if (args[iteration].equals(OUTPUT_PARAM)) {
                    outputDirectory = args[iteration + 1];
                }
            }

            Path documentsPath = new Path(documentsDirectory);
            Path outputPath = new Path(outputDirectory);

            KMeansClusteringDemo kmeans = new KMeansClusteringDemo(documentsPath, outputPath);
            //kmeans.runClustering();
        }
    }

    private static void showHelpText() {
        System.out.println("Mandatory parameters:");
        System.out.println("-d [documents directory]");
        System.out.println("-o [output directory]");
    }


    private Configuration configuration;
    private Path documentsPath;
    private Path output;
    private FileSystem fileSystem;

    private Path sequenceFile;

    public KMeansClusteringDemo(Path documentsPath, Path output) {
        this.documentsPath = documentsPath;
        this.output = output;
        this.configuration = new Configuration();

        try {
            this.fileSystem = FileSystem.get(this.configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }

        initializePaths();
    }

    private void initializePaths() {
        this.sequenceFile = new Path(output, "sequenceFile");
    }

    private void createSequenceFile() throws IOException {
        SequenceFile.Writer writer =
                new SequenceFile.Writer(this.fileSystem, this.configuration,
                        this.sequenceFile, Text.class, Text.class);


        //List<IArticle> articles = ArticlesDeserializer.fromDirectory(documentsPath.toString());

//        articles.stream().
//                forEach(article -> {
//                    try {
//                        writer.append(ArticleExtractor.extractTitle(article), ArticleExtractor.extractContent(article));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
    }


    public void runClustering() throws IOException {
        System.out.println("Creating sequence file");
        createSequenceFile();
        System.out.println("Created sequence file at: " + sequenceFile.getName());


        //KMeansDriver.run(this.configuration, );

    }


}
