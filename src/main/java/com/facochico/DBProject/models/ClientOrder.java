package com.facochico.DBProject.models;

import jakarta.persistence.*;

@Entity
public class ClientOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long clientId;
    private String category;
    private String brand;
    private String size;
    private String color;
    private String price;
    private String orderDate;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] orderPhoto;

    public ClientOrder(Long clientId, String category, String brand,String size, String color,
                       String price, String orderDate, String description, byte[] orderPhoto) {
        this.clientId = clientId;
        this.category = category;
        this.brand = brand;
        this.size = size;
        this.color = color;
        this.price = price;
        this.orderDate = orderDate;
        this.description = description;
        this.orderPhoto = orderPhoto;
    }

    public ClientOrder() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getOrderPhoto() {
        return orderPhoto;
    }

    public void setOrderPhoto(byte[] orderPhoto) {
        this.orderPhoto = orderPhoto;
    }
}