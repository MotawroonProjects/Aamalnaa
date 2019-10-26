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
           private String title;
         private String details;
            private String image;
            private String created_at;
            private String updated_at;

        public int getId() {
            return id;
        }


        public String getTitle() {
            return title;
        }

        public String getDetails() {
            return details;
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
