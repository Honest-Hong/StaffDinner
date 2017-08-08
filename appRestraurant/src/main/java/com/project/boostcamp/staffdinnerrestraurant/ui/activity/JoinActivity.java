package com.project.boostcamp.staffdinnerrestraurant.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.project.boostcamp.publiclibrary.api.RetrofitAdmin;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.data.Geo;
import com.project.boostcamp.publiclibrary.domain.AdminJoinDTO;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;
import com.project.boostcamp.publiclibrary.util.EditTextHelper;
import com.project.boostcamp.publiclibrary.util.GeocoderHelper;
import com.project.boostcamp.publiclibrary.util.MarkerBuilder;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.publiclibrary.util.StringHelper;
import com.project.boostcamp.staffdinnerrestraurant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, OnMapReadyCallback, OnSuccessListener<Location>, GoogleApiClient.OnConnectionFailedListener {
    private static final int REQUEST_PERMISSION = 0x100;
    public static final String EXTRA_LOGIN_ID = "login_id";
    public static final String EXTRA_LOGIN_TYPE = "login_type";
    private View rootView;
    @BindView(R.id.edit_name) EditText editName;
    @BindView(R.id.edit_phone) EditText editPhone;
    @BindView(R.id.edit_style) EditText editStyle;
    @BindView(R.id.edit_menu) EditText editMenu;
    @BindView(R.id.edit_menu_cost) EditText editCost;
    @BindView(R.id.text_location) TextView textLocation;
    @BindView(R.id.button_join) Button btnJoin;
    @BindView(R.id.check_box) CheckBox checkBox;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient; // 현재 위치를 가져오는 서비스
    private Marker marker; // 신청서의 위치 지도 마커
    private String loginId;
    private int loginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        if(getIntent() != null) {
            loginId = getIntent().getStringExtra(EXTRA_LOGIN_ID);
            loginType = getIntent().getIntExtra(EXTRA_LOGIN_TYPE, -1);
        }
        ButterKnife.bind(this);

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        setupView();
    }

    private void setupView() {
        rootView = getWindow().getDecorView().getRootView();
        checkBox.setOnCheckedChangeListener(this);

        // TODO: 2017-07-31 위치 기능이 켜있는지 확인
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @OnClick(R.id.button_join)
    public void doJoin() {
        if(checkInvalidate()) {
            final AdminJoinDTO admin = new AdminJoinDTO();
            admin.setId(loginId);
            admin.setType(loginType);
            admin.setName(editName.getText().toString());
            admin.setPhone(editPhone.getText().toString());
            admin.setStyle(editStyle.getText().toString());
            admin.setMenu(editMenu.getText().toString());
            admin.setCost(Integer.parseInt(editCost.getText().toString()));
            admin.setGeo(new Geo("Point", new double[]{
                    googleMap.getCameraPosition().target.longitude,
                    googleMap.getCameraPosition().target.latitude
            }).toGeoDTO());
            admin.setToken(FirebaseInstanceId.getInstance().getToken());

            RetrofitAdmin.getInstance().adminService.join(admin).enqueue(new Callback<LoginDTO>() {
                @Override
                public void onResponse(Call<LoginDTO> call, Response<LoginDTO> response) {
                    Log.d("HTJ", "join onResponse: " + response.body());
                    if(response.body().getId() != null) {
                        SharedPreperenceHelper.getInstance(JoinActivity.this).saveLogin(response.body());
                        SharedPreperenceHelper.getInstance(JoinActivity.this).saveGeo(admin.getGeo());
                        startMainActivity();
                    } else {
                        Log.e("HTJ", "fail to join");
                    }
                }

                @Override
                public void onFailure(Call<LoginDTO> call, Throwable t) {
                    Log.e("HTJ", "join onFailure: " + t.getMessage());
                }
            });
        }
    }

    private boolean checkInvalidate() {
        // TODO: 2017-07-31 입력한 값들의 유효성 판단
        if(!EditTextHelper.greaterLength(editName, 4)) {
            Snackbar.make(rootView, R.string.snack_name_length, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if(!EditTextHelper.greaterLength(editPhone, 7)) {
            Snackbar.make(rootView, R.string.snack_phone, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if(!EditTextHelper.greaterLength(editStyle, 1)) {
            Snackbar.make(rootView, R.string.snack_style_select, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if(!EditTextHelper.greaterLength(editMenu, 1)) {
            Snackbar.make(rootView, R.string.snack_menu_input, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if(!EditTextHelper.greaterLength(editCost, 1)) {
            Snackbar.make(rootView, R.string.snack_menu_cost, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        btnJoin.setEnabled(checkBox.isChecked());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        initGoogleMap();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_PERMISSION);
        } else {
            setMyLocation();
        }
    }

    private void initGoogleMap() {
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setScrollGesturesEnabled(false);
        uiSettings.setZoomGesturesEnabled(false);
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }

    private void setMyLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this);
        }
    }

    @Override
    public void onSuccess(Location location) {
        if(location != null) {
            LatLng latLng = new LatLng(
                    location.getLatitude(),
                    location.getLongitude());
            setLocation(latLng);
        } else {
            Log.e("HTJ", "onSuccess: location is null");
        }
    }

    private void setLocation(LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        String add = GeocoderHelper.getAddress(this, latLng);
        textLocation.setText(StringHelper.cutStart(add, 18));
        if(marker != null) {
            marker.remove();
        }
        marker = googleMap.addMarker(MarkerBuilder.simple(latLng));
    }

    @OnClick(R.id.button_search)
    public void searchLocation() {
        LatLng latLng = marker.getPosition();
        Intent intentMap = new Intent(this, MapDetailActivity.class);
        intentMap.putExtra(ExtraType.EXTRA_LATITUDE, latLng.latitude);
        intentMap.putExtra(ExtraType.EXTRA_LONGITUDE, latLng.longitude);
        intentMap.putExtra(ExtraType.EXTRA_READ_ONLY, false);
        startActivityForResult(intentMap, ExtraType.REQUEST_LOCATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ExtraType.REQUEST_LOCATION) {
            if(resultCode == RESULT_OK) {
                setLocation(new LatLng(
                        data.getDoubleExtra(ExtraType.EXTRA_LATITUDE, 0),
                        data.getDoubleExtra(ExtraType.EXTRA_LONGITUDE, 0)
                ));
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("HTJ", connectionResult.getErrorMessage());
    }
}
