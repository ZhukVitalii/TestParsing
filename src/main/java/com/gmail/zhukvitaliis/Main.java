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

        long startTime = System.currentTimeMillis();
        ParsingService parsingService = new ParsingService();

        //It is Block to parse by name
       try {
            parsingService.parseByName();
        }catch (SocketTimeoutException e){

        }
        parsingService.getAmount();



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

        long timeSpent = System.currentTimeMillis() - startTime;
        System.out.println("Run time = " + (timeSpent/1000)/60 + "min");


    }
}




