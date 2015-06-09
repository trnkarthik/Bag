package com.getmebag.bag.model;

/**
 * Created by karthiktangirala on 5/4/15.
 */
public class UserLocation {
    Double LocationLat;
    Double LocationLong;
    String zipCode;

    public UserLocation(Double locationLat, Double locationLong, String zipCode) {
        this.LocationLat = locationLat;
        this.LocationLong = locationLong;
        this.zipCode = zipCode;
    }

    public Double getLocationLat() {
        return LocationLat;
    }

    public void setLocationLat(Double locationLat) {
        LocationLat = locationLat;
    }

    public Double getLocationLong() {
        return LocationLong;
    }

    public void setLocationLong(Double locationLong) {
        LocationLong = locationLong;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
