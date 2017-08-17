package com.project.boostcamp.staffdinnerrestraurant.activity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.boostcamp.publiclibrary.api.DataReceiver;
import com.project.boostcamp.publiclibrary.api.RetrofitAdmin;
import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.data.RequestType;
import com.project.boostcamp.publiclibrary.domain.AdminDTO;
import com.project.boostcamp.publiclibrary.domain.ResultIntDTO;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.util.BitmapHelper;
import com.project.boostcamp.publiclibrary.util.FileHelper;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.staffdinnerrestraurant.GlideApp;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.adapter.ImageRecyclerAdapter;
import com.project.boostcamp.staffdinnerrestraurant.dialog.ImageModeDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends AppCompatActivity {
    @BindView(R.id.recycler_image) RecyclerView recyclerImage;
    @BindView(R.id.edit_name) MaterialEditText editName;
    @BindView(R.id.edit_phone) MaterialEditText editPhone;
    @BindView(R.id.image_admin_image) ImageView imageAdmin;
    @BindView(R.id.text_admin_style) TextView textStyle;
    @BindView(R.id.edit_menu) MaterialEditText editMenu;
    @BindView(R.id.edit_menu_cost) MaterialEditText editCost;
    private ImageRecyclerAdapter imageRecyclerAdapter;
    private String imageTitlePath = "";
    private ArrayList<String> images;
    private AdminDTO admin;
    private int saveBonusImageCount = 0;
//    private ImagePicker imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        setupView();
        String adminId = SharedPreperenceHelper.getInstance(this).getLoginId();
        int adminType = SharedPreperenceHelper.getInstance(this).getLoginType();
        RetrofitAdmin.getInstance().getAdminInformation(adminId, adminType, adminDataReceiver);
    }

    private void setupView() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.account_activity_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        images = new ArrayList<>();
        imageRecyclerAdapter = new ImageRecyclerAdapter(this, uriDataEvent);
        recyclerImage.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerImage.setAdapter(imageRecyclerAdapter);
    }

    private DataEvent<String> uriDataEvent = new DataEvent<String>() {
        @Override
        public void onClick(String data) {
            int index = images.indexOf(data);
            images.remove(index);
            imageRecyclerAdapter.notifyItemRemoved(index);
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

    @OnClick(R.id.image_admin_image)
    public void selectImage() {
        ImageModeDialog.newInstace(new ImageModeDialog.ImageSelector() {
            @Override
            public void onSelect(int mode) {
                if(mode == ImageModeDialog.MODE_CAMERA) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, RequestType.REQUEST_CAMERA);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, RequestType.REQUEST_PICUTRE);
                }
            }
        }).show(getSupportFragmentManager(), null);
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
                    String path = FileHelper.getPath(this, data.getData());
                    if(!images.contains(path)) {
                        if(images.size() >= 10) {
                            Toast.makeText(this, "이미지는 최대 10장입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            images.add(path);
                        }
                    }
                } else if(data.getClipData() != null) {
                    ClipData clipData = data.getClipData();
                    for(int i=0; i<clipData.getItemCount(); i++) {
                        String path = FileHelper.getPath(this, clipData.getItemAt(i).getUri());
                        if(!images.contains(path)) {
                            if(images.size() >= 10) {
                                Toast.makeText(this, "이미지는 최대 10장입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                images.add(path);
                            }
                        }
                    }
                }
                imageRecyclerAdapter.setData(images);
            }
        } else if(requestCode == RequestType.REQUEST_PICUTRE) {
            if(resultCode == RESULT_OK) {
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    imageAdmin.setImageBitmap(photo);
                    imageTitlePath = FileHelper.getPath(this, data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private DataReceiver<AdminDTO> adminDataReceiver = new DataReceiver<AdminDTO>() {
        @Override
        public void onReceive(AdminDTO data) {
            admin = data;
            editName.setText(data.getName());
            editPhone.setText(data.getPhone());
            textStyle.setText(data.getStyle());
            editMenu.setText(data.getMenu());
            editCost.setText(getResources().getQuantityString(R.plurals.menu_cost, data.getCost(), data.getCost()));
            GlideApp.with(AccountActivity.this)
                    .load(RetrofitClient.getInstance().getAdminImageUrl(data.getId(), data.getType()))
                    .into(imageAdmin);
        }

        @Override
        public void onFail() {
            finish();
        }
    };

    @OnClick(R.id.button_save)
    public void doSave() {
        RetrofitAdmin.getInstance().setAdminInformation(admin.getId(), admin.getType(), admin, setInformationDataReceiver);
    }

    private DataReceiver<ResultIntDTO> setInformationDataReceiver = new DataReceiver<ResultIntDTO>() {
        @Override
        public void onReceive(ResultIntDTO data) {
            if(!imageTitlePath.equals("")) {
                RetrofitAdmin.getInstance().setImage(admin.getId(), admin.getType(), BitmapHelper.resizeImage(AccountActivity.this, imageTitlePath, 512, 512), setImageDataReceiver);
            } else if(images.size() > 0) {
                saveBonusImage();
            } else {
                finish();
            }
        }

        @Override
        public void onFail() {
            Toast.makeText(AccountActivity.this, "정보 저장 실패", Toast.LENGTH_SHORT).show();
        }
    };

    private DataReceiver<ResultIntDTO> setImageDataReceiver = new DataReceiver<ResultIntDTO>() {
        @Override
        public void onReceive(ResultIntDTO data) {
            saveBonusImage();
        }

        @Override
        public void onFail() {
            Toast.makeText(AccountActivity.this, "타이틀 이미지 저장 실패", Toast.LENGTH_SHORT).show();
        }
    };

    private void saveBonusImage() {
        saveBonusImageCount = 0;
        for(int i=0; i<images.size(); i++) {
            RetrofitAdmin.getInstance().setBonusImage(admin.getId(), admin.getType(), i, BitmapHelper.resizeImage(this, images.get(i), 512, 512), setBonusImageDataReceiver);
        }
    }

    private DataReceiver<ResultIntDTO> setBonusImageDataReceiver = new DataReceiver<ResultIntDTO>() {
        @Override
        public void onReceive(ResultIntDTO data) {
            saveBonusImageCount++;
            if(saveBonusImageCount == images.size()) {
                finish();
            }
        }

        @Override
        public void onFail() {
            Toast.makeText(AccountActivity.this, "보너스 이미지 저장 실패", Toast.LENGTH_SHORT).show();
        }
    };
}
