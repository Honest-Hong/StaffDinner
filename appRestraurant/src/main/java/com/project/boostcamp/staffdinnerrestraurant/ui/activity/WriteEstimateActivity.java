package com.project.boostcamp.staffdinnerrestraurant.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.boostcamp.publiclibrary.api.RetrofitAdmin;
import com.project.boostcamp.publiclibrary.data.AdminApplication;
import com.project.boostcamp.publiclibrary.dialog.DialogResultListener;
import com.project.boostcamp.publiclibrary.dialog.MyAlertDialog;
import com.project.boostcamp.publiclibrary.domain.EstimateAddDTO;
import com.project.boostcamp.publiclibrary.domain.ResultIntDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.publiclibrary.util.TimeHelper;
import com.project.boostcamp.staffdinnerrestraurant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteEstimateActivity extends AppCompatActivity implements DialogResultListener {
    private AdminApplication application;
    @BindView(R.id.text_title_application) TextView textApplicationTitle;
    @BindView(R.id.text_message) TextView textMessage;
    @BindView(R.id.text_number) TextView textNumber;
    @BindView(R.id.text_time) TextView textTime;
    @BindView(R.id.text_distance) TextView textDistance;
    @BindView(R.id.text_style) TextView textStyle;
    @BindView(R.id.text_menu) TextView textMenu;
    @BindView(R.id.edit_message) EditText editMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_estimate);

        ButterKnife.bind(this);
        if(getIntent() != null) {
            application = getIntent().getParcelableExtra(AdminApplication.class.getName());
            setupToolbar();
            setupView();
        }
    }

    private void setupView() {
        textApplicationTitle.setText(getString(R.string.text_application_name, application.getWriterName()));
        textMessage.setText(application.getTitle());
        textNumber.setText(getString(R.string.people_count, application.getNumber()));
        textTime.setText(TimeHelper.getTimeString(application.getTime(), getString(R.string.default_date)));
        textDistance.setText(String.format(getString(R.string.distance_kilo), application.getDistance()));
        textStyle.setText(application.getStyle());
        textMenu.setText(application.getMenu());
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.write_estimate_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    @OnClick(R.id.button_send)
    public void sendEstimate() {
        MyAlertDialog.newInstance(
                getString(R.string.text_alert_dialog_title)
                , getString(R.string.text_alert_send_estimate)
                , this)
                .show(getSupportFragmentManager(), null);
    }

    @Override
    public void onPositive() {
        EstimateAddDTO dto = new EstimateAddDTO();

        dto.setWriter(SharedPreperenceHelper.getInstance(this).getLoginId());
        dto.setMessage(editMessage.getText().toString());
        dto.setWritedTime(TimeHelper.now());
        RetrofitAdmin.getInstance().adminService.addEstimate(application.getId(), dto).enqueue(new Callback<ResultIntDTO>() {
            @Override
            public void onResponse(Call<ResultIntDTO> call, Response<ResultIntDTO> response) {
                Log.d("HTJ", "Estimate add - onResponse: " + response.body().getResult());
                if(response.body().getResult() == 1) {
                    returnToMain();
                } else {
                    Log.e("HTJ", "reponse 에러");
                }
            }

            @Override
            public void onFailure(Call<ResultIntDTO> call, Throwable t) {
                Log.e("HTJ", "Estimate add - onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onNegative() {

    }

    private void returnToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
