package com.project.boostcamp.staffdinner.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.boostcamp.publiclibrary.api.DataReceiver;
import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.data.DefaultValue;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.domain.EventDTO;
import com.project.boostcamp.publiclibrary.domain.NearAdminDTO;
import com.project.boostcamp.publiclibrary.domain.NewAdminDTO;
import com.project.boostcamp.publiclibrary.domain.ReviewDTO;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.inter.GuidePlayer;
import com.project.boostcamp.publiclibrary.inter.ReviewEventListener;
import com.project.boostcamp.publiclibrary.sqlite.SQLiteHelper;
import com.project.boostcamp.publiclibrary.util.GeocoderHelper;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.activity.AdminDetailActivity;
import com.project.boostcamp.staffdinner.adapter.EventPagerAdapter;
import com.project.boostcamp.staffdinner.adapter.NearAdminRecyclerAdapter;
import com.project.boostcamp.staffdinner.adapter.NearReviewRecyclerAdapter;
import com.project.boostcamp.staffdinner.adapter.NewAdminRecyclerAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Hong Tae Joon on 2017-08-10.
 * 홈 화면 프래그먼트
 */

public class HomeFragment extends Fragment implements ReviewEventListener {
    @BindView(R.id.scroll_view) NestedScrollView scrollView;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.indicator) CircleIndicator circleIndicator;
    @BindView(R.id.recycler_view_near_admin) RecyclerView recyclerNearAdmin;
    @BindView(R.id.text_current_location) TextView textCurrentLocation;
    @BindView(R.id.recycler_view_review) RecyclerView recyclerReview;
    @BindView(R.id.recycler_view_new_admin) RecyclerView recyclerNewAdmin;
    private EventPagerAdapter eventPagerAdapter;
    private NearAdminRecyclerAdapter nearAdminRecyclerAdapter;
    private NearReviewRecyclerAdapter nearReviewRecyclerAdapter;
    private NewAdminRecyclerAdapter newAdminRecyclerAdapter;
    private Handler scrollHandler;
    private FusedLocationProviderClient fusedLocationClient; // 현재 위치를 가져오는 서비스
    private GuidePlayer guidePlayer;
    private double latitude;
    private double longitude;
    private SQLiteHelper sqLiteHelper;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sqLiteHelper = SQLiteHelper.getInstance(context);
        try {
            guidePlayer = (GuidePlayer)context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        RetrofitClient.getInstance().getEvents(eventDataReceiver);
        setupNearAdmin();
        setupNearReview();
        setupNewAdmin();
        refreshLocation();
        loadNewAdmin();
        return v;
    }

    private void loadNearAdmin() {
        RetrofitClient.getInstance().getNearAdmins(latitude, longitude, nearAdminReceiver);
    }

    private void loadNearReview() {
        RetrofitClient.getInstance().getNearReviews(latitude, longitude, nearReviewReceiver);
    }

    private void loadNewAdmin() {
        RetrofitClient.getInstance().getNewAdmins(newAdminReceiver);
    }

    private void setupNearReview() {
        nearReviewRecyclerAdapter = new NearReviewRecyclerAdapter(getContext(), nearReviewEvent);
        recyclerReview.setHasFixedSize(true);
        recyclerReview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerReview.setAdapter(nearReviewRecyclerAdapter);
    }

    private void setupNearAdmin() {
        nearAdminRecyclerAdapter = new NearAdminRecyclerAdapter(getContext(), nearAdminEvent);
        recyclerNearAdmin.setHasFixedSize(true);
        recyclerNearAdmin.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerNearAdmin.setAdapter(nearAdminRecyclerAdapter);
    }

    private void setupNewAdmin() {
        newAdminRecyclerAdapter = new NewAdminRecyclerAdapter(getContext(), newAdminEvent);
        recyclerNewAdmin.setHasFixedSize(true);
        recyclerNewAdmin.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerNewAdmin.setAdapter(newAdminRecyclerAdapter);
    }

    private void setupViewPager(ArrayList<EventDTO> data) {
        eventPagerAdapter = new EventPagerAdapter(getChildFragmentManager(), data);
        viewPager.setOffscreenPageLimit(data.size());
        viewPager.setAdapter(eventPagerAdapter);
        circleIndicator.setViewPager(viewPager);
        scrollHandler = new Handler();
        startAutoScroll();
    }

    private final DataReceiver<ArrayList<EventDTO>> eventDataReceiver = new DataReceiver<ArrayList<EventDTO>>() {
        @Override
        public void onReceive(ArrayList<EventDTO> data) {
            setupViewPager(data);
            sqLiteHelper.refreshEvents(data);
        }

        @Override
        public void onFail() {
            setupViewPager(sqLiteHelper.getEvents());
        }
    };

    private final DataReceiver<ArrayList<NearAdminDTO>> nearAdminReceiver = new DataReceiver<ArrayList<NearAdminDTO>>() {
        @Override
        public void onReceive(ArrayList<NearAdminDTO> data) {
            sqLiteHelper.refreshNearAdmins(data);
            nearAdminRecyclerAdapter.setData(data);
        }

        @Override
        public void onFail() {
            nearAdminRecyclerAdapter.setData(sqLiteHelper.getNearAdmins());
        }
    };

    private final DataReceiver<ArrayList<ReviewDTO>> nearReviewReceiver = new DataReceiver<ArrayList<ReviewDTO>>() {
        @Override
        public void onReceive(ArrayList<ReviewDTO> data) {
            sqLiteHelper.refreshNearReviews(data);
            nearReviewRecyclerAdapter.setData(data);
        }

        @Override
        public void onFail() {
            nearReviewRecyclerAdapter.setData(sqLiteHelper.getNearReviews());
        }
    };

    private final DataReceiver<ArrayList<NewAdminDTO>> newAdminReceiver = new DataReceiver<ArrayList<NewAdminDTO>>() {
        @Override
        public void onReceive(ArrayList<NewAdminDTO> data) {
            sqLiteHelper.refreshNewAdmins(data);
            newAdminRecyclerAdapter.setData(data);
        }

        @Override
        public void onFail() {
            newAdminRecyclerAdapter.setData(sqLiteHelper.getNewAdmins());
        }
    };

    private final DataEvent<NearAdminDTO> nearAdminEvent = new DataEvent<NearAdminDTO>() {
        @Override
        public void onClick(NearAdminDTO data) {
            redirectAdminDetailActivity(data.getAdminId(), data.getAdminType());
        }
    };

    private final DataEvent<ReviewDTO> nearReviewEvent = new DataEvent<ReviewDTO>() {
        @Override
        public void onClick(ReviewDTO data) {
            redirectAdminDetailActivity(data.getReceiverId(), data.getReceiverType());
        }
    };

    private final DataEvent<NewAdminDTO> newAdminEvent = new DataEvent<NewAdminDTO>() {
        @Override
        public void onClick(NewAdminDTO data) {
            redirectAdminDetailActivity(data.getId(), data.getType());
        }
    };

    private void redirectAdminDetailActivity(String id, int type) {
        Intent intent = new Intent(getContext(), AdminDetailActivity.class);
        intent.putExtra(ExtraType.EXTRA_LOGIN_ID, id);
        intent.putExtra(ExtraType.EXTRA_LOGIN_TYPE, type);
        startActivity(intent);
    }

    private void startAutoScroll() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                scrollHandler.post(runScrollPager);
            }
        }, DefaultValue.DEFAULT_SCROLL_DELAY, DefaultValue.DEFAULT_SCROLL_TIME);
    }

    private final Runnable runScrollPager = new Runnable() {
        @Override
        public void run() {
            final int currentPage = viewPager.getCurrentItem();
            if(currentPage < eventPagerAdapter.getCount() - 1) {
                viewPager.setCurrentItem(currentPage + 1);
            } else {
                viewPager.setCurrentItem(0);
            }
        }
    };

    @OnClick(R.id.button_guide)
    public void playGuide() {
        guidePlayer.showGuide();
    }

    @Override
    public void onNewReview() {
        RetrofitClient.getInstance().getNearReviews(latitude, longitude, nearReviewReceiver);
        scrollView.smoothScrollTo(0, recyclerReview.getTop());
    }

    @OnClick(R.id.button_refresh_location)
    public void refreshLocation() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        loadNearAdmin();
                        loadNearReview();
                        textCurrentLocation.setText(GeocoderHelper.getAddress(getContext(), new LatLng(latitude, longitude)));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    latitude = DefaultValue.DEFAULT_LATITUDE;
                    longitude = DefaultValue.DEFAULT_LONGITUDE;
                    loadNearAdmin();
                    loadNearReview();
                    textCurrentLocation.setText(GeocoderHelper.getAddress(getContext(), new LatLng(latitude, longitude)));
                }
            });
        }
    }
}
