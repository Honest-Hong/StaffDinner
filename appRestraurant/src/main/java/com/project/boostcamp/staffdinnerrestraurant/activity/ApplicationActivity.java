package com.project.boostcamp.staffdinnerrestraurant.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.project.boostcamp.publiclibrary.data.AdminApplication;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.util.GeocoderHelper;
import com.project.boostcamp.publiclibrary.util.MarkerBuilder;
import com.project.boostcamp.publiclibrary.util.TimeHelper;
import com.project.boostcamp.staffdinnerrestraurant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplicationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private AdminApplication application;
    @BindView(R.id.tool_bar) Toolbar toolbar;
    @BindView(R.id.text_name) TextView textName;
    @BindView(R.id.text_message) TextView textMessage;
    @BindView(R.id.text_number) TextView textNumber;
    @BindView(R.id.text_wanted_time) TextView textWantedTime;
    @BindView(R.id.text_wanted_style) TextView textWantedStyle;
    @BindView(R.id.text_wanted_menu) TextView textWantedMenu;
    @BindView(R.id.text_location) TextView textLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_detail);
        ButterKnife.bind(this);

        application = getIntent().getParcelableExtra(AdminApplication.class.getName());
        setupView();
        setupToolbar();
        setupMap();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.apply_detail_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    private void setupView() {
        textName.setText(getString(R.string.text_apply_detail_name, application.getWriterName()));
        textMessage.setText(application.getTitle());
        textNumber.setText(getString(R.string.text_apply_detail_number, application.getNumber()));
        textWantedTime.setText(TimeHelper.getTimeString(application.getTime(), "MM월 dd일 HH시 mm분"));
        textWantedStyle.setText(application.getStyle());
        textWantedMenu.setText(application.getMenu());
        textLocation.setText(GeocoderHelper.getAddress(
                this
                , application.getGeo().toLatLng()));
    }

    private void setupMap() {
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        UiSettings settings = googleMap.getUiSettings();
        settings.setZoomControlsEnabled(false);
        settings.setMyLocationButtonEnabled(false);
        settings.setScrollGesturesEnabled(false);
        LatLng latLng = application.getGeo().toLatLng();
        googleMap.addMarker(MarkerBuilder.simple(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        googleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Intent intentMap = new Intent(this, MapDetailActivity.class);
        intentMap.putExtra(ExtraType.EXTRA_LATITUDE, application.getGeo().getCoordinates()[1]);
        intentMap.putExtra(ExtraType.EXTRA_LONGITUDE, application.getGeo().getCoordinates()[0]);
        intentMap.putExtra(ExtraType.EXTRA_READ_ONLY, true);
        startActivity(intentMap);
    }

    @OnClick(R.id.button_send)
    public void checkEstimate() {
        // TODO: 2017-07-31 견적서를 이미 작성했는지 확인해야함.
        boolean alreadyWrited = false;
        if(!alreadyWrited) {
            showWriteEstimateActivity();
        }
    }

    private void showWriteEstimateActivity() {
        Intent intent = new Intent(this, WriteEstimateActivity.class);
        intent.putExtra(AdminApplication.class.getName(), application);
        startActivity(intent);
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
}
