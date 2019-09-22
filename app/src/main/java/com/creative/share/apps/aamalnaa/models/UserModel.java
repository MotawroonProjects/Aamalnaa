package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;

public class UserModel implements Serializable {

    private User user;
    private Information information;

    public User getUser() {
        return user;
    }

    public class User
    {
        private int id;
        private String name;
        private String email;
        private String user_type;
        private int be_company;
        private double latitude;
        private double longitude;
        private String national_image;
        private String responsible;
        private String phone_code;
        private String phone;
        private String address;
        private int is_available;
        private double ratings;
        private String image;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getUser_type() {
            return user_type;
        }

        public int getBe_company() {
            return be_company;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getNational_image() {
            return national_image;
        }

        public String getResponsible() {
            return responsible;
        }

        public String getPhone() {
            return phone;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public int getIs_available() {
            return is_available;
        }

        public double getRatings() {
            return ratings;
        }

        public String getImage() {
            return image;
        }

        public String getAddress() {
            return address;
        }
    }

    public Information getInformation() {
        return information;
    }

    public class Information implements Serializable
    {
        private int accepted;
        private int refused;
        private int money;
        private int site_money;
        private int credit_limit;


        public int getAccepted() {
            return accepted;
        }

        public int getRefused() {
            return refused;
        }

        public int getMoney() {
            return money;
        }

        public int getSite_money() {
            return site_money;
        }

        public int getCredit_limit() {
            return credit_limit;
        }
    }
}
