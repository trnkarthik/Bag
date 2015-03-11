package com.getmebag.bag.ftx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.getmebag.bag.R;
import com.getmebag.bag.base.BagBaseFragment;
import com.getmebag.bag.login.LoginActivity;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A placeholder fragment containing a simple view.
 */
public class FTXTutorialFragment extends BagBaseFragment implements View.OnClickListener {

    //Fragments to be added to the viewPager
    @Inject FTXSlideOneFragment ftxSlideOneFragment;
    @Inject FTXSlideTwoFragment ftxSlideTwoFragment;
    @Inject FTXSlideThreeFragment ftxSlideThreeFragment;

    FTXTutorialMainPagerAdapter ftxTutorialMainPagerAdapter;
    ViewPager ftxTurorialViewPager;
    CirclePageIndicator ftxTutorialCirclePageIndicator;

    ArrayList<Fragment> ftxTutorialPageDetails;

    @InjectView(R.id.ftx_sign_up)
    Button ftxSignUp;

    @InjectView(R.id.ftx_log_in)
    Button ftxLogIn;

    public FTXTutorialFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ftxtutorial, container, false);

        ButterKnife.inject(this, rootView);

        //setUp buttons
        ftxSignUp.setOnClickListener(this);
        ftxLogIn.setOnClickListener(this);

        initializePageDetails();
        setUpPageAdapter();
        setUpViewPager(rootView);
        setUpCirclePageIndicator(rootView);

        return rootView;
    }

    private void setUpCirclePageIndicator(View rootView) {
        ftxTutorialCirclePageIndicator =
                (CirclePageIndicator) rootView.findViewById(R.id.ftx_tutorial_indicator);
        ftxTutorialCirclePageIndicator.setViewPager(ftxTurorialViewPager);
    }

    private void setUpViewPager(View rootView) {
        ftxTurorialViewPager = (ViewPager) rootView.findViewById(R.id.ftx_tutorial_pager);
        ftxTurorialViewPager.setAdapter(ftxTutorialMainPagerAdapter);
    }

    private void setUpPageAdapter() {
        ftxTutorialMainPagerAdapter = new FTXTutorialMainPagerAdapter(getFragmentManager());
        ftxTutorialMainPagerAdapter.setPageDetails(ftxTutorialPageDetails);
    }

    private void initializePageDetails() {
        ftxTutorialPageDetails = new ArrayList<>();
        ftxTutorialPageDetails.add(ftxSlideOneFragment);
        ftxTutorialPageDetails.add(ftxSlideTwoFragment);
        ftxTutorialPageDetails.add(ftxSlideThreeFragment);
    }

    @Override
    public void onClick(View v) {
        startActivity(LoginActivity.intent(getActivity()));
        getActivity().finish();
    }
}
