package com.example.test_project.Retrofit;

public class Modal {

    int id;
    String title;
    Location location;
    long timestamp;

    public Modal(int id, String title, Location location, long timestamp) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

