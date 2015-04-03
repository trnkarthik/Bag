package com.getmebag.bag.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.getmebag.bag.app.BagApplication;

import javax.inject.Inject;

/**
 * Created by karthiktangirala on 1/2/15.
 */
public class BagAuthBaseActivity extends FragmentActivity {

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

    public void addFragmentToContentView(Bundle savedInstanceState, int containerId,
                                         Fragment fragment) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(containerId, fragment)
                    .commit();
        }
    }

}
