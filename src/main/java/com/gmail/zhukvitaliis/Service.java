package com.gmail.zhukvitaliis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by VitaliiZhuk on 02.11.2017.
 */
public class Service {
    public String parseName(Elements element){
        String nameAndBrand = element.select("h1[class = productName_192josg]").text();
        String[] name = nameAndBrand.split("\\|");
        return name[1];
    }

    public String parseBrand(Elements element){
        String nameAndBrand = element.select("h1[class = productName_192josg]").text();
        String[] brand = nameAndBrand.split("\\|");
        return brand[0];
    }

    public String parsePrice(Elements element){
        String price = element.select("span[class = finalPrice_klth9M]").text();
        return price;
    }

    public String parseDescription(Elements element){
        String description = element.select("div[class = container_iv4rb4]").text();
        return description;
    }

    public String parseArticle(Elements element){
        String articleAndDescription = element.select("div[class = container_iv4rb4]").first().text();
        String[] article = articleAndDescription.split(":");
        return article[1];
    }
    public Document parcePage(String url) throws IOException {
        Document document = Jsoup.connect("https://www.aboutyou.de"+url).get();
        return document;
    }

}
