package com.getmebag.bag.ftx;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FTXTutorialMainPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> pageDetails;

    public FTXTutorialMainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setPageDetails(ArrayList<Fragment> details) {
        this.pageDetails = new ArrayList<>();
        this.pageDetails.addAll(details);
    }

    @Override
    public Fragment getItem(int position) {
        return pageDetails.get(position);
    }

    @Override
    public int getCount() {
        return pageDetails.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //My tabs doesn't need titles.This is a bad practice though.
        //TODO : Set corresponding tab titles.
        return null;
    }
}
