package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;
import java.util.List;

public class Slider_Model implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public  class  Data implements Serializable
    {
        private int id;
            private String ar_title;
           private String en_title;
            private String ar_description;
            private String en_description;
            private String image;
            private String created_at;
            private String updated_at;

        public int getId() {
            return id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_description() {
            return ar_description;
        }

        public String getEn_description() {
            return en_description;
        }

        public String getImage() {
            return image;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
