package com.getmebag.bag.connections;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class ContactsProvider {

    private Uri FREQUENT_QUERY_URI = ContactsContract.Contacts.CONTENT_STREQUENT_URI;
    private Uri ALL_CONTACTS_QUERY_URI = ContactsContract.Contacts.CONTENT_URI;

    private String CONTACT_ID = ContactsContract.Contacts._ID;
    private String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    private String PHOTO_THUMBNAIL_URL = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI;
    private Uri EMAIL_CONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
    private String EMAIL_CONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
    private String EMAIL_DATA = ContactsContract.CommonDataKinds.Email.DATA;
    private String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    private String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private Uri PHONE_CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    private String STARRED_CONTACT = ContactsContract.Contacts.STARRED;
    private ContentResolver contentResolver;
    private int count = 0;
    final Pattern numberPattern = Patterns.PHONE;

    @Inject
    public ContactsProvider(Context context) {
        contentResolver = context.getContentResolver();
    }

    public List<ContactListItem> getAllContacts() {
        List<ContactListItem> contactListItemList = new ArrayList<ContactListItem>();
        String[] projection = new String[]{CONTACT_ID, DISPLAY_NAME, HAS_PHONE_NUMBER,
                STARRED_CONTACT, PHOTO_THUMBNAIL_URL};
        String selection = HAS_PHONE_NUMBER + "!= 0";
        Cursor cursor = contentResolver.query(ALL_CONTACTS_QUERY_URI, projection, selection, null,
                "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
        while (cursor.moveToNext()) {
            ContactListItem contactListItem = getContact(cursor);
            if (numberPattern.matcher(contactListItem.phone).matches()) {
                contactListItemList.add(contactListItem);
            }
        }

        cursor.close();
        return contactListItemList;
    }

    public List<ContactListItem> getFrequentContacts(int numberOfContactsRequired) {
        List<ContactListItem> contactListItemList = new ArrayList<ContactListItem>();
        String[] projection = new String[]{CONTACT_ID, DISPLAY_NAME, HAS_PHONE_NUMBER,
                STARRED_CONTACT, PHOTO_THUMBNAIL_URL};
        String selection = HAS_PHONE_NUMBER + "!= 0";
        Cursor cursor = contentResolver.query(FREQUENT_QUERY_URI, projection, selection, null, null);

        //init count
        count = 0;

        while (cursor.moveToNext() && count < numberOfContactsRequired) {
            ContactListItem contactListItem = getContact(cursor);
            if (numberPattern.matcher(contactListItem.phone).matches()) {
                contactListItemList.add(contactListItem);
                count++;
            }
        }

        cursor.close();
        return contactListItemList;
    }

    private ContactListItem getContact(Cursor cursor) {
        String contactId = cursor.getString(cursor.getColumnIndex(CONTACT_ID));
        String name = (cursor.getString(cursor.getColumnIndex(DISPLAY_NAME)));
        Uri uri = Uri.withAppendedPath(FREQUENT_QUERY_URI, String.valueOf(contactId));
        String photoThumbnailUrl = (cursor.getString(cursor.getColumnIndex(PHOTO_THUMBNAIL_URL)));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        String intentUriString = intent.toUri(0);

        ContactListItem contactListItem = new ContactListItem();
        contactListItem.id = Integer.valueOf(contactId);
        contactListItem.name = name;
        contactListItem.uriString = intentUriString;
        contactListItem.photoThumbnailUrl = photoThumbnailUrl;

        getPhone(cursor, contactId, contactListItem);

        //TODO : We dont need email for now
        //getEmail(contactId, contactListItem);

        return contactListItem;
    }

    private void getPhone(Cursor cursor, String contactId, ContactListItem contactListItem) {
        int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
        if (hasPhoneNumber > 0) {
            Cursor phoneCursor = contentResolver.query(PHONE_CONTENT_URI, null, PHONE_CONTACT_ID + " = ?", new String[]{contactId}, null);
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(PHONE_NUMBER));
                contactListItem.phone = phoneNumber;
            }
            phoneCursor.close();
        }
    }

    /*
    TODO : We dont need email for now
    private void getEmail(String contactId, ContactListItem contactListItem) {
        Cursor emailCursor = contentResolver.query(EMAIL_CONTENT_URI, null, EMAIL_CONTACT_ID + " = ?", new String[]{contactId}, null);
        while (emailCursor.moveToNext()) {
            String email = emailCursor.getString(emailCursor.getColumnIndex(EMAIL_DATA));
            if (!TextUtils.isEmpty(email)) {
                contactListItem.email = email;
            }
        }
        emailCursor.close();
    }
*/

}
