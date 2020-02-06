package com.maeultalk.gongneunglife.test;

public class YoutubeList {

    String text;
    String youtube;

    public YoutubeList(String text, String youtube) {
        this.text = text;
        this.youtube = youtube;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }
}
