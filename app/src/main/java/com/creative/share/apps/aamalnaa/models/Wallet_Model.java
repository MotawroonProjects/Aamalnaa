package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;

public class Wallet_Model implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data implements Serializable {
        private int id;

                private double Evaluation;

       public int getId() {
           return id;
       }

       public double getEvaluation() {
           return Evaluation;
       }
   }
}
