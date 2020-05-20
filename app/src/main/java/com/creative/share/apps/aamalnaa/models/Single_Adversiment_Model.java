package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;
import java.util.List;

public class Single_Adversiment_Model implements Serializable {


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
    private String mobile;

    private int code;
private String created_at;
        private int ads_type;
            private String image;
            private String city;
            private String user_about;
        private String category;
        private String subcategory;
            private int is_like;
            private int like_ad;
             private int follow;
                     private int report;
            private int is_reported;
            private int user_is_login;
                    private long user_current_sing_in_at;
private String user;
private long date;
private List<Images> images;
private List<Comments> comments;

public class Images implements Serializable
    {
        private int id;
        private int ad_id;
            private String image;

        public int getId() {
            return id;
        }

        public int getAd_id() {
            return ad_id;
        }

        public String getImage() {
            return image;
        }
    }
public class Comments implements Serializable{
  private int id;
    private int user_id;
    private int ad_id;
          private String comment;

          private String user_name;
          private String user_image;
private long date;
    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getAd_id() {
        return ad_id;
    }

    public String getComment() {
        return comment;
    }

    public String getUser_name() {
        return user_name;
    }

    public long getDate() {
        return date;
    }

    public String getUser_image() {
        return user_image;
    }
}
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


    public String getUser_about() {
        return user_about;
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


    public String getCity() {
        return city;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getUser() {
        return user;
    }

    public int getIs_like() {
            return is_like;
        }

    public int getLike_ad() {
        return like_ad;
    }

    public int getFollow() {
        return follow;
    }

    public int getReport() {
        return report;
    }

    public int getIs_reported() {
            return is_reported;
        }

    public int getUser_is_login() {
        return user_is_login;
    }

    public long getUser_current_sing_in_at() {
        return user_current_sing_in_at;
    }


    public List<Images> getImages() {
        return images;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setLike_ad(int like_ad) {
        this.like_ad = like_ad;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public String getMobile() {
        return mobile;
    }
}
