package com.facochico.DBProject.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String patronymic;
    private String surname;
    private String sex;
    private String phoneNumber;
    private String bDay;

    public Client(String name, String patronymic, String surname, String sex, String phoneNumber, String bDay) {
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.bDay = bDay;
    }

    public Client() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBDay() {
        return bDay;
    }

    public void setBDay(String bDay) {
        this.bDay = bDay;
    }

    public boolean matchesFullName(String query) {
        String[] parts = query.trim().split("\\s+");

        for (int i = 0; i < parts.length; i++)
            parts[i] = parts[i].toLowerCase();

        boolean surnameMatched = false;
        boolean nameMatched = false;
        boolean patronymicMatched = false;

        for (String queryPart : parts) {
            if (surname != null) {
                surnameMatched = surname.toLowerCase().contains(queryPart);
            }
            else surnameMatched = true;

            if (name != null) {
                nameMatched = name.toLowerCase().contains(queryPart);
            }
            else nameMatched = true;

            if (patronymic != null) {
                patronymicMatched = patronymic.toLowerCase().contains(queryPart);
            }
            else patronymicMatched = true;
        }

        return nameMatched || patronymicMatched || surnameMatched;
    }
}