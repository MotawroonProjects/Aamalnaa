package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;
import java.util.List;

public class UserRoomModelData implements Serializable {

    private List<UserRoomModel> data;

    public List<UserRoomModel> getData() {
        return data;
    }



        public class UserRoomModel implements Serializable
        {
            private int id;
            private int sender_id;
            private int receiver_id;
                private String last_message;
            private String sender_name;
            private String sender_avatar;
            private String receiver_name;
            private String receiver_avatar;
            public int getId() {
                return id;
            }

            public int getSender_id() {
                return sender_id;
            }

            public int getReceiver_id() {
                return receiver_id;
            }

            public String getLast_message() {
                return last_message;
            }

            public String getSender_name() {
                return sender_name;
            }

            public String getSender_avatar() {
                return sender_avatar;
            }

            public String getReceiver_name() {
                return receiver_name;
            }

            public String getReceiver_avatar() {
                return receiver_avatar;
            }

            public void setSender_id(int sender_id) {
                this.sender_id = sender_id;
            }

            public void setReceiver_id(int receiver_id) {
                this.receiver_id = receiver_id;
            }

            public void setSender_name(String sender_name) {
                this.sender_name = sender_name;
            }

            public void setSender_avatar(String sender_avatar) {
                this.sender_avatar = sender_avatar;
            }

            public void setReceiver_name(String receiver_name) {
                this.receiver_name = receiver_name;
            }

            public void setReceiver_avatar(String receiver_avatar) {
                this.receiver_avatar = receiver_avatar;
            }

            public void setLast_message(String last_message) {
                this.last_message = last_message;
            }
        }
}
