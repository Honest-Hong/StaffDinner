package com.project.boostcamp.staffdinnerrestraurant.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.project.boostcamp.publiclibrary.api.DataReceiver;
import com.project.boostcamp.publiclibrary.api.RetrofitAdmin;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.data.Geo;
import com.project.boostcamp.publiclibrary.dialog.ArrayResultListener;
import com.project.boostcamp.publiclibrary.dialog.DialogResultListener;
import com.project.boostcamp.publiclibrary.dialog.MyAlertDialog;
import com.project.boostcamp.publiclibrary.domain.AdminJoinDTO;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;
import com.project.boostcamp.publiclibrary.domain.ResultIntDTO;
import com.project.boostcamp.publiclibrary.util.EditTextHelper;
import com.project.boostcamp.publiclibrary.util.GeocoderHelper;
import com.project.boostcamp.publiclibrary.util.MarkerBuilder;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.publiclibrary.util.StringHelper;
import com.project.boostcamp.staffdinner.dialog.StyleSelectDialog;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.dialog.ImageModeDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 식당 회원가입 액티비티
 * 이름, 사진, 전화번호, 분위기, 메뉴, 위치를 입력하고 회원가입을 진행한다.
 * 이전 액티비티에서 로그인이 정상적으로 이루어진 후에 이동된다.
 */
