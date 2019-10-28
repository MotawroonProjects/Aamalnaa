package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;
import java.util.List;

public class NotificationDataModel implements Serializable {

    private List<NotificationModel> data;
private int current_page;
    public List<NotificationModel> getData() {
        return data;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public class NotificationModel implements Serializable
    {
        private int id;
                private String note;
private long date;
        public int getId() {
            return id;
        }

        public String getNote() {
            return note;
        }

        public long getDate() {
            return date;
        }
    }
}
