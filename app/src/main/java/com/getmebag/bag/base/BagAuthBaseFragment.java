package com.getmebag.bag.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.Session;
import com.facebook.SessionState;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.getmebag.bag.MainActivity;
import com.getmebag.bag.annotations.FireBaseUsersRef;
import com.getmebag.bag.annotations.ForApplication;
import com.getmebag.bag.annotations.MainFireBaseRef;
import com.getmebag.bag.app.BagApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by karthiktangirala on 1/2/15.
 */
public class BagAuthBaseFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public ProgressDialog progressDialog;

    @Inject
    @MainFireBaseRef
    public Firebase mainFirebaseRef;

    @Inject
    @FireBaseUsersRef
    public Firebase firebaseUsersRef;

    @Inject @ForApplication
    public Context appContext;

    /* Data from the authenticated user */
    public AuthData authData;

    /* Request code used to invoke sign in user interactions for Google+ */
    public static final int RC_GOOGLE_LOGIN = 1;

    /* Client used to interact with Google APIs. */
    @Inject
    public GoogleApiClient googleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents us from starting further intents. */
    public boolean googleIntentInProgress;

    /* Track whether the sign-in button has been clicked so that we know to resolve all issues preventing sign-in
     * without waiting. */
    public boolean googleLoginClicked;

    /* Store the connection result from onConnectionFailed callbacks so that we can resolve them when the user clicks
     * sign-in. */
    public ConnectionResult googleConnectionResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Injecting fragment to object graph
        ((BagApplication) getActivity().getApplication()).inject(this);

    }

    /**
     * Show errors to users
     */
    public void showErrorDialog(String message) {
        if (isAdded()) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Error")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    /* Handle any changes to the Facebook session */
    public void onFacebookSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    /* A helper method to resolve the current ConnectionResult error. */
    public void resolveSignInError() {
        if (googleConnectionResult.hasResolution()) {
            try {
                googleIntentInProgress = true;
                if (isAdded()) {
                    googleConnectionResult.startResolutionForResult(getActivity(), RC_GOOGLE_LOGIN);
                }
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                googleIntentInProgress = false;
                googleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(final Bundle bundle) {
        /* Connected with Google API, use this to authenticate with Firebase */
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!googleIntentInProgress) {
            /* Store the ConnectionResult so that we can use it later when the user clicks on the Google+ login button */
            googleConnectionResult = result;

            if (googleLoginClicked) {
                /* The user has already clicked login so we attempt to resolve all errors until the user is signed in,
                 * or they cancel. */
                resolveSignInError();
            } else {
                Log.e(TAG, result.toString());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // ignore
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!googleApiClient.isConnected() && !googleApiClient.isConnecting()) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Map<String, String> options = new HashMap<String, String>();
        if (requestCode == RC_GOOGLE_LOGIN) {
            /* This was a request by the Google API */
            if (resultCode != RESULT_OK) {
                googleLoginClicked = false;
            }
            googleIntentInProgress = false;
            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        } else {
            /* Otherwise, it's probably the request by the Facebook login button, keep track of the session */
            if (Session.getActiveSession() != null) {
                Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
            }
        }
    }

}