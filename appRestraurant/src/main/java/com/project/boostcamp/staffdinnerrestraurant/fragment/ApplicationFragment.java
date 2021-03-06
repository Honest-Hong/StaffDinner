package com.project.boostcamp.staffdinnerrestraurant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.api.DataReceiver;
import com.project.boostcamp.publiclibrary.api.RetrofitAdmin;
import com.project.boostcamp.publiclibrary.data.AdminApplication;
import com.project.boostcamp.publiclibrary.data.DefaultValue;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.domain.AdminApplicationDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.activity.ApplicationDetailActivity;
import com.project.boostcamp.staffdinnerrestraurant.adapter.ApplicationAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Tae Joon on 2017-07-28.
 */

public class ApplicationFragment extends Fragment {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.image_empty) ImageView imageEmpty;
    @BindView(R.id.text_empty) TextView textEmpty;
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
        textEmpty.setText(R.string.not_exist_application);
    }

    private DataEvent<AdminApplication> dataEvent = new DataEvent<AdminApplication>() {
        @Override
        public void onClick(AdminApplication data) {
            Intent intent = new Intent(getContext(), ApplicationDetailActivity.class);
            intent.putExtra(AdminApplication.class.getName(), data);
            startActivity(intent);
        }
    };

    public void loadData() {
        showRefreshing();
        String id = SharedPreperenceHelper.getInstance(getContext()).getLoginId();
        RetrofitAdmin.getInstance().getApplicationList(id, DefaultValue.DEFAULT_MAX_DISTNACE, appDataReceiver);
    }

    private DataReceiver<ArrayList<AdminApplicationDTO>> appDataReceiver = new DataReceiver<ArrayList<AdminApplicationDTO>>() {
        @Override
        public void onReceive(ArrayList<AdminApplicationDTO> data) {
            if(data == null) {
                data = new ArrayList<>();
            }
            ArrayList<AdminApplication> arr = new ArrayList<AdminApplication>();
            for(AdminApplicationDTO dto : data) {
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
                imageEmpty.setVisibility(View.GONE);
                textEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                imageEmpty.setVisibility(View.VISIBLE);
                textEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            hideRefreshing();
            adapter.setData(arr);
        }

        @Override
        public void onFail() {
            hideRefreshing();
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadData();
        }
    };

    private void showRefreshing() {
        if(!swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
        }
    }

    private void hideRefreshing() {
        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_applications, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_refresh:
                loadData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
