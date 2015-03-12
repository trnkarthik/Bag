package com.getmebag.bag.base;

import com.getmebag.bag.androidspecific.AndroidModule;

import dagger.Module;

/**
 * Created by karthiktangirala on 1/2/15.
 */
@Module(
        includes = {
                AndroidModule.class,
        },
        injects = {
                BagBaseActivity.class,
                BagBaseFragment.class,
                BagAuthBaseActivity.class,
                BagAuthBaseFragment.class,
                FBBaseFragment.class,
        },
        complete = false
)
public class UIBaseModule {
    // TODO put your application-specific providers here!
}
