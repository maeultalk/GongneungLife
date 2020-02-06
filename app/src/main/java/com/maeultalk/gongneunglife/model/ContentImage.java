package com.maeultalk.gongneunglife.model;

import static com.maeultalk.gongneunglife.key.Key.URL_IMAGES;

/*
 * Created by troy379 on 10.03.17.
 */
public class ContentImage {

    private String url;

    public ContentImage(String url) {
        this.url = URL_IMAGES + url;
    }

    public String getUrl() {
        return url;
    }

}