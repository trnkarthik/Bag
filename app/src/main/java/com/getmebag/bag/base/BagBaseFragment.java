package com.getmebag.bag.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.getmebag.bag.app.BagApplication;

import javax.inject.Inject;

/**
 * Created by karthiktangirala on 1/2/15.
 */
public class BagBaseFragment extends Fragment {

    private static final String TAG = "BagBaseFragment";

    @Inject
    public BagBaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Injecting fragment to object graph
        ((BagApplication) getActivity().getApplication()).inject(this);

    }


}