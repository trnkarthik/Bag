package com.getmebag.bag.ftx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getmebag.bag.R;
import com.getmebag.bag.base.BagBaseFragment;

import javax.inject.Inject;

public class FTXSlideOneFragment extends BagBaseFragment {

    @Inject
    public FTXSlideOneFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        ((BagApplication) getActivity().getApplication()).inject(this);
        return inflater.inflate(R.layout.fragment_ftxslide_one, container, false);
    }

}
