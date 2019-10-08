package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;
import java.util.List;

public class Catogries_Model implements Serializable {
private List<Data> data;


    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable
    {
        public Data(String name) {
            this.name = name;
        }

        private String id;
        private String  icon;
        private String  name;
              private int commission;
private List<Subcategory> subcategory;

        public Data(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getIcon() {
            return icon;
        }

        public String getName() {
            return name;
        }

        public int getCommission() {
            return commission;
        }

        public List<Subcategory> getSubcategory() {
            return subcategory;
        }

        public static class Subcategory implements Serializable
        {
            private int id;
                private int category_id;
                private String name;

            public Subcategory(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public int getCategory_id() {
                return category_id;
            }

            public String getName() {
                return name;
            }
        }
    }
}

