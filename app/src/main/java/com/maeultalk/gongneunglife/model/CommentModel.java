package com.maeultalk.gongneunglife.model;

public class CommentModel {
    private String id;
    private String email;
    private String nick;
    private String date;
    private String comment;

    public CommentModel(String id, String email, String nick, String date, String comment) {
        this.id = id;
        this.email = email;
        this.nick = nick;
        this.date = date;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNick() {
        return nick;
    }

    public String getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}