package com.project.boostcamp.staffdinnerrestraurant.activity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.project.boostcamp.publiclibrary.data.RequestType;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.util.FileHelper;
import com.project.boostcamp.publiclibrary.util.Logger;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.adapter.ImageRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends AppCompatActivity {
    @BindView(R.id.recycler_image) RecyclerView recyclerImage;
    private ImageRecyclerAdapter imageRecyclerAdapter;
    private ArraySet<String> images;
//    private ImagePicker imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        setupView();
    }

    private void setupView() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.account_activity_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        images = new ArraySet<>();
        imageRecyclerAdapter = new ImageRecyclerAdapter(this, uriDataEvent);
        recyclerImage.setHasFixedSize(true);
        recyclerImage.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerImage.setAdapter(imageRecyclerAdapter);
    }

    private DataEvent<String> uriDataEvent = new DataEvent<String>() {
        @Override
        public void onClick(String data) {
        }
    };

    @OnClick(R.id.button_add_image)
    public void addMenuImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), RequestType.REQUEST_PICUTRES);
//        ImagePicker.create(this)
//                .multi()
//                .start(RequestType.REQUEST_PICUTRES);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RequestType.REQUEST_PICUTRES && resultCode == RESULT_OK && data != null) {
//            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
//            for(Image image: images) {
//                Logger.i(this, "onActivityResult", image.getPath());
//            }
//        }
        if(requestCode == RequestType.REQUEST_PICUTRES) {
            if(resultCode == RESULT_OK) {
                if(data.getData() != null) {
                    images.add(FileHelper.getPath(this, data.getData()));
                } else if(data.getClipData() != null) {
                    ClipData clipData = data.getClipData();
                    for(int i=0; i<clipData.getItemCount(); i++) {
                        images.add(FileHelper.getPath(this, clipData.getItemAt(i).getUri()));
                    }
                }
                imageRecyclerAdapter.setData(images);
            }
        }
    }
}
