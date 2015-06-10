package com.getmebag.bag.androidspecific;

import com.getmebag.bag.connections.ContactListItem;

import java.util.List;

/**
 * Created by karthiktangirala on 6/9/15.
 */
public class ContactsListWrapper {
    List<ContactListItem> contactListItems;

    public ContactsListWrapper(List<ContactListItem> contactListItems) {
        this.contactListItems = contactListItems;
    }

    public List<ContactListItem> getContactListItems() {
        return contactListItems;
    }

    public void setContactListItems(List<ContactListItem> contactListItems) {
        this.contactListItems = contactListItems;
    }
}
