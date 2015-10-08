package com.arek00.clusterizer.ArticleUtils;

import com.arek00.webCrawler.Entities.Articles.Article;
import com.arek00.webCrawler.Entities.Articles.IArticle;
import com.arek00.webCrawler.Serializers.ISerializer;
import com.arek00.webCrawler.Serializers.XMLSerializer;
import org.apache.commons.io.FileUtils;
import org.simpleframework.xml.core.ValueRequiredException;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArticlesDeserializer {

    public static Iterator<IArticle> fromDirectory(String directory) {

        List<IArticle> articles = new ArrayList<IArticle>();

        Iterator<File> documents = FileUtils.iterateFiles(new File(directory), new String[]{"xml"}, false);
        ISerializer serializer = new XMLSerializer();

        documents.forEachRemaining(documentFile -> {
            try {

                IArticle article = serializer.deserialize(Article.class, documentFile);
                articles.add(article);

            } catch (ValueRequiredException exception) {
                System.out.println("Article without content: " + documentFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return articles.iterator();
    }
}
