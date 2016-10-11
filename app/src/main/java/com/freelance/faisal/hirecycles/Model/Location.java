package com.freelance.faisal.hirecycles.Model;

import com.freelance.faisal.hirecycles.LoginActivity;

/**
 * Created by Faisal on 9/2/16.
 */
public class Location {
    public String id;
    public String name;
    public Double lat;
    public Double lng;

    public Location(String id, String name, Double lat, Double lng) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
