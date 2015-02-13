package com.getmebag.bag.missile.Entities;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class BagUser {

    @Id
    long userId;

    User user;
    String userName;
    Email emailId;
    GeoPt currentLocation;
    Ref<Bag> bag;

    public BagUser() {
    }

    public BagUser(long userId, User bagUser, String userName, Email emailId, GeoPt currentLocation) {
        this.userId = userId;
        this.user = bagUser;
        this.userName = userName;
        this.emailId = emailId;
        this.currentLocation = currentLocation;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Email getEmailId() {
        return emailId;
    }

    public void setEmailId(Email emailId) {
        this.emailId = emailId;
    }

    public GeoPt getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoPt currentLocation) {
        this.currentLocation = currentLocation;
    }
}

