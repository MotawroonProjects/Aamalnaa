package com.creative.share.apps.aamalnaa.models;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

public class Adversiment_Model implements Serializable {
     private int current_page;
     private List<Data> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable
    {
       private int  id;
        private int category_id;
        private int subcategory_id;
        private int user_id;
        private int city_id;
        private String title;
            private String address;
        private String details;
        private int views_num;
        private int is_Special;
        private int is_Install;
        private int commented;
        private int end_by_client;
        private int my_work;
        private int accepted;
        private int price;
        private double lat;
        private double lng;
            private int code;
private String created_at;
        private int ads_type;
            private String image;
            private String city_name;
        private String category_name;
        private String sub_category_name;
            private int is_like;
            private int is_reported;
private String user_name;
private long date;

        public long getDate() {
            return date;
        }

        public int getId() {
            return id;
        }

        public int getCategory_id() {
            return category_id;
        }

        public int getSubcategory_id() {
            return subcategory_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getCity_id() {
            return city_id;
        }

        public String getTitle() {
            return title;
        }

        public String getAddress() {
            return address;
        }

        public String getDetails() {
            return details;
        }

        public int getViews_num() {
            return views_num;
        }

        public int getIs_Special() {
            return is_Special;
        }

        public int getIs_Install() {
            return is_Install;
        }

        public int getCommented() {
            return commented;
        }

        public int getEnd_by_client() {
            return end_by_client;
        }

        public int getMy_work() {
            return my_work;
        }

        public int getAccepted() {
            return accepted;
        }

        public int getPrice() {
            return price;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public String getCity_name() {
            return city_name;
        }

        public int getCode() {
            return code;
        }

        public String getCreated_at() {
            return created_at;
        }

        public int getAds_type() {
            return ads_type;
        }

        public String getImage() {
            return image;
        }


        public String getCategory_name() {
            return category_name;
        }

        public String getSub_category_name() {
            return sub_category_name;
        }

        public int getIs_like() {
            return is_like;
        }

        public int getIs_reported() {
            return is_reported;
        }

        public String getUser_name() {
            return user_name;
        }
    }
}
