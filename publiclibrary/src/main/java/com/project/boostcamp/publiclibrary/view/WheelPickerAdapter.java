package com.project.boostcamp.publiclibrary.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-07-27.
 */

public abstract class WheelPickerAdapter<T> {
    protected ArrayList<T> data;
    public abstract View getView(LayoutInflater inflater, ViewGroup parent, int pos);

    public void setData(ArrayList<T> data) {
        this.data = data;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public int getCount() {
        return data != null
                ? data.size()
                : 0;
    }
    public T getItem(int pos) {
        return data != null
                ? data.get(pos)
                : null;
    }
}

