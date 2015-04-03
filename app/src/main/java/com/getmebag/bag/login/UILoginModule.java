package com.getmebag.bag.login;

import android.content.SharedPreferences;

import com.getmebag.bag.MainActivity;
import com.getmebag.bag.androidspecific.AndroidModule;
import com.getmebag.bag.androidspecific.prefs.CustomObjectPreference;
import com.getmebag.bag.annotations.CurrentUser;
import com.getmebag.bag.annotations.CurrentUserPreference;
import com.getmebag.bag.ftx.UIFTXModule;
import com.getmebag.bag.model.BagUser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by karthiktangirala on 1/2/15.
 */
@Module(
        includes = {
                AndroidModule.class,
                UIFTXModule.class,
        },
        injects = {
                LoginActivity.class,
                LoginFragment.class,
                MainActivity.class,
                MainActivity.MainFragment.class,
        },
        complete = false
)
public class UILoginModule {
    // TODO put your application-specific providers here!

    @Provides
    @Singleton
    @CurrentUserPreference
    CustomObjectPreference<BagUser> currentUserPreference(SharedPreferences preferences) {
        return new CustomObjectPreference<>(preferences, "bag_current_user", null);
    }

    @Provides
    @Singleton
    @CurrentUser
    BagUser currentUser(@CurrentUserPreference CustomObjectPreference<BagUser> customObjectPreference) {
        return (BagUser) customObjectPreference.get(BagUser.class);
    }


//    @Provides
//    @Singleton
//    ProgressDialog progressDialog(@ForApplication Context app) {
//        ProgressDialog progressDialog = new ProgressDialog(app);
//        progressDialog.setTitle("Loading");
//        progressDialog.setMessage("Authenticating with Firebase...");
//        progressDialog.setCancelable(false);
//        return progressDialog;
//    }

}