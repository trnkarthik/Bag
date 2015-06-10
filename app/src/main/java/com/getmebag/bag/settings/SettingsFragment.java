package com.getmebag.bag.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.Session;
import com.firebase.client.AuthData;
import com.getmebag.bag.R;
import com.getmebag.bag.androidspecific.prefs.CustomObjectPreference;
import com.getmebag.bag.annotations.CurrentUserPreference;
import com.getmebag.bag.base.BagAuthBaseFragment;
import com.getmebag.bag.connections.InviteContactsActivity;
import com.getmebag.bag.ftx.FTXLocationActivity;
import com.getmebag.bag.login.LoginActivity;
import com.getmebag.bag.model.BagUser;
import com.getmebag.bag.userprofile.UserProfileActivity;
import com.google.android.gms.plus.Plus;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
* Created by karthiktangirala on 4/5/15.
*/
public class SettingsFragment extends BagAuthBaseFragment {

    @InjectView(R.id.settings_list_view)
    ListView settingsListView;

    ArrayList<SettingsListItem> settingsListItems = new ArrayList<>();

//    @InjectView(R.id.logout)
//    Button logoutButton;

//    @InjectView(R.id.profile)
//    Button profileButton;

    @Inject @CurrentUserPreference
    CustomObjectPreference<BagUser> currentUserPreference;

    @Inject SettingsListAdapter settingsListAdapter;

    @Inject
    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, rootView);

        setUpSettingsList();
//        setUpLogoutButton();
//        setUpProfileButton();

        return rootView;
    }

    private void setUpSettingsList() {
        SettingsListItem helpItem = SettingsListItem.builder()
                .setMainIcon(getString(R.string.icon_font_question))
                .setTitle(getString(R.string.settings_help))
                .setType(SettingsItemType.HELP)
                .build();

        SettingsListItem profileItem = SettingsListItem.builder()
                .setMainIcon(getString(R.string.icon_font_user))
                .setTitle(getString(R.string.settings_profile))
                .setType(SettingsItemType.PROFILE)
                .build();

        SettingsListItem locationItem = SettingsListItem.builder()
                .setMainIcon(getString(R.string.icon_font_location))
                .setTitle(getString(R.string.settings_location))
                .setType(SettingsItemType.LOCATION)
                .build();

        SettingsListItem inviteContactsItem = SettingsListItem.builder()
                .setMainIcon(getString(R.string.icon_font_invite_contacts))
                .setTitle(getString(R.string.settings_invite_contacts))
                .setType(SettingsItemType.INVITE_CONTACTS)
                .build();

        SettingsListItem logoutItem = SettingsListItem.builder()
                .setMainIcon(getString(R.string.icon_font_logout))
                .setTitle(getString(R.string.settings_logout))
                .setType(SettingsItemType.LOGOUT)
                .build();

        settingsListItems.add(profileItem);
        settingsListItems.add(locationItem);
        settingsListItems.add(helpItem);
        settingsListItems.add(inviteContactsItem);
        settingsListItems.add(logoutItem);

        settingsListAdapter.addAll(settingsListItems);
        settingsListView.setAdapter(settingsListAdapter);

        setUpListViewOnItemClickListener();

    }

    private void setUpListViewOnItemClickListener() {
        settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SettingsItemType type = settingsListItems.get(position).getType();

                if (type == SettingsItemType.PROFILE) {
                    startActivity(UserProfileActivity.intent(getActivity()));
                } else if (type == SettingsItemType.LOCATION) {
                    startActivity(FTXLocationActivity.intent(getActivity()));
                } else if (type == SettingsItemType.INVITE_CONTACTS) {
                    startActivity(InviteContactsActivity.intent(getActivity()));
                } else if (type == SettingsItemType.HELP) {
                    // TODO
                } else if (type == SettingsItemType.LOGOUT) {
                    logout(mainFirebaseRef.getAuth());
                }
            }
        });
    }

    /**
     * Unauthenticate from Firebase and from providers where necessary.
     * @param auth
     */
    private void logout(AuthData auth) {
        this.authData = auth;
        if (this.authData != null) {
            /* logout of Firebase */
            mainFirebaseRef.unauth();
            /* Logout of any of the Frameworks. This step is optional, but ensures the user is not logged into
             * Facebook/Google+ after logging out of Firebase. */
            if (this.authData.getProvider().equals("facebook")) {
                /* Logout from Facebook */
                Session session = Session.getActiveSession();
                if (session != null) {
                    if (!session.isClosed()) {
                        session.closeAndClearTokenInformation();
                    }
                } else {
                    session = new Session(getActivity());
                    Session.setActiveSession(session);
                    session.closeAndClearTokenInformation();
                }
            } else if (this.authData.getProvider().equals("google")) {
                /* Logout from Google+ */
                if (googleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(googleApiClient);
                    googleApiClient.disconnect();
                }
            }
        }
        this.authData = null;
        currentUserPreference.delete();
        startActivity(LoginActivity.intent(getActivity()));
    }

}
