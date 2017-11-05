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
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * Created by VitaliiZhuk on 02.11.2017.
 */
public class ParsingService {


    //Method to parse Name of product
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

    //Method to parse Brand of product
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

    //Method to parse Price of product
    public static String parsePrice(Elements element){
        String price = element.select("span[class = finalPrice_klth9M]").text();
        return price;
    }

    //Method to parse Description of product
    public static String parseDescription(Elements element){
        String description = element.select("div[class = container_iv4rb4]").text();
        return description;
    }

    //Method to parse Article of product, article is part of Description
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

    //Return Document by  input url
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

    /*
    * Method to parse page. Input:
    * name of file for saving XML,
    * Url of page that wont to parse,
    * Document of page that wont to page
    * */
    public static void parsePage(String fileName,String url, Document documentToParse) throws IOException {

        //Create list with products
        List<Offer> offerList = new ArrayList<Offer>();

        //Create list with  links to product
        HashSet<String> linkList = new HashSet<String>();

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
            offerList.add(new Offer(name,brand,price,description,article));
        }

        //For testing
        System.out.println(offerList.size());

        //Create new object for correct saving to XML
        AllOffers allOffers = new AllOffers("Offers",offerList);

        //Method that save Offers to XML
        convertObjectToXml(allOffers,fileName);
    }

    // Method that search offer by keywords and does parsing
    public void parseByName() throws IOException {

        List<String> sportsLinks = new ArrayList<String>();

        List<String> allLinks = new ArrayList<String>();

        List<String> underLinkSports = new ArrayList<String>();

        //Block for input name of product
        System.out.println("Input name for search");
        Scanner scanner = new Scanner(System.in);
        String search = scanner.nextLine();

        String mainLink = "https://www.aboutyou.de";

        //Block for parsing products for woman
        try {
            Document documentFrauen = Jsoup.connect("https://www.aboutyou.de/frauen/").get();
            Elements elements = documentFrauen.select("div[class = container subWrapper_j428gh] li[class = categoryTreeItem item_5emc65] a");
            for (Element element : elements) {
                String a = element.attr("href");
                allLinks.add(mainLink.concat(a));
            }
        }catch (SocketTimeoutException e) {
        }

        //Block for parsing products for man
        try {
            Document documentMan = Jsoup.connect("https://www.aboutyou.de/maenner/").get();
            Elements elementsMan = documentMan.select("div[class = container subWrapper_j428gh] li[class = categoryTreeItem item_5emc65] a");
            for (Element element : elementsMan) {
                String a = element.attr("href");
                allLinks.add(mainLink.concat(a));
            }
        }catch (SocketTimeoutException e) {
        }

        //Block for parsing products for kids
        try {
            Document documentKinder = Jsoup.connect("https://www.aboutyou.de/kinder").get();
            Elements elementsKinder = documentKinder.select("div[class = sidebar-navigation js-sidebar-navigation-scroll-area] ul[class = list-unstyled js-category-tree-kids js-gender-tree-138113] li[class = category-item] a");
            for (Element element : elementsKinder) {
                String a = element.attr("href");
                allLinks.add(mainLink.concat(a));
            }
        }catch (SocketTimeoutException e){

        }

        //Sport parsing woman
        try {
            Document documentSport = Jsoup.connect("https://www.aboutyou.de/frauen/sport").get();
            Elements elementsSport = documentSport.select("div[class = sidebar-navigation js-sidebar-navigation-scroll-area] ul[class = list-unstyled] li[class = category-item] a");
            for (Element element : elementsSport) {
                String a = element.attr("href");
                sportsLinks.add(a);
            }
            for (String sportsLink : sportsLinks) {
                String urlSport = mainLink.concat(sportsLink);
                Document documentSportCategory = Jsoup.connect(urlSport).get();
                Elements elementsSportCategory = documentSportCategory.select("div[class = js-category-accordion-toggle-content is-collapsed] ul[class = list-unstyled] li[class = category-item] a");
                for (Element element : elementsSportCategory) {
                    String a = element.attr("href");
                    underLinkSports.add(a);
                }
            }
            for (String underLinkSport : underLinkSports) {
                String urlSport = mainLink.concat(underLinkSport);
                Document documentSportCategory = Jsoup.connect(urlSport).get();
                Elements elementsSportCategory = documentSportCategory.select("div[class = js-category-accordion-toggle-content is-collapsed] ul[class = list-unstyled] li[class = category-item] a");
                for (Element element : elementsSportCategory) {
                    String a = element.attr("href");
                    allLinks.add(mainLink.concat(a));
                }
            }
        }catch (SocketTimeoutException e){
        }


        //Sport parsing Man
        try {
            Document documentSport = Jsoup.connect("https://www.aboutyou.de/maenner/sport").get();
            Elements elementsSport = documentSport.select("div[class = sidebar-navigation js-sidebar-navigation-scroll-area] ul[class = list-unstyled] li[class = category-item] a");
            for (Element element : elementsSport) {
                String a = element.attr("href");
                sportsLinks.add(a);
            }
            for (String sportsLink : sportsLinks) {
                String urlSport = mainLink.concat(sportsLink);
                Document documentSportCategory = Jsoup.connect(urlSport).get();
                Elements elementsSportCategory = documentSportCategory.select("div[class = js-category-accordion-toggle-content is-collapsed] ul[class = list-unstyled] li[class = category-item] a");
                for (Element element : elementsSportCategory) {
                    String a = element.attr("href");
                    underLinkSports.add(a);
                }
            }
            for (String underLinkSport : underLinkSports) {
                String urlSport = mainLink.concat(underLinkSport);
                Document documentSportCategory = Jsoup.connect(urlSport).get();
                Elements elementsSportCategory = documentSportCategory.select("div[class = js-category-accordion-toggle-content is-collapsed] ul[class = list-unstyled] li[class = category-item] a");
                for (Element element : elementsSportCategory) {
                    String a = element.attr("href");
                    allLinks.add(mainLink.concat(a));
                }
            }
        }catch (SocketTimeoutException e){
        }


        for (String allLink : allLinks) {
            System.out.println(allLink);
        }
        try {

            for (String allLink : allLinks) {
                Document document = Jsoup.connect(allLink).get();
                parseSearchedElement(allLink,search,document);
            }
        } catch (NumberFormatException e){

        }


    }

    public void parseSearchedElement(String url,String search, Document documentToParse) throws IOException {

        List<String> names = new ArrayList<String>();

        HashSet<String> linkList = new HashSet<String>();

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
                }
            }
        }

        for (String s : linkList) {
            String searchedUrl=s;
            String fullUrl = mainUrl.concat(searchedUrl);
            parseOneElement(fullUrl);
        }
     }


    List<Offer> offerList = new ArrayList<Offer>();

    public void parseOneElement(String url) throws IOException {

        //For testing
        System.out.println("For parsing = " + url);

        String fileName = "Offers.xml";

        Document document = Jsoup.connect(url).get();
        Elements elements = document.select("div[class = content_1jug6qr]");
        String name = parseName(elements);String price = parsePrice(elements);
        String brand = parseBrand(elements);
        String description = parseDescription(elements);
        String article = parseArticle(elements);
        offerList.add(new Offer(name, brand, price, description, article));

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

    public void getAmount(){

        System.out.println("Amount of extracted products = " + offerList.size() );

    }

}
