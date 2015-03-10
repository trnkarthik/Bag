package com.getmebag.bag.login;

import android.os.Bundle;

import com.getmebag.bag.R;
import com.getmebag.bag.base.BagAuthBaseActivity;

import javax.inject.Inject;

public class LoginActivity extends BagAuthBaseActivity {

    @Inject
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addFragmentToContentView(savedInstanceState, R.id.login_container,
                loginFragment);
    }

}
