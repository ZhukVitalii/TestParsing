package com.gmail.zhukvitaliis;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VitaliiZhuk on 02.11.2017.
 */

@XmlType(propOrder = { "name","offer" }, name = "offers")
@XmlRootElement

public class AllOffers implements Serializable{
    private String name;
    private List<Offer> offer = new ArrayList<Offer>();

    public AllOffers() {
    }

    public AllOffers(String name, List<Offer> offer) {

        this.name = name;
        this.offer = offer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Offer> getOffer() {
        return offer;
    }

    public void setOffer(List<Offer> offer) {
        this.offer = offer;
    }
}
