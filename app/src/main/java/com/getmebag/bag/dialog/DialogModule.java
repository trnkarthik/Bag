package com.getmebag.bag.dialog;

import com.getmebag.bag.androidspecific.AndroidModule;
import com.getmebag.bag.annotations.ViewPagerDialogFragmentRef;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by karthiktangirala on 1/2/15.
 */
@Module(
        includes = {
                AndroidModule.class,
        },
        injects = {
                ViewPagerDialogFragment.class,
        },
        complete = false,
        library = true
)
public class DialogModule {

    @Provides
    @Singleton
    @ViewPagerDialogFragmentRef
    ViewPagerDialogFragment getViewPagerDialogFragment() {
        return new ViewPagerDialogFragment();
    }

}

