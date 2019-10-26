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
        }
}
