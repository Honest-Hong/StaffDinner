package com.project.boostcamp.staffdinner.fragment;

import android.Manifest;
import android.content.Context;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.boostcamp.publiclibrary.api.DataReceiver;
import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.data.DefaultValue;
import com.project.boostcamp.publiclibrary.domain.AdminDTO;
import com.project.boostcamp.publiclibrary.domain.EventDTO;
import com.project.boostcamp.publiclibrary.domain.NearAdminDTO;
import com.project.boostcamp.publiclibrary.domain.NewAdminDTO;
import com.project.boostcamp.publiclibrary.domain.ReviewDTO;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.inter.GuidePlayer;
import com.project.boostcamp.publiclibrary.inter.ReviewEventListener;
import com.project.boostcamp.staffdinner.R;
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
 */

public class HomeFragment extends Fragment implements ReviewEventListener {
    @BindView(R.id.scroll_view) NestedScrollView scrollView;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.indicator) CircleIndicator circleIndicator;
    @BindView(R.id.recycler_view_near) RecyclerView recyclerNearAdmin;
    @BindView(R.id.recycler_view_review) RecyclerView recyclerReview;
    @BindView(R.id.recycler_view_new_admin) RecyclerView recyclerNewAdmin;
    private EventPagerAdapter eventPagerAdapter;
    private NearAdminRecyclerAdapter nearAdminRecyclerAdapter;
    private NearReviewRecyclerAdapter nearReviewRecyclerAdapter;
    private NewAdminRecyclerAdapter newAdminRecyclerAdapter;
    private Handler scrollHandler;
    private FusedLocationProviderClient fusedLocationClient; // 현재 위치를 가져오는 서비스
    private GuidePlayer guidePlayer;
    private Timer timer;
    private double latitude = DefaultValue.DEFAULT_LATITUDE;
    private double longitude = DefaultValue.DEFAULT_LONGITUDE;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            guidePlayer = (GuidePlayer)context;
        } catch (Exception e) {
            throw e;
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
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadNearAdmin();
                    loadNearReview();
                }
            });
        }
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

    private DataReceiver<ArrayList<EventDTO>> eventDataReceiver = new DataReceiver<ArrayList<EventDTO>>() {
        @Override
        public void onReceive(ArrayList<EventDTO> data) {
            setupViewPager(data);
        }

        @Override
        public void onFail() {
            setupViewPager(new ArrayList<EventDTO>());
        }
    };

    private DataReceiver<ArrayList<NearAdminDTO>> nearAdminReceiver = new DataReceiver<ArrayList<NearAdminDTO>>() {
        @Override
        public void onReceive(ArrayList<NearAdminDTO> data) {
            nearAdminRecyclerAdapter.setData(data);
        }

        @Override
        public void onFail() {

        }
    };

    private DataReceiver<ArrayList<NewAdminDTO>> newAdminReceiver = new DataReceiver<ArrayList<NewAdminDTO>>() {
        @Override
        public void onReceive(ArrayList<NewAdminDTO> data) {
            newAdminRecyclerAdapter.setData(data);
        }

        @Override
        public void onFail() {

        }
    };

    private DataEvent<NearAdminDTO> nearAdminEvent = new DataEvent<NearAdminDTO>() {
        @Override
        public void onClick(NearAdminDTO data) {
        }
    };

    private DataEvent<ReviewDTO> nearReviewEvent = new DataEvent<ReviewDTO>() {
        @Override
        public void onClick(ReviewDTO data) {
        }
    };

    private DataEvent<NewAdminDTO> newAdminEvent = new DataEvent<NewAdminDTO>() {
        @Override
        public void onClick(NewAdminDTO data) {

        }
    };

    private void startAutoScroll() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                scrollHandler.post(runScrollPager);
            }
        }, DefaultValue.DEFAULT_SCROLL_DELAY, DefaultValue.DEFAULT_SCROLL_TIME);
    }

    private Runnable runScrollPager = new Runnable() {
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

    private DataReceiver<ArrayList<ReviewDTO>> nearReviewReceiver = new DataReceiver<ArrayList<ReviewDTO>>() {
        @Override
        public void onReceive(ArrayList<ReviewDTO> data) {
            nearReviewRecyclerAdapter.setData(data);
        }

        @Override
        public void onFail() {

        }
    };

    @OnClick(R.id.button_guide)
    public void playGuide() {
        guidePlayer.showGuide(0);
    }

    @Override
    public void onNewReview() {
        RetrofitClient.getInstance().getNearReviews(latitude, longitude, nearReviewReceiver);
        scrollView.smoothScrollTo(0, recyclerReview.getTop());
    }
}
