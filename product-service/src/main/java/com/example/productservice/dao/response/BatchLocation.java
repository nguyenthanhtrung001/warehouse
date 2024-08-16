package com.example.productservice.dao.response;

public class BatchLocation {
    String location;
    String bath;

    public BatchLocation() {
    }

    public BatchLocation(String location, String bath) {
        this.location = location;
        this.bath = bath;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBath() {
        return bath;
    }

    public void setBath(String bath) {
        this.bath = bath;
    }
}
