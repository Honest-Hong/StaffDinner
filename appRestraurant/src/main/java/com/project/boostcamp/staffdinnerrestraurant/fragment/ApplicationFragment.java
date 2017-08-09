package com.project.boostcamp.staffdinnerrestraurant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.api.RetrofitAdmin;
import com.project.boostcamp.publiclibrary.data.AdminApplication;
import com.project.boostcamp.publiclibrary.data.DataEvent;
import com.project.boostcamp.publiclibrary.domain.AdminApplicationDTO;
import com.project.boostcamp.publiclibrary.domain.GeoDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.activity.ApplicationDetailActivity;
import com.project.boostcamp.staffdinnerrestraurant.adapter.ApplicationAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hong Tae Joon on 2017-07-28.
 */

public class ApplicationFragment extends Fragment {
    private static final float MAX_DISTANCE = 2.0f;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.help_empty) View viewEmpty;
    private ApplicationAdapter adapter;

    public static ApplicationFragment newInstance() {
        return new ApplicationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout, container, false);
        setupView(v);
        loadData();
        return v;
    }

    private void setupView(View v) {
        ButterKnife.bind(this, v);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ApplicationAdapter(getContext(), dataEvent);
        recyclerView.setAdapter(adapter);
        swipeRefresh.setOnRefreshListener(onRefreshListener);
    }

    private DataEvent<AdminApplication> dataEvent = new DataEvent<AdminApplication>() {
        @Override
        public void onClick(AdminApplication data) {
            Intent intent = new Intent(getContext(), ApplicationDetailActivity.class);
            intent.putExtra(AdminApplication.class.getName(), data);
            startActivity(intent);
        }
    };

    private void loadData() {
        GeoDTO geo = SharedPreperenceHelper.getInstance(getContext()).getGeo();
        RetrofitAdmin.getInstance().adminService.get(geo.getCoordinates()[1], geo.getCoordinates()[0], MAX_DISTANCE).enqueue(new Callback<List<AdminApplicationDTO>>() {
            @Override
            public void onResponse(Call<List<AdminApplicationDTO>> call, Response<List<AdminApplicationDTO>> response) {
                Log.d("HTJ", "ApplicationFragment-loadData-onResponse: " + response.body());
                ArrayList<AdminApplication> arr = new ArrayList<AdminApplication>();
                for(AdminApplicationDTO dto : response.body()) {
                    AdminApplication app = new AdminApplication();
                    app.setId(dto.get_id());
                    app.setWriterName(dto.getClient().getName());
                    app.setTitle(dto.getTitle());
                    app.setNumber(dto.getNumber());
                    app.setTime(dto.getTime());
                    app.setDistance(dto.getDistance());
                    app.setGeo(dto.getGeo().toGeo());
                    app.setStyle(dto.getStyle());
                    app.setMenu(dto.getMenu());
                    app.setWritedTime(dto.getWritedTime());
                    arr.add(app);
                }
                // 데이터가 존재하면 보여주고 존재하지 않으면 데이터가 없다는 것을 표시해줌
                if(arr.size() > 0) {
                    viewEmpty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    viewEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                // Refreshing 애니메이션 중지
                if(swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }
                adapter.setData(arr);
            }

            @Override
            public void onFailure(Call<List<AdminApplicationDTO>> call, Throwable t) {
                Log.e("HTJ", "ApplicationFragment-loadData-onFailure: " + t.getMessage());
            }
        });
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadData();
        }
    };
}
