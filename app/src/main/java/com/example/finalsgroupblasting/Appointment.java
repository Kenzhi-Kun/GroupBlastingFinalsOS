package com.example.finalsgroupblasting;

public class Appointment {
    private String id;
    private String title;
    private String time;
    private String date;

    // Required empty public constructor for Firebase
    public Appointment() {
    }

    public Appointment(String id, String title, String time, String date) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
