package com.project.boostcamp.staffdinnerrestraurant.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.boostcamp.staffdinnerrestraurant.ui.fragment.ApplicationFragment;
import com.project.boostcamp.staffdinnerrestraurant.ui.fragment.ContactFragment;
import com.project.boostcamp.staffdinnerrestraurant.ui.fragment.EstimateFragment;

/**
 * Created by Hong Tae Joon on 2017-07-25.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ApplicationFragment.newInstance();
            case 1:
                return EstimateFragment.newInstance();
            case 2:
                return ContactFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
