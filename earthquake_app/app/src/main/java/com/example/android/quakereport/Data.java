package com.example.android.quakereport;


public class Data {

    Double mag;
    String location;
    Long date;
    String url;

    public Data(Double mag, String location, Long date, String url) {
        this.mag = mag;
        this.location = location;
        this.date = date;
        this.url = url;
    }

    public Double getMag() {
        return mag;
    }

    public String getLocation() {
        return location;
    }

    public Long getTimeInMilliseconds() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
