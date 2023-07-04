package com.facochico.DBProject.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AdditionalClientInfo {
    @Id
    private Long id;

    private String clothSize;
    private String footSize;
    private String lastMsg;
    @Column(columnDefinition = "TEXT")
    private String description;

    public AdditionalClientInfo(Long id, String clothSize, String footSize, String lastMsg, String description) {
        this.id = id;
        this.clothSize = clothSize;
        this.footSize = footSize;
        this.lastMsg = lastMsg;
        this.description = description;
    }

    public AdditionalClientInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClothSize() {
        return clothSize;
    }

    public void setClothSize(String clothSize) {
        this.clothSize = clothSize;
    }

    public String getFootSize() {
        return footSize;
    }

    public void setFootSize(String footSize) {
        this.footSize = footSize;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}