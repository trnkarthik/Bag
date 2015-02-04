package com.getmebag.bag.missile.Entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.List;

/**
 * Created by karthiktangirala on 12/11/14.
 */
@Entity
public class UserList {

    @Id
    long listId;

    Ref<List<Item>> listItems;
}
