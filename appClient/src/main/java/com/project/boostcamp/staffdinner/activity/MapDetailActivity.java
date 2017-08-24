package com.project.boostcamp.staffdinner.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.data.RequestType;
import com.project.boostcamp.publiclibrary.util.GeocoderHelper;
import com.project.boostcamp.staffdinner.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 지도를 상세하게 볼 수 있는 액티비티
 * 인텐트로 위도와 경도 그리고 읽기 모드를 전달해준다
 * 읽기 모드에서는 위도와 경도에 마커를 표시하며 마커가 이동하지 않는다
 * 기본 모드에서는 위도와 경도에 마커 포인트가 표시되고 그 위에 선택 버튼이 보인다
 * 선택버튼을 누르면 result값으로 선택된 위도와 경도가 반환된다
 */
public class MapDetailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener{
    private double latitude;
    private double longitude;
    private boolean readOnly;

    @BindView(R.id.text_location) TextView textLocation;
    @BindView(R.id.image_marker) ImageView imageMarker;
    @BindView(R.id.button_select) Button btnSelect;
    @BindView(R.id.button_search) ImageButton btnSearch;
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
        btnSearch.setVisibility(readOnly
                ? View.GONE
                : View.VISIBLE);
    }

    /**
     * 구글맵이 준비가 되면 카메라를 넘겨받은 위도와 경도로 옮긴다
     * 읽기 모드일 경우에는 마커를 추가한다
     * 기본 모드에서는 기타 컨트롤러가 표시되며 카메라를 옮길 때 현재 위치의 주소가 표시된다
     * @param googleMap
     */
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

    /**
     * 카메라를 이동하고 정지했을 때 변경된 주소를 표시해준다
     */
    @Override
    public void onCameraIdle() {
        latitude = googleMap.getCameraPosition().target.latitude;
        longitude = googleMap.getCameraPosition().target.longitude;
        textLocation.setText(GeocoderHelper.getAddress(this, new LatLng(latitude, longitude)));
    }

    /**
     * 선택버튼을 클릭하였을 때 현재 위도와 경도를 반환해 준다
     * @param view
     */
    @OnClick(R.id.button_select)
    public void onSelect(View view) {
        getIntent().putExtra(ExtraType.EXTRA_LATITUDE, latitude);
        getIntent().putExtra(ExtraType.EXTRA_LONGITUDE, longitude);
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @OnClick(R.id.button_search)
    public void onSearch() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);
            startActivityForResult(intent, RequestType.REQUEST_LOCATION);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestType.REQUEST_LOCATION) {
            if(resultCode == RESULT_OK) {
                // 주소 검색 결과 표시
                Place place = PlaceAutocomplete.getPlace(this, data);
                textLocation.setText(place.getAddress());
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
            }
        }
    }
}
