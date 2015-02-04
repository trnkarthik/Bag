package com.getmebag.bag.missile.Entities;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.impl.translate.opt.joda.MoneyStringTranslatorFactory;

import java.math.BigDecimal;
import java.util.List;


/**
 * Created by karthiktangirala on 11/1/14.
 */
@Entity
public class Item {

    @Id
    long itemId;
    String itemName;
    Quantity quantity;

    //We should change this to joda money
    BigDecimal price;
    List<Category> categoriesList;
    String notes;
    Store store;


    Item() {
    }

}
