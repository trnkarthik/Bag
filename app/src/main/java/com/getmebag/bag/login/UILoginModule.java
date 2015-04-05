package com.getmebag.bag.login;

import com.getmebag.bag.MainActivity;
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
                MainActivity.class,
                MainActivity.MainFragment.class,
        },
        complete = false
)
public class UILoginModule {
    // TODO put your application-specific providers here!

//    @Provides
//    @Singleton
//    ProgressDialog progressDialog(@ForApplication Context app) {
//        ProgressDialog progressDialog = new ProgressDialog(app);
//        progressDialog.setTitle("Loading");
//        progressDialog.setMessage("Authenticating with Firebase...");
//        progressDialog.setCancelable(false);
//        return progressDialog;
//    }

}