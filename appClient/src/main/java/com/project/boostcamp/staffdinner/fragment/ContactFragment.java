package com.project.boostcamp.staffdinner.fragment;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import com.project.boostcamp.publiclibrary.api.DataReceiver;
import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.data.RequestType;
import com.project.boostcamp.publiclibrary.inter.DataEvent;
import com.project.boostcamp.publiclibrary.domain.ContactDTO;
import com.project.boostcamp.publiclibrary.inter.ReviewEventListener;
import com.project.boostcamp.publiclibrary.util.SQLiteHelper;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.activity.ContactDetailActivity;
import com.project.boostcamp.staffdinner.adapter.ContactRecyclerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Tae Joon on 2017-07-25.
 */

public class ContactFragment extends Fragment {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.image_empty) ImageView imageEmpty;
    @BindView(R.id.text_empty) TextView textEmpty;
    private ContactRecyclerAdapter adapter;
    private ReviewEventListener reviewEventListener;

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            reviewEventListener = (ReviewEventListener)context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        setupView(v);
        loadData();
        return v;
    }

    private void setupView(View v) {
        ButterKnife.bind(this, v);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContactRecyclerAdapter(getContext(), dataEvent);
        recyclerView.setAdapter(adapter);
        swipeRefresh.setOnRefreshListener(onRefreshListener);
    }

    public void loadData() {
        showRefreshing();
        String clientID = SharedPreperenceHelper.getInstance(getContext()).getLoginId();
        RetrofitClient.getInstance().getContacts(clientID, dataReceiver);
    }

    private DataEvent<ContactDTO> dataEvent = new DataEvent<ContactDTO>() {
        @Override
        public void onClick(ContactDTO data) {
            Intent intent = new Intent(getContext(), ContactDetailActivity.class);
            intent.putExtra(ContactDTO.class.getName(), data);
            startActivityForResult(intent, RequestType.REQUEST_REVIEW);
        }
    };

    /**
     * 계약서 목록을 불러왔을 때 결과 처리
     * - 성공할 경우
     * 서버에서 불러온 데이터를 보여주도록 처리한다.
     * 로컬에 저장된 계약 내역을 최신화 한다
     * - 실패할 경우
     * 로컬에 저장된 데이터를 보여주도록 처리한다.
     */
    private DataReceiver<ArrayList<ContactDTO>> dataReceiver = new DataReceiver<ArrayList<ContactDTO>>() {
        @Override
        public void onReceive(ArrayList<ContactDTO> data) {
            if(data.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                imageEmpty.setVisibility(View.VISIBLE);
                textEmpty.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                imageEmpty.setVisibility(View.GONE);
                textEmpty.setVisibility(View.GONE);
                SQLiteHelper.getInstance(getContext()).refreshContact(data);
                adapter.setData(data);
            }
            hideRefreshing();
        }

        @Override
        public void onFail() {
            ArrayList<ContactDTO> data = SQLiteHelper.getInstance(getContext()).selectContact();
            if(data.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                imageEmpty.setVisibility(View.VISIBLE);
                textEmpty.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                imageEmpty.setVisibility(View.GONE);
                textEmpty.setVisibility(View.GONE);
                adapter.setData(data);
            }
            hideRefreshing();
            Toast.makeText(getContext(), R.string.fail_to_load_contacts, Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestType.REQUEST_REVIEW) {
            if(resultCode == Activity.RESULT_OK) {
                reviewEventListener.onNewReview();
            }
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
        inflater.inflate(R.menu.menu_estimates, menu);
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
