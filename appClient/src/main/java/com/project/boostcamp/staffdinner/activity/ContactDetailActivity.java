package com.project.boostcamp.staffdinner.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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
import com.project.boostcamp.publiclibrary.domain.ContactDTO;
import com.project.boostcamp.publiclibrary.domain.ResultIntDTO;
import com.project.boostcamp.publiclibrary.domain.ReviewAddDTO;
import com.project.boostcamp.publiclibrary.inter.ReviewListener;
import com.project.boostcamp.publiclibrary.util.GeocoderHelper;
import com.project.boostcamp.publiclibrary.util.MarkerBuilder;
import com.project.boostcamp.publiclibrary.util.StringHelper;
import com.project.boostcamp.publiclibrary.util.TimeHelper;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.dialog.WriteReviewDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  계약서를 자세히 볼 수 잇는 액티비티
 */
public class ContactDetailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private ContactDTO contact;
    @BindView(R.id.text_admin) TextView textAdmin;
    @BindView(R.id.text_client) TextView textClient;
    @BindView(R.id.text_contact_time) TextView textContactTime;
    @BindView(R.id.text_client_phone) TextView textClientPhone;
    @BindView(R.id.text_application_number) TextView textApplicationNumber;
    @BindView(R.id.text_application_location) TextView textApplicationLocation;
    @BindView(R.id.text_application_time) TextView textApplicationTime;
    @BindView(R.id.text_admin_location) TextView textAdminLocation;
    @BindView(R.id.text_admin_phone) TextView textAdminPhone;
    @BindView(R.id.text_estimate_time) TextView textEstimateTime;
    @BindView(R.id.text_estimate_message) TextView textEstimateMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        overridePendingTransition(R.anim.slide_in_bottom, android.R.anim.fade_out);

        if(getIntent() != null) {
            contact = getIntent().getParcelableExtra(ContactDTO.class.getName());
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
     * 뷰를 가져오고 툴바를 설정하는 함수
     * 또한 인텐트로 전달받은 신청서 데이터를 텍스트뷰에 뿌려준다
     */
    private void setupView() {
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.contact_detail_activity_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        textAdmin.setText(contact.getAdminName());
        textClient.setText(getString(R.string.user_name, contact.getClientName()));
        textContactTime.setText(TimeHelper.getTimeString(contact.getContactTime(), getString(R.string.default_time_pattern)));
        textClientPhone.setText(StringHelper.toPhoneNumber(contact.getClientPhone()));
        textApplicationNumber.setText(getString(R.string.person_number, contact.getAppNumber()));
        textApplicationLocation.setText(GeocoderHelper.getAddress(this, contact.getAppGeo().toLatLng()));
        textApplicationTime.setText(TimeHelper.getTimeString(contact.getAppTime(), getString(R.string.default_time_pattern)));
        textAdminLocation.setText(GeocoderHelper.getAddress(this, contact.getAdminGeo().toLatLng()));
        textAdminPhone.setText(StringHelper.toPhoneNumber(contact.getAdminPhone()));
        textEstimateTime.setText(TimeHelper.getTimeString(contact.getEstimateTime(), getString(R.string.default_time_pattern)));
        textEstimateMessage.setText(contact.getEstimateMessage());

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * 구글맵이 준비가 다 되었을 때를 처리하는 함수
     * 맵을 그대로만 보여주도록 제스쳐를 모두 false한다
     * 카메라의 위치는 식당의 위치를 가리킨다
     * 맵을 클릭하였을 때만 이벤트가 발생한다
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setScrollGesturesEnabled(false);
        uiSettings.setZoomGesturesEnabled(false);
        LatLng latLng = contact.getAdminGeo().toLatLng();
        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(latLng, 16));
        googleMap.addMarker(MarkerBuilder.simple(latLng));
        googleMap.setOnMapClickListener(this);
    }

    /**
     * 홈버튼 클릭을 처리하기 위해서 추가
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
     * 구글맵을 클릭하게 되는 경우 맵을 자세하게 볼 수 있는 액티비티로 넘어가게 된다
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        Intent intentMap = new Intent(this, MapDetailActivity.class);
        intentMap.putExtra(ExtraType.EXTRA_LATITUDE, contact.getAdminGeo().getCoordinates()[1]);
        intentMap.putExtra(ExtraType.EXTRA_LONGITUDE, contact.getAdminGeo().getCoordinates()[0]);
        intentMap.putExtra(ExtraType.EXTRA_READ_ONLY, true);
        startActivity(intentMap);
    }

    /**
     * 리뷰 남기기
     */
    @OnClick(R.id.button_review)
    public void sendReview() {
        WriteReviewDialog.newInstance(contact.getAdminName(), contact.getClientName(), reviewListener)
                .show(getSupportFragmentManager(), null);
    }

    /**
     * 리뷰 다이얼로그 결과 리스너
     */
    private ReviewListener reviewListener = new ReviewListener() {
        @Override
        public void onReview(String content, int rating) {
            ReviewAddDTO dto = new ReviewAddDTO();
            dto.setWriterId(contact.getClientId());
            dto.setContent(content);
            dto.setRating((float) rating / 2.0f);
            dto.setWritedTime(TimeHelper.now());
            RetrofitClient.getInstance().addReview(contact.getAdminId(), dto, reviewResultReceiver);
        }
    };

    /**
     * 리뷰 등록 요청 결과 리스너
     */
    private DataReceiver<ResultIntDTO> reviewResultReceiver = new DataReceiver<ResultIntDTO>() {
        @Override
        public void onReceive(ResultIntDTO data) {
            setResult(RESULT_OK);
            Snackbar.make(getWindow().getDecorView().getRootView(), R.string.thanks_to_review, Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onFail() {
            Toast.makeText(ContactDetailActivity.this, "리뷰 실패!", Toast.LENGTH_SHORT).show();
        }
    };

    @OnClick({R.id.text_admin_phone, R.id.text_client_phone})
    public void redirectDial(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if(v.getId() == R.id.text_client_phone) {
            intent.setData(Uri.parse("tel:" + contact.getClientPhone()));
        } else {
            intent.setData(Uri.parse("tel:" + contact.getAdminPhone()));
        }
        startActivity(intent);
    }
}
