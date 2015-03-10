package com.getmebag.bag.androidspecific;

import android.content.Context;

import com.getmebag.bag.annotations.ForApplication;
import com.getmebag.bag.app.BagApplication;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by karthiktangirala on 1/2/15.
 */
@Module(library = true)
public class AndroidModule {
    private final BagApplication application;

    public AndroidModule(BagApplication application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @ForApplication
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    public GoogleApiClient provideGoogleApiClient(@ForApplication Context appContext) {
        return new GoogleApiClient.Builder(appContext)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }

/*
    @Provides @Singleton
    SharedPreferences provideSharedPreferences(Application app) {
        return app.getSharedPreferences("u2020", MODE_PRIVATE);
    }

    Not needed for now.This is how we should 'PROVIDE' android services
    @Provides @Singleton
    LocationManager provideLocationManager() {
        return (LocationManager) application.getSystemService(LOCATION_SERVICE);
    }
*/

}