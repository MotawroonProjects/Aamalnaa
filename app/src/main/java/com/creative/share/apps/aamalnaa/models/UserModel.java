package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    private User user;
    private List<Customers> customers;
    private List<Ads> ads;
    private List<Rateds> rateds;
private List<Previous> previous;

    public User getUser() {
        return user;
    }

    public List<Customers> getCustomers() {
        return customers;
    }

    public List<Ads> getAds() {
        return ads;
    }

    public List<Rateds> getRateds() {
        return rateds;
    }

    public List<Previous> getPrevious() {
        return previous;
    }

    public class User implements Serializable{
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
    }}
 public class Customers implements Serializable
    {
        private int id;
        private int follower_id;
        private int followed_id;
        private String created_at;
        private String updated_at;
        private String user_name;
        private String user_avatar;

        public int getId() {
            return id;
        }

        public int getFollower_id() {
            return follower_id;
        }

        public int getFollowed_id() {
            return followed_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getUser_avatar() {
            return user_avatar;
        }
    }
     public class Ads implements Serializable
    {
        private int  id;
        private int category_id;
        private int subcategory_id;
        private int user_id;
        private int city_id;
        private String title;
        private String address;
        private String details;
        private int views_num;
        private int is_Special;
        private int is_Install;
        private int commented;
        private int end_by_client;
        private int my_work;
        private int accepted;
        private int price;
        private double lat;
        private double lng;
        private int code;
        private String created_at;
        private int ads_type;
        private String image;
        private String city_name;
        private String category_name;
        private String sub_category_name;
        private int is_like;
        private int is_reported;
        private String user_name;
        private long date;

        public long getDate() {
            return date;
        }

        public int getId() {
            return id;
        }

        public int getCategory_id() {
            return category_id;
        }

        public int getSubcategory_id() {
            return subcategory_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getCity_id() {
            return city_id;
        }

        public String getTitle() {
            return title;
        }

        public String getAddress() {
            return address;
        }

        public String getDetails() {
            return details;
        }

        public int getViews_num() {
            return views_num;
        }

        public int getIs_Special() {
            return is_Special;
        }

        public int getIs_Install() {
            return is_Install;
        }

        public int getCommented() {
            return commented;
        }

        public int getEnd_by_client() {
            return end_by_client;
        }

        public int getMy_work() {
            return my_work;
        }

        public int getAccepted() {
            return accepted;
        }

        public int getPrice() {
            return price;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public int getCode() {
            return code;
        }

        public String getCreated_at() {
            return created_at;
        }

        public int getAds_type() {
            return ads_type;
        }

        public String getImage() {
            return image;
        }

        public String getCity_name() {
            return city_name;
        }

        public String getCategory_name() {
            return category_name;
        }

        public String getSub_category_name() {
            return sub_category_name;
        }

        public int getIs_like() {
            return is_like;
        }

        public int getIs_reported() {
            return is_reported;
        }

        public String getUser_name() {
            return user_name;
        }
    }
     public class Rateds implements Serializable
    {
        private int id;
        private int user_id;
        private int ad_id;
        private int liked;
        private int rated_id;
        private String reason;
        private String created_at;

           private String user_name;
        private String user_avatar;

        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getAd_id() {
            return ad_id;
        }

        public int getLiked() {
            return liked;
        }

        public int getRated_id() {
            return rated_id;
        }

        public String getReason() {
            return reason;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getUser_avatar() {
            return user_avatar;
        }
    }
    public class Previous implements Serializable
    {
        private int id;
            private int user_id;
            private int follower_id;

            private String user_name;
        private String user_avatar;

        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getFollower_id() {
            return follower_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getUser_avatar() {
            return user_avatar;
        }
    }
}
