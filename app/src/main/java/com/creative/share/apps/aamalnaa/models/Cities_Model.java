package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;
import java.util.List;

public class Cities_Model implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable
    {
        private int id;
            private String name;
        private String created_at;
        private String updated_at;

        public Data(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}

