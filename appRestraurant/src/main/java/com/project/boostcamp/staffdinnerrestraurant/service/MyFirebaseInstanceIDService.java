package com.project.boostcamp.staffdinnerrestraurant.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.project.boostcamp.publiclibrary.api.RetrofitAdmin;
import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.domain.ResultIntDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hong Tae Joon on 2017-07-27.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String loginId = SharedPreperenceHelper.getInstance(getApplicationContext()).getLoginId();
        int loginType = SharedPreperenceHelper.getInstance(getApplicationContext()).getLoginType();
        if(!loginId.equals("")) {
            RetrofitAdmin.getInstance().adminService.updateToken(
                    loginId,
                    loginType,
                    FirebaseInstanceId.getInstance().getToken()).enqueue(new Callback<ResultIntDTO>() {
                @Override
                public void onResponse(Call<ResultIntDTO> call, Response<ResultIntDTO> response) {
                    if(response.body().getResult() == 1) {
                        Log.d("HTJ", "success to token refresh");
                    } else {
                        Log.d("HTJ", "fail to token refresh");
                    }
                }

                @Override
                public void onFailure(Call<ResultIntDTO> call, Throwable t) {
                    Log.e("HTJ", "onFailure onTokenRefresh: " + t.getMessage());
                }
            });
        }
    }
}
