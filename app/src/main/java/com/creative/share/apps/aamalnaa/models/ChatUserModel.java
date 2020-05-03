package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;

public class ChatUserModel implements Serializable {

    private String name;
    private String image;
    private int id;
    private String phone_code;
    private String phone;


    public ChatUserModel(String name, String image, int id, String phone) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }


    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }
}
