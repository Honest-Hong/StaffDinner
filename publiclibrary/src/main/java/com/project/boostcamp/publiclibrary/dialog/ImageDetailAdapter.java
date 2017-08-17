package com.project.boostcamp.publiclibrary.dialog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-17.
 */

public class ImageDetailAdapter extends FragmentPagerAdapter {
    private ArrayList<String> images;

    public ImageDetailAdapter(FragmentManager fm, ArrayList<String> images) {
        super(fm);
        this.images = images;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageDetailFragment.newInstance(images.get(position));
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
