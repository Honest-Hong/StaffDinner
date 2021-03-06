package com.project.boostcamp.staffdinner.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.domain.NearAdminDTO;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.staffdinner.GlideApp;
import com.project.boostcamp.staffdinner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Tae Joon on 2017-08-11.
 * 근처 식당 뷰 홀더
 */

public class NearAdminVH extends BaseVH<NearAdminDTO> {
    private final Context context;
    @BindView(R.id.image_view) ImageView imageView;
    @BindView(R.id.text_name) TextView textName;
    @BindView(R.id.text_distance) TextView textDistnace;
    @BindView(R.id.text_style) TextView textStyle;
    public NearAdminVH(Context context, View itemView, final DataEvent<NearAdminDTO> dataEvent) {
        super(itemView, dataEvent);
        this.context = context;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataEvent.onClick(data);
            }
        });
    }

    @Override
    public void setupView(NearAdminDTO data) {
        this.data = data;
        GlideApp.with(context)
                .load(RetrofitClient.getInstance().getAdminImageUrl(data.getAdminId(), data.getAdminType()))
                .centerCrop()
                .into(imageView);
        textName.setText(data.getName());
        textDistnace.setText(context.getString(R.string.distance_km, data.getDistance()));
        textStyle.setText(data.getStyle());
    }
}
