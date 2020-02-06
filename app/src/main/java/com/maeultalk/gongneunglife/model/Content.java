package com.maeultalk.gongneunglife.model;

import java.io.Serializable;

public class Content implements Serializable {

    private String id;
    private String place_code;
    private String place_name;
    private String email;
    private String user;
    private String date;
    private String youtube;
    private String content;
    private String comments;
    private String image;
    private String image2;
    private String image3;
    private boolean good;
    private String good_cnt;
    private boolean favorite;

    public Content(String id, String place_code, String place_name, String email, String user, String date, String youtube, String content, String comments, String image, String image2, String image3, boolean good, String good_cnt, boolean favorite) {
        this.id = id;
        this.place_code = place_code;
        this.place_name = place_name;
        this.email = email;
        this.user = user;
        this.date = date;
        this.content = content;
        this.comments = comments;
        this.image = image;
        this.image2 = image2;
        this.image3 = image3;
        this.good = good;
        this.good_cnt = good_cnt;
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public String getPlace_code() {
        return place_code;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getEmail() {
        return email;
    }

    public String getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }

    public String getYoutube() {
        return youtube;
    }

    public String getContent() {
        return content;
    }

    public String getComments() {
        return comments;
    }

    public String getImage() {
        return image;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public boolean isGood() {
        return good;
    }

    public String getGood_cnt() {
        return good_cnt;
    }

    public void setGood_cnt(String good_cnt) {
        this.good_cnt = good_cnt;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}
