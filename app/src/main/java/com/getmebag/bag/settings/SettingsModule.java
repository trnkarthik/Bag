package com.getmebag.bag.settings;

import com.getmebag.bag.androidspecific.AndroidModule;
import com.getmebag.bag.ftx.UIFTXModule;

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
        },
        complete = false
)
public class SettingsModule {
}
