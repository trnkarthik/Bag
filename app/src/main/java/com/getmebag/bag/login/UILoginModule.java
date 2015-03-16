package com.getmebag.bag.login;

import android.content.SharedPreferences;

import com.getmebag.bag.androidspecific.AndroidModule;
import com.getmebag.bag.androidspecific.prefs.StringPreference;
import com.getmebag.bag.annotations.CurrentAuthProvider;
import com.getmebag.bag.ftx.UIFTXModule;

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
        },
        complete = false
)
public class UILoginModule {
    // TODO put your application-specific providers here!

    @Provides
    @Singleton
    @CurrentAuthProvider
    StringPreference currentAuthProvider(SharedPreferences preferences) {
        return new StringPreference(preferences, "bag_current_auth_provider", null);
    }

}