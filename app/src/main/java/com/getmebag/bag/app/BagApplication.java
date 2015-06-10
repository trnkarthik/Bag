package com.getmebag.bag.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.firebase.client.Firebase;
import com.getmebag.bag.androidspecific.AndroidModule;
import com.getmebag.bag.androidspecific.ContactsListWrapper;
import com.getmebag.bag.androidspecific.prefs.CustomObjectPreference;
import com.getmebag.bag.base.UIBaseModule;
import com.getmebag.bag.connections.ContactListItem;
import com.getmebag.bag.connections.ContactsProvider;
import com.getmebag.bag.connections.InviteContactsModule;
import com.getmebag.bag.ftx.UIFTXModule;
import com.getmebag.bag.login.UILoginModule;
import com.getmebag.bag.settings.SettingsModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by karthiktangirala on 1/2/15.
 */
public class BagApplication extends Application {
    private ObjectGraph graph;

    private final String MODE_ALL_CONTATCS = "BagApplication.GetAllContacts";
    private final String MODE_FREQUENT_CONTATCS = "BagApplication.GetFrequentContacts";

    private final int FREQUENT_CONTATCS_COUNT = 10;

    ContactsProvider contactsProvider;

    CustomObjectPreference<ContactsListWrapper> allContactsListPreference;
    CustomObjectPreference<ContactsListWrapper> frequentContactsListPreference;

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

        graph = ObjectGraph.create(getModules().toArray());

        contactsProvider = new ContactsProvider(getApplicationContext());

        SharedPreferences sharedPreferences = getSharedPreferences("bag", MODE_PRIVATE);
        allContactsListPreference = new CustomObjectPreference<>(sharedPreferences, "bag_all_contacts", null);
        frequentContactsListPreference = new CustomObjectPreference<>(sharedPreferences, "bag_frequent_contacts", null);
        updateContactsAsynchronously();
    }

    protected List<Object> getModules() {
        return Arrays.asList(
                new AndroidModule(this),
                new UIBaseModule(),
                new UILoginModule(),
                new SettingsModule(),
                new UIFTXModule(),
                new InviteContactsModule()
        );
    }

    public void inject(Object object) {
        graph.inject(object);
    }

    private void updateContactsAsynchronously() {
        fetchcontactsInBackground(MODE_ALL_CONTATCS);
        fetchcontactsInBackground(MODE_FREQUENT_CONTATCS);
    }

    private void fetchcontactsInBackground(String mode) {
        new AsyncTask<String, Void, List<ContactListItem>>() {
            public String currentMode = MODE_ALL_CONTATCS;

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected List<ContactListItem> doInBackground(String... params) {
                currentMode = params[0];
                if (currentMode.equals(MODE_ALL_CONTATCS)) {
                    return contactsProvider.getAllContacts();
                } else if (currentMode.equals(MODE_FREQUENT_CONTATCS)) {
                    return contactsProvider.getFrequentContacts(FREQUENT_CONTATCS_COUNT);
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<ContactListItem> result) {
                //update corresponding shared pref object
                if (currentMode.equals(MODE_ALL_CONTATCS)) {
                    allContactsListPreference.set(new ContactsListWrapper(result));
                } else if (currentMode.equals(MODE_FREQUENT_CONTATCS)) {
                    frequentContactsListPreference.set(new ContactsListWrapper(result));
                }
            }
        }.execute(mode);
    }

}
