package com.project.boostcamp.staffdinnerrestraurant.fragment;

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
import android.widget.Toast;

import com.project.boostcamp.publiclibrary.api.DataReceiver;
import com.project.boostcamp.publiclibrary.api.RetrofitAdmin;
import com.project.boostcamp.publiclibrary.data.AdminEstimate;
import com.project.boostcamp.publiclibrary.data.DataEvent;
import com.project.boostcamp.publiclibrary.domain.AdminEstimateDTO;
import com.project.boostcamp.publiclibrary.util.LogHelper;
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
    @BindView(R.id.help_empty) View viewEmpty;
    private EstimateAdapter recyclerAdapter;

    public static EstimateFragment newInstance() {
        return new EstimateFragment();
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
        recyclerAdapter = new EstimateAdapter(getContext(), dataEvent);
        recyclerView.setAdapter(recyclerAdapter);
        swipeRefresh.setOnRefreshListener(onRefreshListener);
    }

    private DataEvent<AdminEstimate> dataEvent = new DataEvent<AdminEstimate>() {
        @Override
        public void onClick(AdminEstimate data) {

        }
    };

    private void loadData() {
        String id = SharedPreperenceHelper.getInstance(getContext()).getLoginId();
        RetrofitAdmin.getInstance().getEstimateList(id, dataReceiver);
    }

    private DataReceiver<ArrayList<AdminEstimateDTO>> dataReceiver = new DataReceiver<ArrayList<AdminEstimateDTO>>() {
        @Override
        public void onReceive(ArrayList<AdminEstimateDTO> data) {
            if(data == null) {
                data = new ArrayList<>();
            }
            ArrayList<AdminEstimate> arr = new ArrayList<>();
            for(int i=0; i<data.size();i ++) {
                AdminEstimate item = new AdminEstimate();
                item.set_id(data.get(i).get_id());
                item.setClientName(data.get(i).getClient().getName());
                item.setMessage(data.get(i).getMessage());
                item.setWritedTime(data.get(i).getWritedTime());
                item.setState(data.get(i).getState());
                arr.add(item);
            }
            if(arr.size() > 0) {
                viewEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                viewEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            if(swipeRefresh.isRefreshing()) {
                swipeRefresh.setRefreshing(false);
            }
            recyclerAdapter.setData(arr);
        }

        @Override
        public void onFail() {
            if(swipeRefresh.isRefreshing()) {
                swipeRefresh.setRefreshing(false);
            }
            Toast.makeText(getContext(), R.string.fail_to_load_estimates, Toast.LENGTH_SHORT).show();
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadData();
        }
    };
}
