package com.example.android.quakereport;


public class Data {

    String mag;
    String location;
    String date;

    public Data(String mag, String location, String date) {
        this.mag = mag;
        this.location = location;
        this.date = date;
    }

    public String getMag() {
        return mag;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }
}
