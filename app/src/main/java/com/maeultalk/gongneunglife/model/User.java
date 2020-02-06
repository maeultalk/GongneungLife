package com.maeultalk.gongneunglife.model;

public class User {

    String id;
    String email;
    String nick;

    public User() {
    }

    public User(String id, String email, String nick) {
        this.id = id;
        this.email = email;
        this.nick = nick;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
