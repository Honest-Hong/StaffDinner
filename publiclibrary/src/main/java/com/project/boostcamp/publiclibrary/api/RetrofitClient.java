package com.project.boostcamp.publiclibrary.api;

import android.util.Log;

import com.google.gson.Gson;
import com.project.boostcamp.publiclibrary.domain.ClientEstimateDTO;
import com.project.boostcamp.publiclibrary.domain.ContactAddDTO;
import com.project.boostcamp.publiclibrary.domain.ContactDTO;
import com.project.boostcamp.publiclibrary.domain.ResultIntDTO;
import com.project.boostcamp.publiclibrary.domain.TokenRefreshDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hong Tae Joon on 2017-08-01.
 * 고객용 리트로핏 클래스
 */

public class RetrofitClient {
    private static RetrofitClient instance;
    private static final String BASE_URL = "http://52.78.76.86:3000/";
    private Retrofit retrofit;
    public ClientService clientService;
    public static RetrofitClient getInstance() {
        if(instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        clientService = retrofit.create(ClientService.class);
    }

    public void getEstimates(String applicationId, final DataReceiver<ArrayList<ClientEstimateDTO>> dataReceiver) {
        clientService.getEstimateList(applicationId).enqueue(new Callback<ArrayList<ClientEstimateDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<ClientEstimateDTO>> call, Response<ArrayList<ClientEstimateDTO>> response) {
                Log.d("HTJ", "getEstimates: " + new Gson().toJson(response.body()));
                dataReceiver.onReceive(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<ClientEstimateDTO>> call, Throwable t) {
                Log.e("HTJ", "Fail to client get estimate list: " + t.getMessage());
                dataReceiver.onFail();
            }
        });
    }

    /**
     * 계약서 추가 요청
     * @param dto 계약서 신청 데이터
     * @param dataReceiver 요청 결과 이벤트
     */
    public void addContact(ContactAddDTO dto, final DataReceiver<ResultIntDTO> dataReceiver) {
        clientService.addContact(dto).enqueue(new Callback<ResultIntDTO>() {
            @Override
            public void onResponse(Call<ResultIntDTO> call, Response<ResultIntDTO> response) {
                dataReceiver.onReceive(response.body());
            }

            @Override
            public void onFailure(Call<ResultIntDTO> call, Throwable t) {
                dataReceiver.onFail();
            }
        });
    }

    public void getContacts(String id, final DataReceiver<ArrayList<ContactDTO>> dataReceiver) {
        clientService.getContacts(id).enqueue(new Callback<ArrayList<ContactDTO>>() {
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

    public String getAdminImageUrl(String id, int type) {
        return BASE_URL + "images/" + id + "-" + type + ".jpg";
    }

    public void refreshToken(String id, int type, String token) {
        TokenRefreshDTO dto = new TokenRefreshDTO();
        dto.setType(type);
        dto.setToken(token);
        clientService.updateToken(id, dto).enqueue(new Callback<ResultIntDTO>() {
            @Override
            public void onResponse(Call<ResultIntDTO> call, Response<ResultIntDTO> response) {
            }

            @Override
            public void onFailure(Call<ResultIntDTO> call, Throwable t) {
            }
        });
    }
}
