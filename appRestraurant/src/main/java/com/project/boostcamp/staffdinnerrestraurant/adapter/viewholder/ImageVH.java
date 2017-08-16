package com.project.boostcamp.staffdinnerrestraurant.adapter.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.object.BaseVH;
import com.project.boostcamp.publiclibrary.util.BitmapHelper;
import com.project.boostcamp.publiclibrary.util.FileHelper;
import com.project.boostcamp.publiclibrary.util.Logger;
import com.project.boostcamp.staffdinnerrestraurant.GlideApp;
import com.project.boostcamp.staffdinnerrestraurant.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hong Tae Joon on 2017-08-16.
 */

public class ImageVH extends BaseVH<String> {
    private Context context;
    @BindView(R.id.image_view) ImageView imageView;
    public ImageVH(View itemView, DataEvent<String> dataEvent) {
        super(itemView, dataEvent);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setupView(String data) {
        this.data = data;
        Bitmap bitmap = BitmapFactory.decodeFile(BitmapHelper.resizeImage(context, data, 512, 512).getAbsolutePath());
        imageView.setImageBitmap(bitmap);
    }

    @OnClick(R.id.button_delete)
    public void doDelete() {
        dataEvent.onClick(data);
    }
}
