package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;
import java.util.List;

public class Service_Model  implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable
    {
        private int id;
        private int coun;
           private String name;
            private String price;

        public int getId() {
            return id;
        }

        public int getCoun() {
            return coun;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public void setName(String name) {
            this.name=name;
        }
    }
}
