package com.getmebag.bag.ftx;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.getmebag.bag.R;
import com.getmebag.bag.base.BagBaseActivity;

public class FTXTutorialActivity extends BagBaseActivity {

    Fragment ftxTutorialFragment;

    public static Intent intent(Context context) {
        return new Intent(context, FTXTutorialActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ftxTutorialFragment = new FTXTutorialFragment();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftxtutorial);
        addFragmentToContentView(savedInstanceState, R.id.ftx_tutorial_activity_container,
                ftxTutorialFragment);
    }

}
