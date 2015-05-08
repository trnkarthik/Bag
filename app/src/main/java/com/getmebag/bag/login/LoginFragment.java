package com.getmebag.bag.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.getmebag.bag.MainActivity;
import com.getmebag.bag.R;
import com.getmebag.bag.androidspecific.prefs.BooleanPreference;
import com.getmebag.bag.androidspecific.prefs.CustomObjectPreference;
import com.getmebag.bag.annotations.CurrentUserPreference;
import com.getmebag.bag.annotations.IsThisLoggedInFirstTimeUse;
import com.getmebag.bag.base.BagAuthBaseFragment;
import com.getmebag.bag.model.BagUser;
import com.getmebag.bag.model.CachedUserData;
import com.getmebag.bag.userprofile.UserProfileActivity;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginFragment extends BagAuthBaseFragment {

    private static final String TAG = "LoginFragment";

    @InjectView(R.id.fb_login_button)
    LoginButton fbLoginButton;

    @InjectView(R.id.gplus_sign_in_button)
    SignInButton googleSignInButton;

    @InjectView(R.id.username)
    TextView username;

    @Inject
    @CurrentUserPreference
    CustomObjectPreference<BagUser> currentUserPreference;

    @Inject
    @IsThisLoggedInFirstTimeUse
    BooleanPreference isThisLoggedInFTX;

    BagUser.Builder bagUserBuilder = new BagUser.Builder();
    BagUser bagUser;

    CachedUserData.Builder cachedUserDataBuilder = new CachedUserData.Builder();

    private String gmail;
    private boolean isAuthDataSet = false;

    @Inject
    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setUpFireBase();

        return rootView;
    }

    private void setUpFireBase() {
                /* Create the Firebase ref that is used for all authentication with Firebase */
        mainFirebaseRef = new Firebase(getResources().getString(R.string.main_firebase_url));

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Authenticating with Firebase...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        /* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
         * user and hide hide any login buttons */
        mainFirebaseRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                progressDialog.dismiss();
                setAuthenticatedUser(authData);
            }
        });

    }

    private void setUpFBLoginButton() {
        fbLoginButton.setFragment(this);
        fbLoginButton.setReadPermissions(Arrays.asList("email", "user_birthday"));
        fbLoginButton.setSessionStatusCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState sessionState, Exception exception) {
                onFacebookSessionStateChange(session, sessionState, exception);
            }
        });
        fbLoginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
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

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLoginClicked = true;
                if (!googleApiClient.isConnecting()) {
                    if (googleConnectionResult != null) {
                        resolveSignInError();
                    } else if (googleApiClient.isConnected()) {
                        getGoogleOAuthTokenAndLogin();
                    } else {
                    /* connect API now */
                        Log.d(TAG, "Trying to connect to Google API");
                        googleApiClient.connect();
                    }
                }

            }
        });
    }

    /**
     * Once a user is logged in, take the authData provided from Firebase and "use" it.
     */
    private void setAuthenticatedUser(AuthData authData) {
        if (!isAuthDataSet && authData != null) {
            checkIfUserExistsAndSaveDataInFireBase(
                    appContext.getString(R.string.firebase_users_url) + authData.getUid(),
                    authData);
        } else {
            /* No authenticated user show all the login buttons */
            fbLoginButton.setVisibility(View.VISIBLE);
            googleSignInButton.setVisibility(View.VISIBLE);
            username.setVisibility(View.INVISIBLE);
        }
        this.authData = authData;
    }

    private void checkIfUserExistsAndSaveDataInFireBase(String firebasePath, final AuthData authData) {
        isAuthDataSet = true;
        convertToBagUser(authData);
        Firebase firebase = new Firebase(firebasePath);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    //New Guy
                    isThisLoggedInFTX.set(true);
                    saveDataInFireBase(authData, true);
                } else {
                    //Old Dude
                    isThisLoggedInFTX.set(false);
                    saveDataInFireBase(authData, false);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getActivity(), "Sorry! Result not available at this time!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveDataInFireBase(final AuthData authData, boolean isThisLoggedInFirstTime) {
        //first create user ref
        if (isThisLoggedInFirstTime) {
            firebaseUsersRef.child(authData.getUid())
                    .setValue(bagUser, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError != null) {
                                //Some problem with saving data. Show retry dialog
                                showErrorDialog("System not responding. Please try again");
                                if (mainFirebaseRef != null) {
                                    mainFirebaseRef.unauth();
                                }
                            } else {
                                //Data saved successfully.Launch FTX flow.
                                currentUserPreference.set(bagUser);
                                launchFTXFlow();
                            }
                        }
                    });
        } else {
            firebaseUsersRef.child(authData.getUid())
                    .child(getString(R.string.firebase_cached_user_data_url_part))
                    .setValue(bagUser.getCachedUserData(), new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError != null) {
                                //Some problem with saving data. Show retry dialog
                                showErrorDialog("System not responding. Please try again");
                                if (mainFirebaseRef != null) {
                                    mainFirebaseRef.unauth();
                                }
                            } else {
                                //Data saved successfully.Launch Main screen.
                                currentUserPreference.set(bagUser);
                                launchMainScreen();
                            }
                        }
                    });
        }
    }

    private BagUser convertToBagUser(AuthData authData) {
        Map<String, String> userCachedData = (Map<String, String>) authData.getProviderData().get("cachedUserProfile");

        if (authData.getProvider().equals("google")) {
            bagUserBuilder.setProvider(authData.getProvider())
                    .setToken(authData.getToken())
                    .setProviderId(authData.getProviderData().get("id").toString());

            cachedUserDataBuilder
                    .setAccessToken(nullSafe(authData.getProviderData().get("accessToken")))
                    .setUserName(nullSafe(authData.getProviderData().get("displayName"), userCachedData.get("given_name")))
                    .setFirstName(nullSafe(userCachedData.get("given_name")))
                    .setLastName(nullSafe(userCachedData.get("family_name")))
                    .setGender(nullSafe(userCachedData.get("gender")))
                    .setProfileLink(nullSafe(userCachedData.get("link")))
                    .setProfilePictureURL(nullSafe(userCachedData.get("picture")));
            if (googleApiClient.isConnected()) {
                Person googlePerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
                if (googlePerson != null) {
                    cachedUserDataBuilder.setBirthDate(nullSafe(googlePerson.getBirthday()));
                    cachedUserDataBuilder.setEmail(nullSafe(Plus.AccountApi.getAccountName(googleApiClient)));
                }
            } else {
                cachedUserDataBuilder.setEmail(nullSafe(gmail));
            }
            bagUserBuilder.setCachedUserData(cachedUserDataBuilder.build());
            bagUser = bagUserBuilder.build();

        } else if (authData.getProvider().equals("facebook")) {
            bagUserBuilder.setProvider(authData.getProvider())
                    .setToken(authData.getToken())
                    .setProviderId(authData.getProviderData().get("id").toString());

            cachedUserDataBuilder
                    .setAccessToken(nullSafe(authData.getProviderData().get("accessToken")))
                    .setUserName(nullSafe(authData.getProviderData().get("displayName")))
                    .setEmail(nullSafe(authData.getProviderData().get("email")))
                    .setFirstName(nullSafe(userCachedData.get("first_name")))
                    .setLastName(nullSafe(userCachedData.get("last_name")))
                    .setGender(nullSafe(userCachedData.get("gender")))
                    .setBirthDate(nullSafe(userCachedData.get("birthday")))
                    .setProfileLink(nullSafe(userCachedData.get("link")))
                    .setProfilePictureURL(parseProfilePicture(userCachedData.get("picture")));
            bagUserBuilder.setCachedUserData(cachedUserDataBuilder.build());
            bagUser = bagUserBuilder.build();

        } else {
            bagUser = null;
        }
        return bagUser;
    }

    private String nullSafe(Object object1, Object object2) {
        if (object1 != null) {
            return object1.toString();
        }
        if (object2 != null) {
            return object2.toString();
        }
        return null;
    }

    private String parseProfilePicture(Object profilePic) {
        Map<String, Object> profilePicJson = (Map<String, Object>) profilePic;
        Map<String, Object> data = (Map<String, Object>) profilePicJson.get("data");
        String url = (String) data.get("url");
        return nullSafe(url);
    }

    private String nullSafe(Object object) {
        if (object != null) {
            return object.toString();
        }
        return null;
    }

    /**
     * Utility class for authentication results
     */
    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            progressDialog.dismiss();
            Log.i(TAG, provider + " auth successful");
            setAuthenticatedUser(authData);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            progressDialog.dismiss();
            showErrorDialog(firebaseError.toString());
        }
    }

    private void launchMainScreen() {
        if (isAdded()) {
            startActivity(MainActivity.intent((appContext)));
        }
    }

    private void launchFTXFlow() {
        if (isAdded()) {
            startActivity(UserProfileActivity.intent((appContext)));
        }
    }

    public void getGoogleOAuthTokenAndLogin() {
//        progressDialog.show();
        /* Get OAuth token in Background */
        if (isAdded()) {
            AsyncTask<Void, Void, String> task = new GoogleOauthTokenAsyncTask();
            task.execute();
        }
    }

    @Override
    public void onFacebookSessionStateChange(Session session, SessionState state, Exception exception) {
        super.onFacebookSessionStateChange(session, state, exception);
        if (state.isOpened()) {
            progressDialog.show();
            mainFirebaseRef.authWithOAuthToken("facebook", session.getAccessToken(), new AuthResultHandler("facebook"));
            Log.d("karu", "fb : " + session.getAccessToken());
        } else if (state.isClosed()) {
            /* Logged out of Facebook and currently authenticated with Firebase using Facebook, so do a logout */
            if (this.authData != null && this.authData.getProvider().equals("facebook")) {
                mainFirebaseRef.unauth();
                setAuthenticatedUser(null);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        getGoogleOAuthTokenAndLogin();
    }

    private class GoogleOauthTokenAsyncTask extends AsyncTask<Void, Void, String> {
        String errorMessage = null;

        @Override
        protected String doInBackground(Void... params) {
            String token = null;
            if (getActivity() != null) {
                try {
                    String scope = String.format("oauth2:%s", Scopes.PROFILE, Scopes.PLUS_ME);
                    gmail = Plus.AccountApi.getAccountName(googleApiClient);
                    cachedUserDataBuilder.setEmail(gmail);
                    token = GoogleAuthUtil.getToken(getActivity(), gmail, scope);
                } catch (IOException transientEx) {
                /* Network or server error */
                    Log.e(TAG, "Error authenticating with Google: " + transientEx);
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    Log.w(TAG, "Recoverable Google OAuth error: " + e.toString());
                /* We probably need to ask for permissions, so start the intent if there is none pending */
                    if (!googleIntentInProgress) {
                        googleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        startActivityForResult(recover, RC_GOOGLE_LOGIN);
                    }
                } catch (GoogleAuthException authEx) {
                /* The call is not ever expected to succeed assuming you have already verified that
                 * Google Play services is installed. */
                    Log.e(TAG, "Error authenticating with Google: " + authEx.getMessage(), authEx);
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }
            }
            return token;
        }

        @Override
        protected void onPostExecute(String token) {
            googleLoginClicked = false;
            if (token != null) {
                /* Successfully got OAuth token, now login with Google */
                Log.d("karu", token);
                mainFirebaseRef.authWithOAuthToken("google", token, new AuthResultHandler("google"));
            } else if (errorMessage != null) {
                progressDialog.dismiss();
                showErrorDialog(errorMessage);
            }
        }
    }
}
