package com.gmail.zhukvitaliis;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by VitaliiZhuk on 02.11.2017.
 */
// определяем корневой элемент
@XmlRootElement(name = "Offer")
// определяем последовательность тегов в XML
@XmlType(propOrder = {"name", "brand", "price","description","article"})
public class Offer {
    private String name;
    private String brand;
    private String price;
    private String description;
    private String article;

    public Offer() {
    }

    public Offer(String name, String brand, String price, String description, String article) {

        this.name = name;
        this.brand = brand;
        this.price = price;
        this.description = description;
        this.article = article;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", article='" + article + '\'' +
                '}';
    }
}
