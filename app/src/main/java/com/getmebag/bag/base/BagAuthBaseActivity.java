package com.getmebag.bag.base;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.getmebag.bag.app.BagApplication;

import javax.inject.Inject;

/**
 * Created by karthiktangirala on 1/2/15.
 */
public class BagAuthBaseActivity extends ActionBarActivity {

    @Inject
    public
    BagAuthBaseFragment bagAuthBaseFragment;

    /**
     * Created by karthiktangirala on 9/30/14.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Injecting fragment to object graph
        ((BagApplication) getApplication()).inject(this);

        addFragmentToContentView(savedInstanceState, 0, bagAuthBaseFragment);
    }

    public void setActionBarIcon(Drawable icon) {
        if (icon != null) {
            getSupportActionBar().setIcon(icon);
        } else {
            getSupportActionBar().setIcon(
                    new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        }
    }

    public void addFragmentToContentView(Bundle savedInstanceState, int containerId,
                                         Fragment fragment) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(containerId, fragment)
                    .commit();
        }
    }

}
