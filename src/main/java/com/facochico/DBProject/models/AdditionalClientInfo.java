package com.facochico.DBProject.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class AdditionalClientInfo {
    @Id
    private Long id;

    private String socialStatus;
    private String clothSize;
    private String footSize;
    private String lastMsg;
    private String lastPurchase;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] clientPhoto;

    public AdditionalClientInfo(Long id, String socialStatus, String clothSize, String footSize, String lastMsg, String lastPurchase, String description, byte[] clientPhoto) {
        this.id = id;
        this.socialStatus = socialStatus;
        this.clothSize = clothSize;
        this.footSize = footSize;
        this.lastMsg = lastMsg;
        this.description = description;
        this.lastPurchase = lastPurchase;
        this.clientPhoto = clientPhoto;
    }

    public AdditionalClientInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSocialStatus() {
        return socialStatus;
    }

    public void setSocialStatus(String socialStatus) {
        this.socialStatus = socialStatus;
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

    public String getLastPurchase() {
        return lastPurchase;
    }

    public void setLastPurchase(String lastPurchase) {
        this.lastPurchase = lastPurchase;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getClientPhoto() {
        return clientPhoto;
    }

    public void setClientPhoto(byte[] clientPhoto) {
        this.clientPhoto = clientPhoto;
    }
}