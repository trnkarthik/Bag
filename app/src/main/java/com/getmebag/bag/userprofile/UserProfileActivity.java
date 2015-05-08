package com.getmebag.bag.userprofile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.getmebag.bag.MainActivity;
import com.getmebag.bag.R;
import com.getmebag.bag.base.BagAuthBaseActivity;

import javax.inject.Inject;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class UserProfileActivity extends BagAuthBaseActivity {

    @Inject
    UserProfileFragment userProfileFragment;

    public static Intent intent(Context context) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP
                | FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        addFragmentToContentView(savedInstanceState, R.id.container,
                userProfileFragment);
        setActionBarIcon(null);
    }

    @Override
    public void onBackPressed() {
        startActivity(MainActivity.intent(this));
    }
}
