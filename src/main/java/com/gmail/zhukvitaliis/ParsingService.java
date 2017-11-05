package com.gmail.zhukvitaliis;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by VitaliiZhuk on 02.11.2017.
 */
public class ParsingService {


    //method to parse Name of product
    public static String parseName(Elements element) throws IOException{
        try {
            String nameAndBrand = element.select("h1[class = productName_192josg]").text();
            String[] name = nameAndBrand.split("\\|");
            return name[1];
        } catch (IndexOutOfBoundsException e) {
            return "error";
        }
        catch (NullPointerException e) {
            return "error";
        }
    }
    //method to parse Brand of product
    public static String parseBrand(Elements element) throws IOException{
        try {
        String nameAndBrand = element.select("h1[class = productName_192josg]").text();
        String[] brand = nameAndBrand.split("\\|");
        return brand[0];
        } catch (IndexOutOfBoundsException e) {
            return "error";
        }
        catch (NullPointerException e) {
            return "error";
        }

    }
    //method to parse Price of product
    public static String parsePrice(Elements element){
        String price = element.select("span[class = finalPrice_klth9M]").text();
        return price;
    }
    //method to parse Description of product
    public static String parseDescription(Elements element){
        String description = element.select("div[class = container_iv4rb4]").text();
        return description;
    }
    //method to parse Article of product, article is part of Description
    public static String parseArticle(Elements element) throws IOException{
        try {
            String articleAndDescription = element.select("div[class = container_iv4rb4]").first().text();
            String[] article = articleAndDescription.split(":");
            return article[1];
        } catch (IndexOutOfBoundsException e)  {
            return "error";
        }
        catch (NullPointerException e) {
            return "error";
        }
    }

    public static Document parcePage(String url) throws IOException {
        Document document = Jsoup.connect("https://www.aboutyou.de"+url).get();
        return document;
    }
    // Method that returns number of page with products
    public static int getPagesNumber(Document document) {
        int count = 0;
        Elements counterPage = document.select("div[class = product-pager]");

        for (Element element : counterPage) {
            String counter = element.select("li[class = gt9]").text();
            count = Integer.parseInt(counter);
        }
        return count;
    }
    //Method to parse page. Input:
    // name of file for saving XML,
    // Url of page that wont to parse,
    // Document of page that wont to page
    public static void parsePage(String fileName,String url, Document documentToParse) throws IOException {
        //Create list with products
        List<Offer> offerList = new ArrayList<Offer>();
        //Create list with  links to product
        List<String> linkList = new ArrayList<String>();
        //Create list with links to each product
        for (int i = 1; i <= getPagesNumber(documentToParse); i++) {
            Document document = Jsoup.connect(url + "?page="+i).get();
            Elements links = document.select("a[class = product-name-link]");
            for (Element link : links) {
                String linkOne = link.attr("href");
                linkList.add(linkOne);
            }
        }
        //Select from list links and do parsing
        for (String s : linkList) {
            Elements elements = parcePage(s).select("div[class = content_1jug6qr]");
            String name = parseName(elements);
            String price = parsePrice(elements);
            String brand = parseBrand(elements);
            String description = parseDescription(elements);
            String article = parseArticle(elements);
            //add product to offerList
            offerList.add(new Offer(name,brand,price,description,article));
        }


        System.out.println(offerList.size());
        //Create new object for correct saving to XML
        AllOffers allOffers = new AllOffers("Offers",offerList);
        //Method that save Offers to XML
        convertObjectToXml(allOffers,fileName);
    }

    public void parseByName() throws IOException {
        List<String> allLinks = new ArrayList<String>();
        String mainLink = "https://www.aboutyou.de";

        Document documentFrauen = Jsoup.connect("https://www.aboutyou.de/frauen").get();
        Elements elements = documentFrauen.select("div[class = container subWrapper_j428gh] li[class = categoryTreeItem item_5emc65] a");
        for (Element element : elements) {
            String a = element.attr("href");
            allLinks.add(mainLink.concat(a));
        }

        Document documentMan = Jsoup.connect("https://www.aboutyou.de/maenner").get();
        Elements elementsMan = documentMan.select("div[class = container subWrapper_j428gh] li[class = categoryTreeItem item_5emc65] a");
        for (Element element : elementsMan) {
            String a = element.attr("href");
            allLinks.add(mainLink.concat(a));
        }

        Document documentKids = Jsoup.connect("https://www.aboutyou.de/kinder").get();
        Elements elementsKids = documentKids.select("div[class = container subWrapper_j428gh] li[class = categoryTreeItem item_5emc65] a");
        for (Element element : elementsKids) {
            String a = element.attr("href");
            allLinks.add(mainLink.concat(a));
        }

        try {
            System.out.println("Input name for search");
            Scanner scanner = new Scanner(System.in);
            String search = scanner.nextLine();
            for (String allLink : allLinks) {
                Document document = Jsoup.connect(allLink).get();
                parseSearchedElement(allLink,search,document);
            }
        } catch (NumberFormatException e){

        }
    }

