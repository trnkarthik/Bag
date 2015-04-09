package com.getmebag.bag.app;

import android.app.Application;

import com.firebase.client.Firebase;
import com.getmebag.bag.androidspecific.AndroidModule;
import com.getmebag.bag.base.UIBaseModule;
import com.getmebag.bag.ftx.UIFTXModule;
import com.getmebag.bag.login.UILoginModule;
import com.getmebag.bag.settings.SettingsModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by karthiktangirala on 1/2/15.
 */
public class BagApplication extends Application {
    private ObjectGraph graph;

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

        graph = ObjectGraph.create(getModules().toArray());
    }

    protected List<Object> getModules() {
        return Arrays.asList(
                new AndroidModule(this),
                new UIBaseModule(),
                new UILoginModule(),
                new SettingsModule(),
                new UIFTXModule()
        );
    }

    public void inject(Object object) {
        graph.inject(object);
    }

}
