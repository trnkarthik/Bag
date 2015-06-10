package com.getmebag.bag.dialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.getmebag.bag.R;
import com.getmebag.bag.app.BagApplication;
import com.getmebag.bag.ftx.FTXSlideOneFragment;
import com.getmebag.bag.ftx.FTXSlideThreeFragment;
import com.getmebag.bag.ftx.FTXSlideTwoFragment;
import com.getmebag.bag.ftx.FTXTutorialMainPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by karthiktangirala on 5/20/15.
 */
public class ViewPagerDialogFragment extends BagBaseDialogFragment {

    //Fragments to be added to the viewPager
    @Inject
    FTXSlideOneFragment ftxSlideOneFragment;
    @Inject
    FTXSlideTwoFragment ftxSlideTwoFragment;
    @Inject
    FTXSlideThreeFragment ftxSlideThreeFragment;

    ArrayList<Fragment> pageDetails;
    CirclePageIndicator circlePageIndicator;
    ViewPager pager;

    @Inject
    public ViewPagerDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((BagApplication) getActivity().getApplication()).inject(this);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.viewpager_dialog_layout, container);

        initializePageDetails();
        pager = setUpViewPager(view);
        setUpCirclePageIndicator(view);

        return view;
    }

    private void setUpCirclePageIndicator(View view) {
        circlePageIndicator =
                (CirclePageIndicator) view.findViewById(R.id.viewpager_dialog_pager_indicator);
        circlePageIndicator.setViewPager(pager);
    }

    private ViewPager setUpViewPager(View view) {
        ViewPager pager = (ViewPager) view.findViewById(R.id.viewpager_dialog_pager);

        //reusing FTXTutorialMainPagerAdapter
        FTXTutorialMainPagerAdapter adapter = new FTXTutorialMainPagerAdapter(getChildFragmentManager());

        adapter.setPageDetails(pageDetails);
        pager.setAdapter(adapter);
        return pager;
    }

    private void initializePageDetails() {
        pageDetails = new ArrayList<>();
        pageDetails.add(ftxSlideOneFragment);
        pageDetails.add(ftxSlideTwoFragment);
        pageDetails.add(ftxSlideThreeFragment);
    }

}
