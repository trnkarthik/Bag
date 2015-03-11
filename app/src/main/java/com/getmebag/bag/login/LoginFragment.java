package com.getmebag.bag.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.getmebag.bag.R;
import com.getmebag.bag.base.BagAuthBaseFragment;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.Plus;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginFragment extends BagAuthBaseFragment {

    private static final String TAG = "LoginFragment";

    @InjectView(R.id.fb_login_button)
    LoginButton loginBtn;
    @InjectView(R.id.gplus_sign_in_button)
    SignInButton signInButton;
    @InjectView(R.id.username)
    TextView username;

    @Inject
    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.inject(this, rootView);

        setUpFBLoginButton();
        setUpGPlusButton();

        return rootView;
    }

    private void setUpFBLoginButton() {
        loginBtn.setFragment(this);
        loginBtn.setReadPermissions(Arrays.asList("email"));
        loginBtn.setSessionStatusCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState sessionState, Exception exception) {
                onFacebookSessionStateChange(session, sessionState, exception);
            }
        });
        loginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    username.setText("You are currently logged in as " + user.getName());
                } else {
                    username.setText("You are not logged in.");
                }
            }
        });
    }

    private void setUpGPlusButton() {
        googleApiClient.registerConnectionCallbacks(this);
        googleApiClient.registerConnectionFailedListener(this);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!googleApiClient.isConnected()) {
                    if (!googleApiClient.isConnecting()) {
                        switch (v.getId()) {
                            case R.id.gplus_sign_in_button:
                                username.setText("Signing In...");
                                resolveSignInError();
                                break;
                        }
                    }
                }

            }
        });
    }

    @Override
    public void onConnected(Bundle connectionHint) {
//        Person currentUser = Plus.PeopleApi.getCurrentPerson(googleApiClient);
        String currentUser = Plus.AccountApi.getAccountName(googleApiClient);
        username.setText("Logged in as : " + currentUser);
//        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void onFacebookSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            //TODO : Get Profile Info and do some FireBase stuff
//            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

}
