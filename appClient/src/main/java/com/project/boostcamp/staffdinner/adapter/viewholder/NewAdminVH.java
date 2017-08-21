package com.project.boostcamp.staffdinner.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.domain.NewAdminDTO;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinner.GlideApp;
import com.project.boostcamp.staffdinner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Tae Joon on 2017-08-12.
 */

public class NewAdminVH extends BaseVH<NewAdminDTO> {
    @BindView(R.id.image_view) ImageView imageView;
    @BindView(R.id.text_name) TextView textName;
    @BindView(R.id.text_style) TextView textStyle;
    private Context context;

    public NewAdminVH(View itemView, final DataEvent<NewAdminDTO> dataEvent) {
        super(itemView, dataEvent);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataEvent.onClick(data);
            }
        });
    }

    @Override
    public void setupView(NewAdminDTO data) {
        this.data = data;
        GlideApp.with(context)
                .load(RetrofitClient.getInstance().getAdminImageUrl(data.getId(), data.getType()))
                .centerCrop()
                .into(imageView);
        textName.setText(data.getName());
        textStyle.setText(data.getStyle());
    }
}
