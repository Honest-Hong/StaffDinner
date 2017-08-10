package com.project.boostcamp.staffdinner.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.boostcamp.publiclibrary.api.DataReceiver;
import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.inter.ContactEventListener;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.data.RequestType;
import com.project.boostcamp.publiclibrary.domain.ClientEstimateDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.activity.EstimateDetailActivity;
import com.project.boostcamp.staffdinner.adapter.EstimateRecyclerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Hong Tae Joon on 2017-07-25.
 * 견적서 목록을 보여주는 화면
 */

// TODO: 2017-08-07 계약이 진행되었을 때 신청서를 작성하거나 계약된 것만 보여주도록
public class EstimateFragment extends Fragment {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.help_empty) View viewEmpty;
    private EstimateRecyclerAdapter adapter;
    private ContactEventListener contactEventListener;

    public static EstimateFragment newInstance() {
        EstimateFragment fragment = new EstimateFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            contactEventListener = (ContactEventListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Fail to casting contactEventListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_estimate, container, false);
        ButterKnife.bind(this, v);
        setupView(v);
        loadData();
        return v;
    }

    private void setupView(View v) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EstimateRecyclerAdapter(getContext(), dataEvent);
        recyclerView.setAdapter(adapter);
        swipeRefresh.setOnRefreshListener(onRefreshListener);
    }

    public void loadData() {
        showRefreshing();
        String clientId = SharedPreperenceHelper.getInstance(getContext()).getLoginId();
        RetrofitClient.getInstance().getEstimates(clientId, dataReceiver);
    }

    private DataEvent<ClientEstimateDTO> dataEvent = new DataEvent<ClientEstimateDTO>() {
        @Override
        public void onClick(ClientEstimateDTO data) {
            Intent intent = new Intent(getContext(), EstimateDetailActivity.class);
            intent.putExtra(ClientEstimateDTO.class.getName(), data);
            startActivityForResult(intent, RequestType.REQUEST_CONTACT);
        }
    };

    private DataReceiver<ArrayList<ClientEstimateDTO>> dataReceiver = new DataReceiver<ArrayList<ClientEstimateDTO>>() {
        @Override
        public void onReceive(ArrayList<ClientEstimateDTO> data) {
            adapter.setData(data);
            if(data.size() == 0) {
                viewEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                viewEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            hideRefreshing();
        }

        @Override
        public void onFail() {
            Toast.makeText(getContext(), R.string.fail_to_load_estimates, Toast.LENGTH_SHORT).show();
            hideRefreshing();
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadData();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestType.REQUEST_CONTACT) {
            if(resultCode == RESULT_OK) {
                contactEventListener.onContact();
            }
        }
    }

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
}
