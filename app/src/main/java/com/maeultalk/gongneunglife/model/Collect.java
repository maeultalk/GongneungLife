package com.maeultalk.gongneunglife.model;

import java.io.Serializable;

public class Collect implements Serializable {

    String id;
    String collect;
    String image;
    String prm;

    public Collect(String id, String collect, String image, String prm) {
        this.id = id;
        this.collect = collect;
        this.image = image;
        this.prm = prm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPrm() {
        return prm;
    }

    public void setPrm(String prm) {
        this.prm = prm;
    }
}
