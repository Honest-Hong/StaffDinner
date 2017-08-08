package com.project.boostcamp.staffdinnerrestraurant.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.util.GeocoderHelper;
import com.project.boostcamp.staffdinnerrestraurant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapDetailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener{
    private double latitude;
    private double longitude;
    private boolean readOnly;

    @BindView(R.id.text_location) TextView textLocation;
    @BindView(R.id.image_marker) ImageView imageMarker;
    @BindView(R.id.button_select) Button btnSelect;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_detail);

        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        latitude = getIntent().getDoubleExtra(ExtraType.EXTRA_LATITUDE, 0);
        longitude = getIntent().getDoubleExtra(ExtraType.EXTRA_LONGITUDE, 0);
        readOnly = getIntent().getBooleanExtra(ExtraType.EXTRA_READ_ONLY, false);

        imageMarker.setVisibility(readOnly
                ? View.GONE
                : View.VISIBLE);
        btnSelect.setVisibility(readOnly
                ? View.GONE
                : View.VISIBLE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        if(readOnly) {
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng));
            textLocation.setText(GeocoderHelper.getAddress(this, latLng));
        } else {
            UiSettings uiSettings = googleMap.getUiSettings();
            uiSettings.setMyLocationButtonEnabled(true);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                uiSettings.setZoomControlsEnabled(true);
                googleMap.setMyLocationEnabled(true);
            }
            googleMap.setOnCameraIdleListener(this);
        }
    }

    @Override
    public void onCameraIdle() {
        latitude = googleMap.getCameraPosition().target.latitude;
        longitude = googleMap.getCameraPosition().target.longitude;
        textLocation.setText(GeocoderHelper.getAddress(this, new LatLng(latitude, longitude)));
    }

    @OnClick(R.id.button_select)
    public void onSelect(View view) {
        getIntent().putExtra(ExtraType.EXTRA_LATITUDE, latitude);
        getIntent().putExtra(ExtraType.EXTRA_LONGITUDE, longitude);
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
