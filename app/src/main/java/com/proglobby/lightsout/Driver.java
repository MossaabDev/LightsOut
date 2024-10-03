package com.proglobby.lightsout;

public class Driver {
    private String name;
    private String team;
    private String img;
    private String time;

    public Driver(String name, String team, String img, String time) {
        this.name = name;
        this.team = team;
        this.img = img;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
