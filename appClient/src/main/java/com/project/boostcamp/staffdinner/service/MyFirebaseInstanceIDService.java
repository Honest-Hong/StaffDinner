package com.project.boostcamp.staffdinner.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;

/**
 * Created by Hong Tae Joon on 2017-07-27.
 * 파이어베이스의 알림 토큰 값을 최신화 해주는 서비스
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String id = SharedPreperenceHelper.getInstance(getApplicationContext()).getLoginId();
        int type = SharedPreperenceHelper.getInstance(getApplicationContext()).getLoginType();
        if(!id.equals("")) {
            RetrofitClient.getInstance().clientService.updateToken(
                    id,
                    type,
                    FirebaseInstanceId.getInstance().getToken());
        }
    }
}
