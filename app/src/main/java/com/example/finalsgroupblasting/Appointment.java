package com.example.finalsgroupblasting;

public class Appointment {
    private String id;
    private String title;
    private String time;
    private String date;
    private String userId;
    private String reason; // Added reason field

    public Appointment() {
    }

    public Appointment(String id, String title, String time, String date, String userId, String reason) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.date = date;
        this.userId = userId;
        this.reason = reason;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public void setKey(String key) { this.id = key; }

    public String getKey() { return id; }

    public String getUser() { return userId; }
}
