package com.project.boostcamp.publiclibrary.api;

import android.util.Log;

import com.google.gson.Gson;
import com.project.boostcamp.publiclibrary.domain.AdminEstimateDTO;
import com.project.boostcamp.publiclibrary.domain.AdminJoinDTO;
import com.project.boostcamp.publiclibrary.domain.ContactDTO;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;
import com.project.boostcamp.publiclibrary.domain.ResultIntDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public void join(AdminJoinDTO dto, final DataReceiver<LoginDTO> dataReceiver) {
        adminService.join(dto).enqueue(new Callback<LoginDTO>() {
            @Override
            public void onResponse(Call<LoginDTO> call, Response<LoginDTO> response) {
                Log.d("HTJ", "join onResponse: " + response.body());
                dataReceiver.onReceive(response.body());
            }

            @Override
            public void onFailure(Call<LoginDTO> call, Throwable t) {
                Log.e("HTJ", "join onFailure: " + t.getMessage());
                dataReceiver.onFail();
            }
        });
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

    public void setImage(String id, int type, File file, final DataReceiver<ResultIntDTO> dataReceiver) {
        // TODO: 2017-08-08 이미지를 작게 만들 필요가 있다
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        adminService.setImage(id, type, body).enqueue(new Callback<ResultIntDTO>() {
            @Override
            public void onResponse(Call<ResultIntDTO> call, Response<ResultIntDTO> response) {
                Log.d("HTJ", "[Admin] setImage onResponse: " + new Gson().toJson(response.body()));
                dataReceiver.onReceive(response.body());
            }

            @Override
            public void onFailure(Call<ResultIntDTO> call, Throwable t) {
                Log.d("HTJ", "[Admin] setImage onFailure: " + t.getMessage());
                dataReceiver.onFail();
            }
        });
    }
}
