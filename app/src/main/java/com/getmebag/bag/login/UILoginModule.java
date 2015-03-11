package com.getmebag.bag.login;

import com.getmebag.bag.androidspecific.AndroidModule;
import com.getmebag.bag.ftx.UIFTXModule;

import dagger.Module;

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
}