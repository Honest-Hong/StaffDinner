package com.project.boostcamp.staffdinner.activity;

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
import com.project.boostcamp.publiclibrary.inter.DialogResultListener;
import com.project.boostcamp.publiclibrary.dialog.MyAlertDialog;
import com.project.boostcamp.publiclibrary.util.GeocoderHelper;
import com.project.boostcamp.publiclibrary.util.MarkerBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EstimateDetailActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, DialogResultListener, GoogleMap.OnMapClickListener {
    private ClientEstimateDTO estimate;
    @BindView(R.id.image_view) ImageView imageView;
    @BindView(R.id.text_name) TextView textName;
    @BindView(R.id.text_date)TextView textDate;
    @BindView(R.id.text_message)TextView textMessage;
    @BindView(R.id.text_style)TextView textStyle;
    @BindView(R.id.text_menu)TextView textMenu;
    @BindView(R.id.text_location)TextView textLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_detail);
        overridePendingTransition(R.anim.slide_in_bottom, android.R.anim.fade_out);

        if(getIntent() != null) {
            estimate = getIntent().getParcelableExtra(ClientEstimateDTO.class.getName());
            setupView();
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_bottom);
    }

    /**
     * 툴바를 설정한다
     * 신청서 내용을 텍스트뷰에 표시한다
     * 구글 맵스의 위치를 변경한다
     */
    private void setupView() {
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.estimate_detail_activity_title);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        GlideApp.with(this)
                .load(RetrofitClient.getInstance().getAdminImageUrl(estimate.getAdmin().getId(), estimate.getAdmin().getType()))
                .centerCrop()
                .into(imageView);
        textName.setText(estimate.getAdmin().getName());
        textDate.setText(TimeHelper.getTimeString(estimate.getWritedTime(), getString(R.string.default_time_pattern)));
        textMessage.setText(estimate.getMessage());
        textStyle.setText(estimate.getAdmin().getStyle());
        textMenu.setText(estimate.getAdmin().getMenu());
        textLocation.setText(GeocoderHelper.getAddress(this, estimate.getAdmin().getGeo().toLatLng()));

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * 홈 버튼 클릭 처리
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 계약하기 버튼 클릭
     * @param view
     */
    @OnClick(R.id.button_contact)
    public void onClick(View view) {
        MyAlertDialog.newInstance(getString(R.string.dialog_alert_title), getString(R.string.dialog_contact_message), this)
                .show(getSupportFragmentManager(), null);
    }

    /**
     * 구글 맵스가 준비되면 카메라를 옮겨준다.
     * 맵의 위치를 옮길 수 없도록 제스쳐를 막아준다.
     * @param googleMap
     */
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

    /**
     * 계약하시겠습니까? 다이얼로그에서 예 버튼을 눌렀을 때
     * 계약을 하기위한 DTO를 설정하고 서버에 계약 요청을 한다.
     */
    @Override
    public void onPositive() {
        ContactAddDTO dto = new ContactAddDTO();
        dto.setApp_id(estimate.getAppId());
        dto.setEstimate_id(estimate.getEstimateId());
        dto.setContactTime(TimeHelper.now());
        RetrofitClient.getInstance().addContact(dto, dataReceiver);
    }

    /**
     * 계약 요청 결과
     * 계약에 성공하면 메인화면으로 돌아간다
     * 실패할경우에는 토스트 메시지를 띄워준다.
     */
    private final DataReceiver<ResultIntDTO> dataReceiver = new DataReceiver<ResultIntDTO>() {
        @Override
        public void onReceive(ResultIntDTO data) {
            if(data.getResult() == 1) {
                setResult(RESULT_OK);
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

    /**
     * 계약하기 다이얼로그 취소 버튼
     */
    @Override
    public void onNegative() {
    }

    /**
     * 구글 맵스를 클릭하였을 때 맵 자세히보기 화면으로 넘어간다
     * @param notThing
     */
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
