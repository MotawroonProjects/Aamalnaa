package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;

public class App_Data_Model implements Serializable {
    private String conditions;
private double commission;
private String advantages;
private String about;
    public String getConditions() {
        return conditions;
    }

    public double getCommission() {
        return commission;
    }

    public String getAdvantages() {
        return advantages;
    }

    public String getAbout() {
        return about;
    }
}
