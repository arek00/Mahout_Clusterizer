package com.arek00.clusterizer.ArticleUtils;


import com.arek00.webCrawler.Entities.Articles.IArticle;
import org.apache.hadoop.io.Text;

public class ArticleExtractor {

    public static Text extractTitle(IArticle article) {
        return new Text(article.getTitle());
    }

    public static Text extractContent(IArticle article) {
        return new Text(article.getContent());
    }

}
