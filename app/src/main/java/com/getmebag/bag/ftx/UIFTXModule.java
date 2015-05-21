package com.getmebag.bag.ftx;

import android.content.SharedPreferences;

import com.getmebag.bag.MainActivity;
import com.getmebag.bag.androidspecific.AndroidModule;
import com.getmebag.bag.androidspecific.prefs.BooleanPreference;
import com.getmebag.bag.annotations.IsThisFirstTimeUse;
import com.getmebag.bag.annotations.IsThisLoggedInFirstTimeUse;
import com.getmebag.bag.dialog.DialogModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by karthiktangirala on 1/2/15.
 */
@Module(
        includes = {
                AndroidModule.class,
                DialogModule.class,
        },
        injects = {
                FTXTutorialActivity.class,
                FTXTutorialFragment.class,
                FTXSlideOneFragment.class,
                FTXSlideTwoFragment.class,
                FTXSlideThreeFragment.class,
                FTXLocationActivity.class,
                FTXLocationFragment.class,
                MainActivity.class,
                InitialControllerActivity.class,
        },
        complete = false,
        library = true
)
public class UIFTXModule {

    @Provides
    @Singleton
    @IsThisFirstTimeUse
    BooleanPreference isThisFirstTimeUse(SharedPreferences preferences) {
        return new BooleanPreference(preferences, "bag_is_this_first_time_use", true);
    }

    @Provides
    @Singleton
    @IsThisLoggedInFirstTimeUse
    BooleanPreference isThisLoggedInFirstTimeUse(SharedPreferences preferences) {
        return new BooleanPreference(preferences, "bag_is_this_logged_in_first_time_use", false);
    }

}

