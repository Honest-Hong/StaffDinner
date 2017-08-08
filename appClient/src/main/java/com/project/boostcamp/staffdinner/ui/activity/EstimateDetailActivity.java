package com.project.boostcamp.staffdinner.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.project.boostcamp.publiclibrary.api.DataReceiver;
import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.domain.ClientEstimateDTO;
import com.project.boostcamp.publiclibrary.domain.ContactAddDTO;
import com.project.boostcamp.publiclibrary.domain.ResultIntDTO;
import com.project.boostcamp.publiclibrary.util.TimeHelper;
import com.project.boostcamp.staffdinner.GlideApp;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.publiclibrary.dialog.DialogResultListener;
import com.project.boostcamp.publiclibrary.dialog.MyAlertDialog;
import com.project.boostcamp.publiclibrary.util.GeocoderHelper;
import com.project.boostcamp.publiclibrary.util.MarkerBuilder;

public class EstimateDetailActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, DialogResultListener, GoogleMap.OnMapClickListener {
    private ClientEstimateDTO estimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_detail);

        if(getIntent() != null) {
            estimate = getIntent().getParcelableExtra(ClientEstimateDTO.class.getName());
            setupView();
        } else {
            finish();
        }
    }

    private void setupView() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.estimate_detail_activity_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        ImageView imageView = (ImageView)findViewById(R.id.image_view);
        TextView textName = (TextView)findViewById(R.id.text_name);
        TextView textDate = (TextView)findViewById(R.id.text_date);
        TextView textMessage = (TextView)findViewById(R.id.text_message);
        TextView textStyle = (TextView)findViewById(R.id.text_style);
        TextView textMenu = (TextView)findViewById(R.id.text_menu);
        TextView textLocation = (TextView)findViewById(R.id.text_location);

        GlideApp.with(this)
                .load(estimate.getAdmin().getImage())
                .centerCrop()
                .into(imageView);
        textName.setText(estimate.getAdmin().getName());
        textDate.setText(TimeHelper.getTimeString(estimate.getWritedTime(), getString(R.string.default_time_pattern)));
        textMessage.setText(estimate.getMessage());
        textStyle.setText(estimate.getAdmin().getStyle());
        textMenu.setText(estimate.getAdmin().getMenu());
        textLocation.setText(GeocoderHelper.getAddress(this, estimate.getAdmin().getGeo().toLatLng()));

        findViewById(R.id.button_contact).setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        MyAlertDialog.newInstance(getString(R.string.dialog_alert_title), getString(R.string.dialog_contact_message), this)
                .show(getSupportFragmentManager(), null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setScrollGesturesEnabled(false);
        uiSettings.setZoomGesturesEnabled(false);
        LatLng latLng = estimate.getAdmin().getGeo().toLatLng();
        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(latLng, 16));
        googleMap.addMarker(MarkerBuilder.simple(latLng));
        googleMap.setOnMapClickListener(this);
    }

    @Override
    public void onPositive() {
        // TODO: 2017-08-07 계약서 서버에 등록하기
        ContactAddDTO dto = new ContactAddDTO();
        dto.setApp_id(estimate.getAppId());
        dto.setEstimate_id(estimate.getEstimateId());
        dto.setContactTime(TimeHelper.now());
        RetrofitClient.getInstance().addContact(dto, dataReceiver);
    }

    private DataReceiver<ResultIntDTO> dataReceiver = new DataReceiver<ResultIntDTO>() {
        @Override
        public void onReceive(ResultIntDTO data) {
            if(data.getResult() == 1) {
                Toast.makeText(EstimateDetailActivity.this, "계약 성공", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EstimateDetailActivity.this, "계약 실패", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFail() {
            Toast.makeText(EstimateDetailActivity.this, "서버 오류로 인한 계약 실패", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onNegative() {
        Toast.makeText(this, "계약 중단", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapClick(LatLng notThing) {
        Intent intentMap = new Intent(this, MapDetailActivity.class);
        LatLng latLng = estimate.getAdmin().getGeo().toLatLng();
        intentMap.putExtra(ExtraType.EXTRA_LATITUDE, latLng.latitude);
        intentMap.putExtra(ExtraType.EXTRA_LONGITUDE, latLng.longitude);
        intentMap.putExtra(ExtraType.EXTRA_READ_ONLY, true);
        startActivity(intentMap);
    }
}
