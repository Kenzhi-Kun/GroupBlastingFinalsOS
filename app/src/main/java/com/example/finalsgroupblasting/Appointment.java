package com.example.finalsgroupblasting;

public class Appointment {

    private String user;
    private String time;
    private String date;
    private String key;

    public Appointment() {
    }

    public Appointment(String user, String date, String time) {
        this.user = user;
        this.key = key;
        this.time = time;
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
