package com.project.boostcamp.publiclibrary.api;

import android.util.Log;

import com.google.gson.Gson;
import com.project.boostcamp.publiclibrary.domain.AdminEstimateDTO;
import com.project.boostcamp.publiclibrary.domain.ContactDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hong Tae Joon on 2017-08-01.
 * 식당용 리트로핏 클래스
 */

public class RetrofitAdmin {
    private static RetrofitAdmin instance;
    private Retrofit retrofit;
    public AdminService adminService;
    public static RetrofitAdmin getInstance() {
        if(instance == null) {
            instance = new RetrofitAdmin();
        }
        return instance;
    }

    public RetrofitAdmin() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://52.78.76.86:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        adminService = retrofit.create(AdminService.class);
    }

    public void getEstimateList(String id, final DataReceiver<ArrayList<AdminEstimateDTO>> dataReceiver) {
        adminService.getEstimate(id).enqueue(new Callback<ArrayList<AdminEstimateDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<AdminEstimateDTO>> call, Response<ArrayList<AdminEstimateDTO>> response) {
                Log.d("HTJ", "admin get estimate list on response: " + new Gson().toJson(response.body()));
                dataReceiver.onReceive(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<AdminEstimateDTO>> call, Throwable t) {
                Log.e("HTJ", "RetrofitAdmin-getEstimates-onFailure: " + t.getMessage());
                dataReceiver.onFail();
            }
        });
    }

    public void getContacts(String id, final DataReceiver<ArrayList<ContactDTO>> dataReceiver) {
        adminService.getContacts(id).enqueue(new Callback<ArrayList<ContactDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<ContactDTO>> call, Response<ArrayList<ContactDTO>> response) {
                Log.d("HTJ", "getContacts onResponse: " + new Gson().toJson(response.body()));
                dataReceiver.onReceive(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<ContactDTO>> call, Throwable t) {
                Log.e("HTJ", "getContacts onFailure: " + t.getMessage());
                dataReceiver.onFail();
            }
        });
    }
}
