package com.getmebag.bag.ftx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getmebag.bag.R;
import com.getmebag.bag.base.BagBaseFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class FTXSlideThreeFragment extends BagBaseFragment {

    private static final String TAG = "FTXSlideThreeFragment";

    @Inject
    public FTXSlideThreeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_ftxslide_three, container, false);

        ButterKnife.inject(this, rootView);
        return rootView;
    }

}
