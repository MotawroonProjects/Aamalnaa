package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;

public class UserModel implements Serializable {

private int id;
        private String user_type;
    private String avatar;
    private String name;
    private String full_name;
    private String iban;
    private String mobile;
    private String email;
       private int role_id;
        private int is_Admin;
    private String msg;
    private String about;
        private int city_id;
        private int star_user;
    private int accepted;
    private int amount;
    private String last_sing_in_at;
    private String current_sing_in_at;
    private int is_login;
    private String created_at;
    private String updated_at;

    private String city_name;
    private int total;
    private int Evaluation;
    private int negativeRate;
    private int postivesRate;
    private int is_follow;
    private String lastSeen;

    public int getId() {
        return id;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getIban() {
        return iban;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public int getRole_id() {
        return role_id;
    }

    public int getIs_Admin() {
        return is_Admin;
    }

    public String getMsg() {
        return msg;
    }

    public String getAbout() {
        return about;
    }

    public int getCity_id() {
        return city_id;
    }

    public int getStar_user() {
        return star_user;
    }

    public int getAccepted() {
        return accepted;
    }

    public int getAmount() {
        return amount;
    }

    public String getLast_sing_in_at() {
        return last_sing_in_at;
    }

    public String getCurrent_sing_in_at() {
        return current_sing_in_at;
    }

    public int getIs_login() {
        return is_login;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCity_name() {
        return city_name;
    }

    public int getTotal() {
        return total;
    }

    public int getEvaluation() {
        return Evaluation;
    }

    public int getNegativeRate() {
        return negativeRate;
    }

    public int getPostivesRate() {
        return postivesRate;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public String getLastSeen() {
        return lastSeen;
    }
}
