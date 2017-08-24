package com.project.boostcamp.staffdinner.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinner.R;

/**
 * Created by Hong Tae Joon on 2017-08-17.
 * 문자열 선택 뷰 홀더
 */

public class SelectStringVH extends BaseVH<String> {
    private final TextView textView;
    public SelectStringVH(View itemView, final DataEvent<String> dataEvent) {
        super(itemView, dataEvent);
        textView = itemView.findViewById(R.id.text_view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataEvent.onClick(data);
            }
        });
    }

    @Override
    public void setupView(String data) {
        this.data = data;
        textView.setText(data);
    }
}
