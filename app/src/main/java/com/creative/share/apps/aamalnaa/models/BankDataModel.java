package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;
import java.util.List;

public class BankDataModel implements Serializable {

    private List<BankModel> data;

    public List<BankModel> getData() {
        return data;
    }


    public static class BankModel implements Serializable
    {
         private int id;
                 private String image;
                 private String iban;
                 private String number;
                 private String name;

        public BankModel(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getImage() {
            return image;
        }

        public String getIban() {
            return iban;
        }

        public String getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }
    }
}
