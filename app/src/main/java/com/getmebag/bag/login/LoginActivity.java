package com.getmebag.bag.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.getmebag.bag.R;
import com.getmebag.bag.androidspecific.prefs.BooleanPreference;
import com.getmebag.bag.annotations.IsThisFirstTimeUse;
import com.getmebag.bag.base.BagAuthBaseActivity;

import javax.inject.Inject;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class LoginActivity extends BagAuthBaseActivity {

    @Inject
    LoginFragment loginFragment;

    @Inject @IsThisFirstTimeUse
    BooleanPreference isThisFirstTimeUse;

    public static Intent intent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP
                | FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addFragmentToContentView(savedInstanceState, R.id.login_container,
                loginFragment);
        isThisFirstTimeUse.set(false);
    }

}
