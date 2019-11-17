package com.creative.share.apps.aamalnaa.models;

public class Filter_Model {
    private static String city_id;
    private static String lat;
    private static String lng;
    private static int is_new;
    private static int photo;
    private static String id;

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Filter_Model.id = id;
    }

    public static int getPhoto() {
        return photo;
    }

    public static String getCity_id() {
        return city_id;
    }

    public static void setCity_id(String city_id) {
        Filter_Model.city_id = city_id;
    }

    public static String getLat() {
        return lat;
    }

    public static void setLat(String lat) {
        Filter_Model.lat = lat;
    }

    public static String getLng() {
        return lng;
    }

    public static void setLng(String lng) {
        Filter_Model.lng = lng;
    }

    public static int getIs_new() {
        return is_new;
    }

    public static void setIs_new(int is_new) {
        Filter_Model.is_new = is_new;
    }

    public static void setphoto(int i) {
        Filter_Model.photo=i;
    }
}
