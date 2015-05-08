package com.getmebag.bag.ftx;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.getmebag.bag.R;
import com.getmebag.bag.base.BagAuthBaseActivity;

import javax.inject.Inject;

public class FTXLocationActivity extends BagAuthBaseActivity {

    @Inject
    FTXLocationFragment ftxLocationFragment;

    public static Intent intent(Context context) {
        Intent intent = new Intent(context, FTXLocationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftxlocation);
        addFragmentToContentView(savedInstanceState, R.id.container,
                ftxLocationFragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
