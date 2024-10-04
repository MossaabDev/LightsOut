package com.proglobby.lightsout;

public class User extends Driver{
    private String email;

    public User(String name, String team, String img, String time) {
        super(name, team, img, time);
        email = "";
    }

    public User(String name, String team, String img, String time, String email) {
        super(name, team, img, time);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
