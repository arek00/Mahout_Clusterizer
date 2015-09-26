package com.arek00.clusterizer.ArticleUtils;


import com.arek00.webCrawler.Entities.Articles.IArticle;
import org.apache.hadoop.io.Text;

public class ArticleRetriever {

    public static Text retrieveTitle(IArticle article) {
        return new Text(article.getTitle());
    }

    public static Text retrieveContent(IArticle article) {
        return new Text(article.getContent());
    }

}
