package com.gmail.zhukvitaliis;

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

/**
 * Created by VitaliiZhuk on 01.11.2017.
 */
public class Main implements Runnable {
    public static void main(String[] args) throws IOException {

        List<Offer> offerList = new ArrayList<Offer>();
        List<String> linkList = new ArrayList<String>();
        Service service = new Service();

        for (int i = 1; i <= 2; i++) {
            Document document = Jsoup.connect("https://www.aboutyou.de/frauen/schuhe?page="+i).get();
            Elements links = document.select("a[class = product-name-link]");
            for (Element link : links) {
            String linkOne = link.attr("href");
            linkList.add(linkOne);
            }
        }
            String fileName = "E:/offers.xml";


                for (String s : linkList) {
                    Elements elements = service.parcePage(s).select("div[class = content_1jug6qr]");
                    String name = service.parseName(elements);
                    String price = service.parsePrice(elements);
                    String brand = service.parseBrand(elements);
                    String description = service.parseDescription(elements);
                    String article = service.parseArticle(elements);
                    offerList.add(new Offer(name,brand,price,description,article));
                }


        System.out.println(offerList.size());
        AllOffers allOffers = new AllOffers("Offers",offerList);

        convertObjectToXml(allOffers,fileName);
        }

            // System.out.println(offerList);


        private static void convertObjectToXml (AllOffers allOffers, String filePath){


        try {
                JAXBContext context = JAXBContext.newInstance(AllOffers.class);
                Marshaller marshaller = context.createMarshaller();
                // устанавливаем флаг для читабельного вывода XML в JAXB
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                // маршаллинг объекта в файл
                marshaller.marshal(allOffers, new File(filePath));
            } catch (JAXBException e) {
                e.printStackTrace();
            }
    }

        public void run () {

        }

        }




