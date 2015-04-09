package com.getmebag.bag.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.Session;
import com.firebase.client.AuthData;
import com.getmebag.bag.R;
import com.getmebag.bag.androidspecific.prefs.CustomObjectPreference;
import com.getmebag.bag.annotations.CurrentUserPreference;
import com.getmebag.bag.base.BagAuthBaseFragment;
import com.getmebag.bag.login.LoginActivity;
import com.getmebag.bag.model.BagUser;
import com.google.android.gms.plus.Plus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
* Created by karthiktangirala on 4/5/15.
*/
public class SettingsFragment extends BagAuthBaseFragment {

    @InjectView(R.id.logout)
    Button logoutButton;

    @Inject @CurrentUserPreference
    CustomObjectPreference<BagUser> currentUserPreference;

    @Inject
    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, rootView);

        setUpLogoutButton();

        return rootView;
    }

    private void setUpLogoutButton() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(mainFirebaseRef.getAuth());
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
