package com.project.boostcamp.staffdinner.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.project.boostcamp.publiclibrary.api.DataReceiver;
import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.domain.ClientDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.staffdinner.GlideApp;
import com.project.boostcamp.staffdinner.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends AppCompatActivity {
    @BindView(R.id.image_background) ImageView imageBackground;
    @BindView(R.id.edit_name) EditText editName;
    @BindView(R.id.edit_phone) EditText editPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        GlideApp.with(this)
                .load(R.drawable.green_background)
                .centerCrop()
                .into(imageBackground);

        String id = SharedPreperenceHelper.getInstance(this).getLoginId();
        int type = SharedPreperenceHelper.getInstance(this).getLoginType();
        RetrofitClient.getInstance().getUserInformation(id, type, userInformReceiver);
    }

    private final DataReceiver<ClientDTO> userInformReceiver = new DataReceiver<ClientDTO>() {
        @Override
        public void onReceive(ClientDTO data) {
            editName.setText(data.getName());
            editPhone.setText(data.getPhone());
        }

        @Override
        public void onFail() {
        }
    };

    @OnClick(R.id.button_save)
    public void doSave() {
        finish();
    }
}
