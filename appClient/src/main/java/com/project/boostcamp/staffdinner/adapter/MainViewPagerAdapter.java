package com.project.boostcamp.staffdinner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.boostcamp.staffdinner.fragment.ApplicationFragment;
import com.project.boostcamp.staffdinner.fragment.ContactFragment;
import com.project.boostcamp.staffdinner.fragment.EstimateFragment;
import com.project.boostcamp.staffdinner.fragment.HomeFragment;

/**
 * Created by Hong Tae Joon on 2017-07-25.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter{
    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return ApplicationFragment.newInstance();
            case 2:
                return EstimateFragment.newInstance();
            case 3:
                return ContactFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
