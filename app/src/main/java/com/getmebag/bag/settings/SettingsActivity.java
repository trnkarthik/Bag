package com.getmebag.bag.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.getmebag.bag.R;
import com.getmebag.bag.base.BagAuthBaseActivity;

import javax.inject.Inject;

public class SettingsActivity extends BagAuthBaseActivity {

    @Inject
    SettingsFragment settingsFragment;

    public static Intent intent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        addFragmentToContentView(savedInstanceState, R.id.container,
                settingsFragment);
    }

}
