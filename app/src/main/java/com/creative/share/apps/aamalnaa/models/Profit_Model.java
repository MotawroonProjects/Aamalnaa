package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;

public class Profit_Model implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable {
        private int id;
        private double ads_price;
        private double pre_price;
        private double rate_price;
        private double total_price;
        private int user_id;

        public int getId() {
            return id;
        }

        public double getAds_price() {
            return ads_price;
        }

        public double getPre_price() {
            return pre_price;
        }

        public double getRate_price() {
            return rate_price;
        }

        public double getTotal_price() {
            return total_price;
        }

        public int getUser_id() {
            return user_id;
        }
    }
}
