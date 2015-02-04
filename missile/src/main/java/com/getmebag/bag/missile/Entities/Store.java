package com.getmebag.bag.missile.Entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by karthiktangirala on 12/11/14.
 */
@Entity
public class Store {

    @Id
    long storeId;

    String storeName;
}
