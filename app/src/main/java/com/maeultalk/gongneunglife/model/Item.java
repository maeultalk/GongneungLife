package com.maeultalk.gongneunglife.model;

public class Item {
    String id;
    int image;
    String imageString;
    String title;
    boolean test;

    public String getId() {
        return id;
    }

    public int getImage() {
        return this.image;
    }

    public String getImageString() {
        return imageString;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isTest() {
        return test;
    }

    public Item(String id, int image, String title, boolean test) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.test = test;
    }

    public Item(String id, String imageString, String title, boolean test) {
        this.id = id;
        this.imageString = imageString;
        this.title = title;
        this.test = test;
    }
}
