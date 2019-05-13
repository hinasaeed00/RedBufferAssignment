package com.redbuffer.redbufferassignment.model;

import com.redbuffer.redbufferassignment.communication.CommonResponse;

import java.io.Serializable;
import java.lang.ref.SoftReference;

public class User  implements Serializable {
    private int id;
    private String username;
    private String email;
    private double lat;
    private double lng;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
