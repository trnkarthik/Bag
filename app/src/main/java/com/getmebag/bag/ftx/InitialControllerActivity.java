package com.getmebag.bag.ftx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.getmebag.bag.R;
import com.getmebag.bag.androidspecific.prefs.BooleanPreference;
import com.getmebag.bag.annotations.IsThisFirstTimeUse;
import com.getmebag.bag.app.BagApplication;
import com.getmebag.bag.login.LoginActivity;

import javax.inject.Inject;

/**
 * Created by karthiktangirala on 3/10/15.
 */
public class InitialControllerActivity extends Activity {

    @Inject @IsThisFirstTimeUse
    BooleanPreference isThisFirstTimeUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Injecting fragment to object graph
        ((BagApplication) getApplication()).inject(this);

        if (isThisFirstTimeUse.get()) {
            showFTX();
        } else {
            showLogin();
        }
    }

    private void showLogin() {
        final Intent intent = LoginActivity.intent(this);
        startActivity(intent);
        this.finish();
    }

    private void showFTX() {
        final Intent intent = FTXTutorialActivity.intent(this);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        this.finish();
    }
}
