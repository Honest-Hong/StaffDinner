package com.project.boostcamp.staffdinnerrestraurant.adapter.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.data.StyleListData;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.inter.ViewHolderEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinnerrestraurant.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Tae Joon on 2017-08-09.
 */

public class StyleVH extends RecyclerView.ViewHolder {
    private CardView cardView;
    @BindView(R.id.text_view) TextView textView;
    private StyleListData data;

    public StyleVH(final View itemView, final ViewHolderEvent<StyleListData> dataEvent) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        cardView = (CardView) itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataEvent.onClick(data, getAdapterPosition());
            }
        });
    }

    public void setupView(StyleListData data) {
        this.data = data;
        textView.setText(data.getName());
        cardView.setBackgroundColor(data.isChecked()
                ? ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary)
                : ContextCompat.getColor(itemView.getContext(), R.color.white));
        textView.setTextColor(data.isChecked()
                ? ContextCompat.getColor(itemView.getContext(), R.color.white)
                : ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
    }
}
