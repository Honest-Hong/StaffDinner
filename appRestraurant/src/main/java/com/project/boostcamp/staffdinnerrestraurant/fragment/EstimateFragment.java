package com.project.boostcamp.staffdinnerrestraurant.fragment;

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
import android.widget.Toast;

import com.project.boostcamp.publiclibrary.api.DataReceiver;
import com.project.boostcamp.publiclibrary.api.RetrofitAdmin;
import com.project.boostcamp.publiclibrary.data.AdminEstimate;
import com.project.boostcamp.publiclibrary.data.EstimateStateType;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.domain.AdminEstimateDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.adapter.EstimateAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Tae Joon on 2017-07-28.
 */

public class EstimateFragment extends Fragment {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.image_empty) ImageView imageEmpty;
    @BindView(R.id.text_empty) TextView textEmpty;
    private EstimateAdapter recyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<AdminEstimate> dataAll;
    private int showMode = -1;
    private boolean isLoading = false;
    private boolean dataEnded = false;
    private int page = 0;

    public static EstimateFragment newInstance() {
        return new EstimateFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout, container, false);
        setupView(v);
        loadFirstData();
        return v;
    }

    private void setupView(View v) {
        ButterKnife.bind(this, v);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new EstimateAdapter(getContext(), dataEvent);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                final int total = linearLayoutManager.getItemCount();
                final int last = linearLayoutManager.findLastVisibleItemPosition();
                if(!isLoading && !dataEnded && total == last + 1) {
                    loadMoreData();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        swipeRefresh.setOnRefreshListener(onRefreshListener);
        textEmpty.setText(R.string.not_exist_estimates);
    }

    private DataEvent<AdminEstimate> dataEvent = new DataEvent<AdminEstimate>() {
        @Override
        public void onClick(AdminEstimate data) {

        }
    };

    public void loadFirstData() {
        page = 0;
        dataEnded = false;
        showRefreshing();
        String id = SharedPreperenceHelper.getInstance(getContext()).getLoginId();
        RetrofitAdmin.getInstance().getEstimateList(id, 0, firstDataReceiver);
    }

    private DataReceiver<ArrayList<AdminEstimateDTO>> firstDataReceiver = new DataReceiver<ArrayList<AdminEstimateDTO>>() {
        @Override
        public void onReceive(ArrayList<AdminEstimateDTO> data) {
            if(data == null) {
                data = new ArrayList<>();
            }
            dataAll = new ArrayList<>();
            for(int i=0; i<data.size();i ++) {
                AdminEstimate item = new AdminEstimate();
                item.set_id(data.get(i).get_id());
                item.setClientName(data.get(i).getClient().getName());
                item.setMessage(data.get(i).getMessage());
                item.setWritedTime(data.get(i).getWritedTime());
                item.setState(data.get(i).getState());
                dataAll.add(item);
            }
            if(dataAll.size() > 0) {
                imageEmpty.setVisibility(View.GONE);
                textEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                dataAll.add(null);
                showListByMode(showMode);
            } else {
                imageEmpty.setVisibility(View.VISIBLE);
                textEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            hideRefreshing();
        }

        @Override
        public void onFail() {
            hideRefreshing();
            Toast.makeText(getContext(), R.string.fail_to_load_estimates, Toast.LENGTH_SHORT).show();
        }
    };

    private void showListByMode(int showMode) {
        if(showMode != -1) {
            recyclerAdapter.setData(filteredData(showMode));
        } else {
            recyclerAdapter.setData(dataAll);
        }
    }

    public void loadMoreData() {
        page++;
        isLoading = true;
        String id = SharedPreperenceHelper.getInstance(getContext()).getLoginId();
        RetrofitAdmin.getInstance().getEstimateList(id, page, moreDataReceiver);
    }

    private DataReceiver<ArrayList<AdminEstimateDTO>> moreDataReceiver = new DataReceiver<ArrayList<AdminEstimateDTO>>() {
        @Override
        public void onReceive(ArrayList<AdminEstimateDTO> data) {
            if(data.size() != 0) {
                for (int i = 0; i < data.size(); i++) {
                    AdminEstimate item = new AdminEstimate();
                    item.set_id(data.get(i).get_id());
                    item.setClientName(data.get(i).getClient().getName());
                    item.setMessage(data.get(i).getMessage());
                    item.setWritedTime(data.get(i).getWritedTime());
                    item.setState(data.get(i).getState());
                    dataAll.add(dataAll.size() - 1, item);
                }
            } else {
                dataAll.remove(dataAll.size() - 1);
                dataEnded = true;
            }
            isLoading = false;
            showListByMode(showMode);
        }

        @Override
        public void onFail() {
            isLoading = false;
            Toast.makeText(getContext(), R.string.fail_to_load_estimates, Toast.LENGTH_SHORT).show();
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadFirstData();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_estimates, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_refresh:
                loadFirstData();
                break;
            case R.id.menu_show_all:
                recyclerAdapter.setData(dataAll);
                showMode = -1;
                break;
            case R.id.menu_show_waiting:
                showMode = EstimateStateType.STATE_WATING;
                recyclerAdapter.setData(filteredData(showMode));
                break;
            case R.id.menu_show_contacted:
                showMode = EstimateStateType.STATE_CONTACTED;
                recyclerAdapter.setData(filteredData(showMode));
                break;
            case R.id.menu_show_canceled:
                showMode = EstimateStateType.STATE_CANCELED;
                recyclerAdapter.setData(filteredData(showMode));

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<AdminEstimate> filteredData(int showMode) {
        ArrayList<AdminEstimate> data = new ArrayList<>();
        for(AdminEstimate est: dataAll) {
            if(est != null && est.getState() == showMode) {
                data.add(est);
            }
        }
        if(!dataEnded) {
            data.add(null);
        }
        return data;
    }
}
