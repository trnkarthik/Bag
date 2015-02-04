package com.getmebag.bag.ftx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getmebag.bag.R;
import com.getmebag.bag.base.BagBaseFragment;

import javax.inject.Inject;

public class FTXSlideTwoFragment extends BagBaseFragment {

    @Inject
    public FTXSlideTwoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ftxslide_two, container, false);
    }

}
