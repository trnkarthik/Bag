package com.getmebag.bag.base;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.getmebag.bag.app.BagApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

/**
 * Created by karthiktangirala on 1/2/15.
 */
public class BagAuthBaseFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "BagBaseFragment";

    //FaceBook
    protected UiLifecycleHelper uiHelper;

    //gplus
    public static final int STATE_DEFAULT = 0;
    public static final int STATE_SIGN_IN = 1;
    public static final int STATE_IN_PROGRESS = 2;

    public static final int RC_SIGN_IN = 0;

    public static final int DIALOG_PLAY_SERVICES_ERROR = 0;

    public static final String SAVED_PROGRESS = "gplus_sign_in_progress";

    public int signInProgress;

    public PendingIntent signInIntent;

    public int signInError;

    @Inject
    public
    GoogleApiClient googleApiClient;

    @Inject
    public BagAuthBaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Injecting fragment to object graph
        ((BagApplication) getActivity().getApplication()).inject(this);

        //FaceBook
        uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
        uiHelper.onCreate(savedInstanceState);

        //gplus
        if (savedInstanceState != null) {
            signInProgress = savedInstanceState.getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }

    }

    //FaceBook
    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            onFacebookSessionStateChange(session, session.getState(), null);
        }
    };

    public void onFacebookSessionStateChange(Session session, SessionState state,
                                             Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            //TODO : Get Profile Info and do some FireBase stuff
//            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
            //TODO : Redirect user to Login Activity.
//            getActivity().startActivity(new Intent(getActivity(), FTXTutorialActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed())) {
            onFacebookSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, signInProgress);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);  //fb
        googleSignInOnActivityResult(requestCode, resultCode);     //gplus
    }

    public void googleSignInOnActivityResult(int requestCode, int resultCode) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == Activity.RESULT_OK) {
                    signInProgress = STATE_SIGN_IN;
                } else {
                    signInProgress = STATE_DEFAULT;
                }

                if (!googleApiClient.isConnecting()) {
                    googleApiClient.connect();
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    public void resolveSignInError() {
        if (signInIntent != null) {
            try {
                signInProgress = STATE_IN_PROGRESS;
                getActivity().startIntentSenderForResult(signInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                signInProgress = STATE_SIGN_IN;
                googleApiClient.connect();
            }
        } else {
           //showDialog(DIALOG_PLAY_SERVICES_ERROR);
           //TODO : Show Goolge Play Services Dialog.
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Reaching onConnected means we consider the user signed in.
        Log.i(TAG, "onConnected");
        // Indicate that the sign in process is complete.
        signInProgress = STATE_DEFAULT;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());

        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            // An API requested for GoogleApiClient is not available. You may need to use a
            // second GoogleApiClient to manage the application's optional APIs.
            //TODO : SNR Dialog
        } else if (signInProgress != STATE_IN_PROGRESS) {
            signInIntent = result.getResolution();
            signInError = result.getErrorCode();

            if (signInProgress == STATE_SIGN_IN) {
                resolveSignInError();
            }
        }
        // In this sample we consider the user signed out whenever they do not have
        // a connection to Google Play services.
        onSignedOut();
    }

    public void onSignedOut() {
        // Update the UI to reflect that the user is signed out.
        //TODO : Reirect to Login Activity
//        getActivity().startActivity(new Intent(getActivity(), FTXTutorialActivity.class));
    }

}