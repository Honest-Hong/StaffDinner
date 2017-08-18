package com.project.boostcamp.staffdinnerrestraurant.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.boostcamp.publiclibrary.api.DataReceiver;
import com.project.boostcamp.publiclibrary.api.RetrofitAdmin;
import com.project.boostcamp.publiclibrary.dialog.MyProgressDialog;
import com.project.boostcamp.publiclibrary.domain.ReviewAverageDTO;
import com.project.boostcamp.publiclibrary.domain.ReviewDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.publiclibrary.util.TimeHelper;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.adapter.ReviewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ReviewsActivity extends AppCompatActivity {
    @BindView(R.id.text_review_average) TextView textAverage;
    @BindView(R.id.text_review_number) TextView textPeopleNumber;
    @BindView(R.id.rating_review_average) MaterialRatingBar ratingBar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private boolean averageLoaded = false;
    private boolean reviewsLoaded = false;
    private MyProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = MyProgressDialog.show(getSupportFragmentManager());
        String id = SharedPreperenceHelper.getInstance(this).getLoginId();
        int type = SharedPreperenceHelper.getInstance(this).getLoginType();
        RetrofitAdmin.getInstance().getAdminReviewAverage(id, type, averageDataReceiver);
        RetrofitAdmin.getInstance().getAdminReviews(id, type, reviewsReceiver);
    }

    private DataReceiver<ReviewAverageDTO> averageDataReceiver = new DataReceiver<ReviewAverageDTO>() {
        @Override
        public void onReceive(ReviewAverageDTO data) {
            textAverage.setText(String.format("%1$.1f", data.getAverage()));
            textPeopleNumber.setText(getString(R.string.person_number, data.getCount()));
            ratingBar.setProgress((int)(data.getAverage() * 2));
            averageLoaded = true;
            hideProgressBar();
        }

        @Override
        public void onFail() {
            averageLoaded = true;
            hideProgressBar();
        }
    };

    private DataReceiver<ArrayList<ReviewDTO>> reviewsReceiver = new DataReceiver<ArrayList<ReviewDTO>>() {
        @Override
        public void onReceive(ArrayList<ReviewDTO> data) {
            setupReview(data);
            reviewsLoaded = true;
            hideProgressBar();
        }

        @Override
        public void onFail() {
            reviewsLoaded = true;
            hideProgressBar();
        }
    };

    private void setupReview(ArrayList<ReviewDTO> data) {
        adapter = new ReviewAdapter(this, data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void hideProgressBar() {
        if(reviewsLoaded && averageLoaded && progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
