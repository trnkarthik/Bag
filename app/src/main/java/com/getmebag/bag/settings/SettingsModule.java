package com.getmebag.bag.settings;

import com.getmebag.bag.androidspecific.AndroidModule;
import com.getmebag.bag.ftx.UIFTXModule;
import com.getmebag.bag.userprofile.UserProfileActivity;
import com.getmebag.bag.userprofile.UserProfileFragment;

import dagger.Module;

/**
 * Created by karthiktangirala on 4/5/15.
 */
@Module(
        includes = {
                AndroidModule.class,
                UIFTXModule.class,
        },
        injects = {
                SettingsActivity.class,
                SettingsFragment.class,
                UserProfileActivity.class,
                UserProfileFragment.class,
                SettingsListAdapter.class,
        },
        complete = false
)
public class SettingsModule {
}
