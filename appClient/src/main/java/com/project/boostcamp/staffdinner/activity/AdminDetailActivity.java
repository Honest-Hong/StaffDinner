package com.project.boostcamp.staffdinner.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.project.boostcamp.publiclibrary.domain.AdminDTO;
import com.project.boostcamp.publiclibrary.domain.ReviewAverageDTO;
import com.project.boostcamp.publiclibrary.domain.ReviewDTO;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.util.GeocoderHelper;
import com.project.boostcamp.publiclibrary.util.MarkerBuilder;
import com.project.boostcamp.publiclibrary.util.StringHelper;
import com.project.boostcamp.publiclibrary.util.TimeHelper;
import com.project.boostcamp.staffdinner.GlideApp;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.BonusImageRecyclerAdapter;
import com.project.boostcamp.staffdinner.adapter.NearReviewRecyclerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * 식당의 자세한 정보를 보여주는 액티비티
 * 식당에 달린 리뷰도 확인이 가능하다
 */

public class AdminDetailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    @BindView(R.id.linear) LinearLayout linear;
    @BindView(R.id.image_view) ImageView imageView;
    @BindView(R.id.text_name) TextView textName;
    @BindView(R.id.text_phone) TextView textPhone;
    @BindView(R.id.text_style) TextView textStyle;
    @BindView(R.id.text_menu) TextView textMenu;
    @BindView(R.id.text_cost) TextView textCost;
    @BindView(R.id.text_location) TextView textLocation;
    @BindView(R.id.text_review_average) TextView textReviewAverage;
    @BindView(R.id.text_review_number) TextView textReviewNumber;
    @BindView(R.id.rating_review_average) MaterialRatingBar ratingReviewAverage;
    @BindView(R.id.recycler_image) RecyclerView recyclerImage;
    private BonusImageRecyclerAdapter bonusImageAdapter;
    private String adminId;
    private int adminType;
    private AdminDTO data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.admin_detail_activity_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        adminId = getIntent().getStringExtra(ExtraType.EXTRA_LOGIN_ID);
        adminType = getIntent().getIntExtra(ExtraType.EXTRA_LOGIN_TYPE, -1);

        RetrofitClient.getInstance().getAdminInformation(adminId, adminType, detailDataReceiver);
        RetrofitClient.getInstance().getAdminReviews(adminId, adminType, reviewDataReceiver);
        RetrofitClient.getInstance().getAdminReviewAverage(adminId, adminType, reviewAverageDataReceiver);
    }

    private DataReceiver<AdminDTO> detailDataReceiver = new DataReceiver<AdminDTO>() {
        @Override
        public void onReceive(AdminDTO data) {
            AdminDetailActivity.this.data = data;
            setupView(data);
        }

        @Override
        public void onFail() {
            Toast.makeText(AdminDetailActivity.this, "Data loading error", Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    private DataReceiver<ArrayList<ReviewDTO>> reviewDataReceiver = new DataReceiver<ArrayList<ReviewDTO>>() {
        @Override
        public void onReceive(ArrayList<ReviewDTO> data) {
            setupReview(data);
        }

        @Override
        public void onFail() {
            setupReview(new ArrayList<ReviewDTO>());
        }
    };

    private DataReceiver<ReviewAverageDTO> reviewAverageDataReceiver = new DataReceiver<ReviewAverageDTO>() {
        @Override
        public void onReceive(ReviewAverageDTO data) {
            textReviewAverage.setText(String.format("%1$.1f", data.getAverage()));
            textReviewNumber.setText(getString(R.string.person_number, data.getCount()));
            ratingReviewAverage.setProgress((int)(data.getAverage() * 2));
        }

        @Override
        public void onFail() {

        }
    };

    private void setupView(AdminDTO data) {
        GlideApp.with(AdminDetailActivity.this)
                .load(RetrofitClient.getInstance().getAdminImageUrl(adminId, adminType))
                .centerCrop()
                .into(imageView);
        textName.setText(data.getName());
        textPhone.setText(StringHelper.toPhoneNumber(data.getPhone()));
        textStyle.setText(data.getStyle());
        textMenu.setText(data.getMenu());
        textCost.setText(getString(R.string.won, data.getCost()));
        textLocation.setText(GeocoderHelper.getAddress(AdminDetailActivity.this, data.getGeo().toLatLng()));
        setupBonusImage(data.getBonusImageCount());

        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(AdminDetailActivity.this);
    }

    private void setupBonusImage(int count) {
        if(count == 0) {
            recyclerImage.setVisibility(View.GONE);
        } else {
            ArrayList<String> bonusImages = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                bonusImages.add(RetrofitClient.getInstance().getBonusImageUrl(adminId, adminType, i));
            }
            bonusImageAdapter = new BonusImageRecyclerAdapter(this, bonusImageEvent);
            bonusImageAdapter.setData(bonusImages);
            recyclerImage.setHasFixedSize(true);
            recyclerImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerImage.setAdapter(bonusImageAdapter);
        }
    }

    private void setupReview(ArrayList<ReviewDTO> data) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(ReviewDTO review: data) {
            View v = getLayoutInflater().inflate(R.layout.item_review, linear, false);
            v.setLayoutParams(layoutParams);
            TextView textTo = (TextView)v.findViewById(R.id.text_receiver);
            TextView textFrom = (TextView)v.findViewById(R.id.text_writer);
            TextView textContent = (TextView) v.findViewById(R.id.text_content);
            TextView textTime = (TextView) v.findViewById(R.id.text_time);
            MaterialRatingBar ratingBar = (MaterialRatingBar) v.findViewById(R.id.rating_bar);

            textTo.setText(review.getReceiver());
            textFrom.setText(review.getWriter());
            textContent.setText(review.getContent());
            textTime.setText(TimeHelper.getTimeString(review.getWritedTime(), "MM/dd"));
            ratingBar.setProgress((int)(review.getRating()*2));
            linear.addView(v);
        }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setScrollGesturesEnabled(false);
        uiSettings.setZoomGesturesEnabled(false);
        LatLng latLng = data.getGeo().toLatLng();
        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(latLng, 16));
        googleMap.addMarker(MarkerBuilder.simple(latLng));
        googleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Intent intentMap = new Intent(this, MapDetailActivity.class);
        intentMap.putExtra(ExtraType.EXTRA_LATITUDE, latLng.latitude);
        intentMap.putExtra(ExtraType.EXTRA_LONGITUDE, latLng.longitude);
        intentMap.putExtra(ExtraType.EXTRA_READ_ONLY, true);
        startActivity(intentMap);
    }

    @OnClick(R.id.text_phone)
    public void redirectDial() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + data.getPhone()));
        startActivity(intent);
    }

    private DataEvent<String> bonusImageEvent = new DataEvent<String>() {
        @Override
        public void onClick(String data) {

        }
    };
}
