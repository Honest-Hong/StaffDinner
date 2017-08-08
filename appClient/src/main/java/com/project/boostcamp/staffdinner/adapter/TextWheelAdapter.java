package com.project.boostcamp.staffdinner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.publiclibrary.view.WheelPickerAdapter;

/**
 * Created by Hong Tae Joon on 2017-07-27.
 */

public class TextWheelAdapter extends WheelPickerAdapter<String> {
    @Override
    public View getView(LayoutInflater inflater, ViewGroup parent, int pos) {
        TextView textView = (TextView)inflater.inflate(R.layout.layout_wheel_item, parent, false);
        textView.setText(data.get(pos));
        return textView;
    }
}
