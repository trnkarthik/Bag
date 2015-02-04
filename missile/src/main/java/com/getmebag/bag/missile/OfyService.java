package com.getmebag.bag.missile;

import com.getmebag.bag.missile.Entities.Bag;
import com.getmebag.bag.missile.Entities.Bagger;
import com.getmebag.bag.missile.Entities.Item;
import com.getmebag.bag.missile.Entities.Quantity;
import com.getmebag.bag.missile.Entities.Store;
import com.getmebag.bag.missile.Entities.UserList;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Objectify service wrapper so we can statically register our persistence classes
 * More on Objectify here : https://code.google.com/p/objectify-appengine/
 *
 */
public class OfyService {

    static {
        ObjectifyService.register(Bag.class);
        ObjectifyService.register(Bagger.class);
        ObjectifyService.register(Item.class);
        ObjectifyService.register(Quantity.class);
        ObjectifyService.register(Store.class);
        ObjectifyService.register(UserList.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
