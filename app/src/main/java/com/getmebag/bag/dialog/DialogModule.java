package com.getmebag.bag.dialog;

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
                BagBaseDialogFragment.class,
                ViewPagerDialogFragment.class,
                LocationZipCodeDialogFragment.class,
                UserNameDialogFragment.class,
                PhoneNumberDialogFragment.class,
                DatePickerDialogFragment.class,
                CommonAlertDialogFragment.class,
        },
        complete = false,
        library = true
)
public class DialogModule {

/*
    @Provides
    @Singleton
    @ViewPagerDialogFragmentRef
    ViewPagerDialogFragment getViewPagerDialogFragment() {
        return new ViewPagerDialogFragment();
    }
*/

}

