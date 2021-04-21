package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;
import java.util.List;

public class MessageModel implements Serializable {
    private SingleMessageModel data;

    public SingleMessageModel getData() {
        return data;
    }

    public static class SingleMessageModel implements Serializable {
        private int id;
        private int sender_id;
        private int receiver_id;
        private int room_id;
        private String message;
        private int seen;
        private String type;
        private double lat;
        private double lng;
        private long date_stamp;
        private String user_name;
        private String user_avatar;

        public SingleMessageModel(int sender_id, int receiver_id, String message, String type, double lat, double lng) {
            this.sender_id = sender_id;
            this.receiver_id = receiver_id;
            this.message = message;
            this.type = type;
            this.lat = lat;
            this.lng = lng;
        }

        public int getId() {
            return id;
        }

        public int getSender_id() {
            return sender_id;
        }

        public int getReceiver_id() {
            return receiver_id;
        }

        public int getRoom_id() {
            return room_id;
        }

        public String getMessage() {
            return message;
        }

        public int getSeen() {
            return seen;
        }

        public String getType() {
            return type;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public long getDate_stamp() {
            return date_stamp;
        }
    }
}