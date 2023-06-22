//package com.facochico.DBProject.models;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//
//@Entity
//public class Client {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private long id;
//
//    private String name;
//    private String patronymic;
//    private String surname;
//    private String phoneNumber;
//    private String BDay;
//
//    public Client(String name, String patronymic, String surname, String phoneNumber, String BDay) {
//        this.name = name;
//        this.patronymic = patronymic;
//        this.surname = surname;
//        this.phoneNumber = phoneNumber;
//        this.BDay = BDay;
//    }
//
//    public Client() {}
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPatronymic() {
//        return patronymic;
//    }
//
//    public void setPatronymic(String patronymic) {
//        this.patronymic = patronymic;
//    }
//
//    public String getSurname() {
//        return surname;
//    }
//
//    public void setSurname(String surname) {
//        this.surname = surname;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public String getBDay() {
//        return BDay;
//    }
//
//    public void setBDay(String BDay) {
//        this.BDay = BDay;
//    }
//
//
//}