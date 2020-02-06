package com.maeultalk.gongneunglife.model;

public class InboxItem {

//    private Drawable icon;
    private String id;
    private String user;
    private String subject;
    private String badge;

    public InboxItem() {
    }

    public InboxItem(String subject, String badge) {
        this.subject = subject;
        this.badge = badge;
    }

    public InboxItem(String id, String user, String subject, String badge) {
        this.id = id;
        this.user = user;
        this.subject = subject;
        this.badge = badge;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }
}