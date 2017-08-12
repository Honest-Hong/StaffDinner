package com.project.boostcamp.staffdinner.adapter.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinner.R;

/**
 * Created by Hong Tae Joon on 2017-08-12.
 */

public class EmptyVH extends BaseVH<Object> {

    public static EmptyVH vertical(ViewGroup parent, String text) {
        return new EmptyVH(parent, text, R.layout.item_empty_vertical);
    }

    public static EmptyVH horizontal(ViewGroup parent, String text) {
        return new EmptyVH(parent, text, R.layout.item_empty_horizontal);
    }

    private EmptyVH(ViewGroup parent, String text, int layout) {
        super(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false), null);
        TextView textView = (TextView) itemView.findViewById(R.id.text_view);
        textView.setText(text);
    }

    @Override
    public void setupView(Object data) {
    }
}
