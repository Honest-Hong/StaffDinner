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
import com.project.boostcamp.publiclibrary.sqlite.SQLiteHelper;
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
    private LinearLayoutManager linearLayoutManager;
    private ReviewEventListener reviewEventListener;
    private boolean isLoading = false;
    private boolean dataEnded = false;
    private int page = 0;

    public static ContactFragment newInstance() {
        return new ContactFragment();
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
        loadFirstData();
        return v;
    }

    private void setupView(View v) {
        ButterKnife.bind(this, v);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ContactRecyclerAdapter(getContext(), dataEvent);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int total = linearLayoutManager.getItemCount();
                final int last = linearLayoutManager.findLastVisibleItemPosition();
                if(!isLoading && !dataEnded && total == (last + 1)) {
                    isLoading = true;
                    page++;
                    loadMoreData(page);
                }
            }
        });
        swipeRefresh.setOnRefreshListener(onRefreshListener);
    }

    /**
     * 상단의 데이터를 최신화 하는 작업
     */
    public void loadFirstData() {
        page = 0;
        dataEnded = false;
        showRefreshing();
        String clientID = SharedPreperenceHelper.getInstance(getContext()).getLoginId();
        RetrofitClient.getInstance().getContacts(clientID, 0, dataReceiver);
    }

    /**
     * 더 많은 내역을 불러오는 작업(하단)
     * @param page
     */
    public void loadMoreData(int page) {
        String clientID = SharedPreperenceHelper.getInstance(getContext()).getLoginId();
        RetrofitClient.getInstance().getContacts(clientID, page, moreDataReceiver);
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
                data.add(null);
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
        }
    };

    /**
     * 하단에서 데이터를 추가로 불러오는 요청
     */
    private DataReceiver<ArrayList<ContactDTO>> moreDataReceiver = new DataReceiver<ArrayList<ContactDTO>>() {
        @Override
        public void onReceive(ArrayList<ContactDTO> data) {
            ArrayList<ContactDTO> arr = adapter.getData();
            if(data.size() != 0) {
                final int lastIndex = arr.size() - 1; // 마지막은 null임
                for (int i = 0; i < data.size(); i++) {
                    arr.add(lastIndex + i, data.get(i));
                    adapter.notifyItemInserted(lastIndex + i);
                }
            } else {
                arr.remove(arr.size() - 1);
                adapter.notifyItemRemoved(arr.size());
                dataEnded = true;
            }
            isLoading = false;
        }

        @Override
        public void onFail() {
            Toast.makeText(getContext(), "데이터 로딩 실패", Toast.LENGTH_SHORT).show();
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
        inflater.inflate(R.menu.menu_contacts, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_refresh:
                loadFirstData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
