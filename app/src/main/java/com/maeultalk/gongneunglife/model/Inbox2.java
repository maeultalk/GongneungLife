package com.maeultalk.gongneunglife.model;

public class Inbox2 {

    private String id;
    private String inbox;
    private String send;
    private String place_code;
    private String place_name;
    private String contents;
    private String good;
    private String nmap;
    private String collect;
    private String image;
    private String image2;
    private String image3;

    public Inbox2() {
    }

    public Inbox2(String send, String contents) {
        this.send = send;
        this.contents = contents;
    }

    public Inbox2(String id, String inbox, String send, String contents) {
        this.id = id;
        this.inbox = inbox;
        this.send = send;
        this.contents = contents;
    }

    public Inbox2(String id, String inbox, String send, String place_code, String place_name, String contents, String good, String nmap, String collect, String image, String image2, String image3) {
        this.id = id;
        this.inbox = inbox;
        this.send = send;
        this.place_code = place_code;
        this.place_name = place_name;
        this.contents = contents;
        this.good = good;
        this.nmap = nmap;
        this.collect = collect;
        this.image = image;
        this.image2 = image2;
        this.image3 = image3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInbox() {
        return inbox;
    }

    public void setInbox(String inbox) {
        this.inbox = inbox;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getPlace_code() {
        return place_code;
    }

    public void setPlace_code(String place_code) {
        this.place_code = place_code;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public String getNmap() {
        return nmap;
    }

    public void setNmap(String nmap) {
        this.nmap = nmap;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}
