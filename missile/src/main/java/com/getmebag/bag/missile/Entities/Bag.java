package com.getmebag.bag.missile.Entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.List;

/**
 * Created by karthiktangirala on 12/11/14.
 */
@Entity
public class Bag {

    @Id
    long bagId;

    Ref<List<UserList>> userLists;

    public Bag() {
    }

    public Bag(long bagId, Ref<List<UserList>> userLists) {
        this.bagId = bagId;
        this.userLists = userLists;
    }

    public Ref<List<UserList>> getUserLists() {
        return userLists;
    }

    public void setUserLists(Ref<List<UserList>> userLists) {
        this.userLists = userLists;
    }

    public long getBagId() {
        return bagId;
    }

    public void setBagId(long bagId) {
        this.bagId = bagId;
    }
}