    public void parseSearchedElement(String url,String search, Document documentToParse) throws IOException {

        List<Offer> offerList = new ArrayList<Offer>();
        List<String> names = new ArrayList<String>();
        List<String> linkList = new ArrayList<String>();
        String searchedUrl = "";
        String mainUrl = "https://www.aboutyou.de";

        for (int i = 1; i <= getPagesNumber(documentToParse); i++) {
            Document document = Jsoup.connect(url + "?page=" + i).get();
            Elements elements = document.select("a[class = product-name-link]");
            for (Element element : elements) {
                String name = element.text();
                if (name.contains(search)) {
                    String link = element.attr("href");
                    linkList.add(link);
                    names.add(name);
                    //searchedUrl = link;
                }
            }
        }

        for (String s : linkList) {
            System.out.println(s);
            searchedUrl=s;
            String fullUrl = mainUrl.concat(searchedUrl);
            parseOneElement(fullUrl);
        }
        //System.out.println(searchedUrl);
       // System.out.println(fullUrl);
        //parseOneElement(fullUrl);
     }



public  static List<Offer> offerList = new ArrayList<Offer>();
    public static void parseOneElement(String url) throws IOException {
        //Create list with products
        System.out.println("For parsing = " + url);
        //Create list with  links to product
        List<String> linkList = new ArrayList<String>();
        //Create list with links to each product
        String fileName = "Offers.xml";
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select("div[class = content_1jug6qr]");
        String name = parseName(elements);String price = parsePrice(elements);
        String brand = parseBrand(elements);
        String description = parseDescription(elements);
        String article = parseArticle(elements);
        //add product to offerList
        offerList.add(new Offer(name, brand, price, description, article));

        System.out.println(offerList.size());
        //Create new object for correct saving to XML

        AllOffers allOffers = new AllOffers("Offers",offerList);
        //Method that save Offers to XML

        convertObjectToXml(allOffers,fileName);
    }

    // Block of methods that parse each category of products

    public void parseWaescheFrauen() throws IOException {
        String fileName = "waesche.xml";
        String waescheFrauen = "https://www.aboutyou.de/frauen/bekleidung/waesche";
        Document documentWaescheFrauen = Jsoup.connect(waescheFrauen).get();
        parsePage(fileName,waescheFrauen,documentWaescheFrauen);
    }

    public void parseBekleidungFrauen() throws IOException {
        String fileName = "bekleidung.xml";
        String bekleidungFrauen = "https://www.aboutyou.de/frauen/bekleidung";
        Document documentBekleidungFrauen = Jsoup.connect(bekleidungFrauen).get();
        parsePage(fileName,bekleidungFrauen,documentBekleidungFrauen);
    }

    public void parseSchuheFrauen() throws IOException {
        String fileName = "schuhe.xml";
        String schuheFrauen = "https://www.aboutyou.de/frauen/schuhe";
        Document documentSchuheFrauen = Jsoup.connect(schuheFrauen).get();
        parsePage(fileName,schuheFrauen,documentSchuheFrauen);
    }

    public void parseAccessoiresFrauen() throws IOException {
        String fileName = "accessoires.xml";
        String accessoiresFrauen = "https://www.aboutyou.de/frauen/accessoires";
        Document documentAccessoiresFrauen = Jsoup.connect(accessoiresFrauen).get();
        parsePage(fileName,accessoiresFrauen,documentAccessoiresFrauen);
    }

    public void parseBekleidungMaenner() throws IOException {
        String fileName = "bekleidungMaenner.xml";
        String bekleidungFrauen = "https://www.aboutyou.de/maenner/bekleidung";
        Document documentBekleidungFrauen = Jsoup.connect(bekleidungFrauen).get();
        parsePage(fileName,bekleidungFrauen,documentBekleidungFrauen);
    }

    public void parseSchuheMaenner() throws IOException {
        String fileName = "schuheMaenner.xml";
        String schuheMaenner = "https://www.aboutyou.de/maenner/schuhe";
        Document documentSchuheMaenner = Jsoup.connect(schuheMaenner).get();
        parsePage(fileName,schuheMaenner,documentSchuheMaenner);
    }

    public void parseAccessoiresMaenner() throws IOException {
        String fileName = "accessoiresMaenner.xml";
        String accessoiresMaenner = "https://www.aboutyou.de/maenner/accessoires";
        Document documentAccessoiresMaenner = Jsoup.connect(accessoiresMaenner).get();
        parsePage(fileName,accessoiresMaenner,documentAccessoiresMaenner);
    }

    public void parseKinderJungen() throws IOException {
        String fileName = "kinderJunger.xml";
        String kinderJungen = "https://www.aboutyou.de/kinder/jungen";
        Document documentKinderJungen = Jsoup.connect(kinderJungen).get();
        parsePage(fileName,kinderJungen,documentKinderJungen);
    }

    public void parseKinderMaedchen() throws IOException {
        String fileName = "kinderMaedchen.xml";
        String kinderMaedchen = "https://www.aboutyou.de/kinder/maedchen";
        Document documentKinderMaedchen = Jsoup.connect(kinderMaedchen).get();
        parsePage(fileName,kinderMaedchen,documentKinderMaedchen);
    }



    //Method for saving result in XML-file

    public static void convertObjectToXml (AllOffers allOffers, String filePath){

        try {
            JAXBContext context = JAXBContext.newInstance(AllOffers.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Marshalling file to XML
            marshaller.marshal(allOffers, new File(filePath));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
