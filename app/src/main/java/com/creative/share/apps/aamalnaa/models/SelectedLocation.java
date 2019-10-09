package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;

public class SelectedLocation implements Serializable {
    private double lat;
    private double lng;
    private String address;

    public SelectedLocation(double lat, double lng, String address) {
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getAddress() {
        return address;
    }
}
