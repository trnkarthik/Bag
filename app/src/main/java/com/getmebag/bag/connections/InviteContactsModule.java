package com.getmebag.bag.connections;

import com.getmebag.bag.androidspecific.AndroidModule;

import dagger.Module;

/**
 * Created by karthiktangirala on 4/29/15.
 */
@Module(
        includes = {
                AndroidModule.class,
        },
        injects = {
                InviteContactsActivity.class,
                InviteContactsFragment.class
        },
        complete = false,
        library = true
)
public class InviteContactsModule {
}
