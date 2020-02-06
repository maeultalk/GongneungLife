package com.maeultalk.gongneunglife.model;

public class Place {
    private String id;
    private String code;
    private String name;
    private String type;
    private String tel;
    private String nmap;
    private String latitude;
    private String longitude;
    private String image;

    public Place(String id, String code, String name, String type, String tel, String nmap, String latitude, String longitude, String image) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.type = type;
        this.tel = tel;
        this.nmap = nmap;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getTel() {
        return tel;
    }

    public String getNmap() {
        return nmap;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getImage() {
        return image;
    }
}