public class JoinActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, OnMapReadyCallback, OnSuccessListener<Location>, GoogleApiClient.OnConnectionFailedListener {
    private View rootView;
    @BindView(R.id.edit_name) EditText editName;
    @BindView(R.id.edit_phone) EditText editPhone;
    @BindView(R.id.text_style) TextView textStyle;
    @BindView(R.id.edit_menu) EditText editMenu;
    @BindView(R.id.edit_menu_cost) EditText editCost;
    @BindView(R.id.text_location) TextView textLocation;
    @BindView(R.id.button_join) CardView btnJoin;
    @BindView(R.id.check_box) CheckBox checkBox;
    @BindView(R.id.image_title) ImageView imageTitle;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient; // 현재 위치를 가져오는 서비스
    private Marker marker; // 신청서의 위치 지도 마커
    private String loginId;
    private int loginType;
    private String imageFilePath = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        if(getIntent() != null) {
            loginId = getIntent().getStringExtra(ExtraType.EXTRA_LOGIN_ID);
            loginType = getIntent().getIntExtra(ExtraType.EXTRA_LOGIN_TYPE, -1);
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

    /**
     * 회원가입처리를 하는 함수
     */
    @OnClick(R.id.button_join)
    public void onJoinClick() {
        if(checkInvalidate()) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, ExtraType.REQUEST_READ_PERMISSION);
            } else {
                doJoin();
            }
        }
    }

    private void doJoin() {
        progressDialog = ProgressDialog.show(this, "", "잠시만 기다려주세요...", true, false);
        final AdminJoinDTO dto = getAdminFromText();
        SharedPreperenceHelper.getInstance(JoinActivity.this).saveGeo(dto.getGeo());
        RetrofitAdmin.getInstance().join(dto, joinDataReceiver);
    }

    private AdminJoinDTO getAdminFromText() {
        AdminJoinDTO dto = new AdminJoinDTO();
        dto.setId(loginId);
        dto.setType(loginType);
        dto.setName(editName.getText().toString());
        dto.setPhone(editPhone.getText().toString());
        dto.setStyle(textStyle.getText().toString());
        dto.setMenu(editMenu.getText().toString());
        dto.setCost(Integer.parseInt(editCost.getText().toString()));
        dto.setGeo(new Geo("Point", new double[]{
                googleMap.getCameraPosition().target.longitude,
                googleMap.getCameraPosition().target.latitude
        }).toGeoDTO());
        dto.setToken(FirebaseInstanceId.getInstance().getToken());
        return dto;
    }

    /**
     * 회원가입 요청 결과
     */
    private DataReceiver<LoginDTO> joinDataReceiver = new DataReceiver<LoginDTO>() {
        @Override
        public void onReceive(LoginDTO data) {
            SharedPreperenceHelper.getInstance(JoinActivity.this).saveLogin(data);
            RetrofitAdmin.getInstance().setImage(data.getId(), data.getType(), new File(imageFilePath), setImageDataReceiver);
        }

        @Override
        public void onFail() {
            // 회원가입 실패
            if(progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    };

    /**
     * 이미지 업로드 요청 결과
     */
    private DataReceiver<ResultIntDTO> setImageDataReceiver =  new DataReceiver<ResultIntDTO>() {
        @Override
        public void onReceive(ResultIntDTO data) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            startMainActivity();
        }

        @Override
        public void onFail() {
            // 이미지 업로드 실패
            if(progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    };

    /**
     * 이미지를 가져오도록 알려주는 함수
     */
    @OnClick(R.id.image_title)
    public void selectImage() {
        ImageModeDialog.newInstace(new ImageModeDialog.ImageSelector() {
            @Override
            public void onSelect(int mode) {
                if(mode == ImageModeDialog.MODE_CAMERA) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, ExtraType.REQUEST_CAMERA);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, ExtraType.REQUEST_PICUTRE);
                }
            }
        }).show(getSupportFragmentManager(), null);
    }

    /**
     * 입력폼의 유효성을 판단하는 함수
     * @return 유효하면 true를 반환
     */
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
        if(!EditTextHelper.greaterLength(editMenu, 1)) {
            Snackbar.make(rootView, R.string.snack_menu_input, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if(!EditTextHelper.greaterLength(editCost, 1)) {
            Snackbar.make(rootView, R.string.snack_menu_cost, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if(imageFilePath.equals("")) {
            Snackbar.make(rootView, "이미지를 선택해주세요", Snackbar.LENGTH_SHORT).show();
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
        btnJoin.setVisibility(checkBox.isChecked()
                ? View.VISIBLE
                : View.INVISIBLE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setScrollGesturesEnabled(false);
        uiSettings.setZoomGesturesEnabled(false);
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, ExtraType.REQUEST_LOCATION);
        } else {
            setMyLocation();
        }
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

    /**
     * 위치 탐색 버튼 클릭 이벤트
     * 맵 액티비티로 이동하여 위치를 선택하도록 한다.
     */
    @OnClick(R.id.button_search)
    public void searchLocation() {
        LatLng latLng = marker.getPosition();
        Intent intentMap = new Intent(this, MapDetailActivity.class);
        intentMap.putExtra(ExtraType.EXTRA_LATITUDE, latLng.latitude);
        intentMap.putExtra(ExtraType.EXTRA_LONGITUDE, latLng.longitude);
        intentMap.putExtra(ExtraType.EXTRA_READ_ONLY, false);
        startActivityForResult(intentMap, ExtraType.REQUEST_LOCATION);
    }

    /**
     * 맵 액티비티로부터 위치 결과를 받는다
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ExtraType.REQUEST_LOCATION) {
            if(resultCode == RESULT_OK) {
                setLocation(new LatLng(
                        data.getDoubleExtra(ExtraType.EXTRA_LATITUDE, 0),
                        data.getDoubleExtra(ExtraType.EXTRA_LONGITUDE, 0)
                ));
            }
        } else if(requestCode == ExtraType.REQUEST_CAMERA) {
            if(resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap)data.getExtras().get("data");
                imageTitle.setImageBitmap(photo);
                imageFilePath = getFilePathFromUri(data.getData());
            }
        } else if(requestCode == ExtraType.REQUEST_PICUTRE) {
            if(resultCode == RESULT_OK) {
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    imageTitle.setImageBitmap(photo);
                    imageFilePath = getFilePathFromUri(data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if(requestCode == ExtraType.REQUEST_READ_PERMISSION) {
            if(resultCode == RESULT_OK) {
                doJoin();
            }
        }
        Log.d("HTJ", "imageFilePath: " + imageFilePath);
    }

    private String getFilePathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index_data);
        cursor.close();
        return filePath;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("HTJ", connectionResult.getErrorMessage());
    }

    @OnClick(R.id.button_style)
    public void selectStyle() {
        StyleSelectDialog dialog = StyleSelectDialog.newInstance(styleResult);
        dialog.show(getSupportFragmentManager(), null);
    }

    /**
     * 스타일 선택 다이얼로그의 결과를 받는 인터페이스
     */
    private ArrayResultListener<String> styleResult = new ArrayResultListener<String>() {
        @Override
        public void onResult(ArrayList<String> array) {
            String str = "";
            for(int i=0; i<array.size(); i++) {
                str += array.get(i);
                if(i < array.size() - 1) {
                    str += ", ";
                }
            }
            textStyle.setText(str);
        }
    };

    /**
     * GPS 기능이 켜있는지 확인하고 알람을 띄워 GPS기능을 키도록 유도하는 함수
     */
    private void checkGPS() {
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            MyAlertDialog.newInstance(
                    getString(R.string.dialog_alert_title),
                    getString(R.string.need_gps_on_help),
                    gpsDialogListener)
                    .show(getSupportFragmentManager(), null);
        }
    }

    /**
     * GPS 기능을 킬 수 있도록 설정으로 연결하거나 아무 행동도 하지 않는다
     */
    private DialogResultListener gpsDialogListener = new DialogResultListener() {
        @Override
        public void onPositive() {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        }

        @Override
        public void onNegative() {
        }
    };
}
