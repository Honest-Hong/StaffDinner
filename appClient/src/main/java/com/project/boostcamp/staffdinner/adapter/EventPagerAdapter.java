package com.project.boostcamp.staffdinner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.boostcamp.publiclibrary.domain.EventDTO;
import com.project.boostcamp.staffdinner.fragment.EventFragment;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-11.
 */

public class EventPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<EventDTO> events;

    public EventPagerAdapter(FragmentManager fm, ArrayList<EventDTO> events) {
        super(fm);
        this.events = events;
    }

    @Override
    public Fragment getItem(int position) {
        return EventFragment.newInstance(events.get(position));
    }

    @Override
    public int getCount() {
        return events.size();
    }
}
