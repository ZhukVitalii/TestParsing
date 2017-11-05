package com.gmail.zhukvitaliis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by VitaliiZhuk on 01.11.2017.
 */
public class Main {
    public static void main(String[] args) throws IOException, SocketTimeoutException {


        ParsingService parsingService = new ParsingService();

        //It is Block to parse by name
        try {
            parsingService.parseByName();
        }catch (SocketTimeoutException e){

        }


        //It is Block to parse category
        //try {
        /*
        * parsingService.parseAccessoiresFrauen();
        *parsingService.parseAccessoiresMaenner();
        *parsingService.parseBekleidungFrauen();
        *parsingService.parseBekleidungMaenner();
        *parsingService.parseSchuheFrauen();
        *parsingService.parseSchuheMaenner();
        */
        /*
        parsingService.parseBekleidungFrauen();
        } catch (SocketTimeoutException e){
            return;
        }
        */

        //Test block for all elements
        /*
        List<String> womanLinks = new ArrayList<String>();
        List<String> manLinks = new ArrayList<String>();
        List<String> kidsLinks = new ArrayList<String>();
        String url = "https://www.aboutyou.de";
        Document document = Jsoup.connect(url).get();
        List<String> linksGender = new ArrayList<String>();
        Elements elements = document.select("div[class = genderMenu_sf14ck] a");
        for (Element element : elements) {
            String a = element.attr("href");
            linksGender.add(a);
        }
        for (String s : linksGender) {
            System.out.println(s);
        }
       try {
           for (int i = 0; i <= linksGender.size(); i++) {
               String b = url + linksGender.get(i);
               System.out.println(b);
           }
       } catch (IndexOutOfBoundsException e){

       }

        String women = "https://www.aboutyou.de/frauen";
        String man = "https://www.aboutyou.de/maenner";
        String kids = "https://www.aboutyou.de/kinder";

        Document womens =  Jsoup.connect("https://www.aboutyou.de/frauen").get();
        Elements linkWomen = womens.select("div[class = container subWrapper_j428gh] ul li a");
        for (Element elementWoman : linkWomen) {
            String link = elementWoman.absUrl("href");
            String a = elementWoman.text();
            if (a.equals("Bekleidung") || a.equals("Wäsche") || a.equals("Schuhe") || a.equals("Accessoires")
                    || a.equals("SALE"))
            womanLinks.add(link);
        }
        for (String s : womanLinks) {
            System.out.println(s);
        }
        Document mens =  Jsoup.connect("https://www.aboutyou.de/maenner").get();
        Elements linkMen = mens.select("div[class = container subWrapper_j428gh] ul li a");
        for (Element elementMan : linkMen) {
            String link = elementMan.absUrl("href");
            String a = elementMan.text();
            if (a.equals("Bekleidung") || a.equals("Wäsche") || a.equals("Schuhe") || a.equals("Accessoires")
                    || a.equals("SALE"))
                manLinks.add(link);
        }

        for (String s : manLinks) {
            System.out.println(s);
        }

        Document kinder =  Jsoup.connect("https://www.aboutyou.de/kinder").get();
        Elements linkKids = kinder.select("div[class = container subWrapper_j428gh] ul li a");
        for (Element elementKids : linkKids) {
            String link = elementKids.absUrl("href");
            String a = elementKids.text();
            if (a.equals("Bekleidung") || a.equals("Wäsche") || a.equals("Schuhe") || a.equals("Accessoires")
                    || a.equals("SALE"))
                kidsLinks.add(link);
        }

        for (String s : kidsLinks) {
            System.out.println(s);
        }

*/





    }
}




