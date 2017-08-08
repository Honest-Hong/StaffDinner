package com.project.boostcamp.staffdinnerrestraurant.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.project.boostcamp.publiclibrary.api.RetrofitAdmin;
import com.project.boostcamp.publiclibrary.data.AccountType;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.staffdinnerrestraurant.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private String id;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        Session.getCurrentSession().addCallback(callbackKaKao);
        Session.getCurrentSession().checkAndImplicitOpen();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, callbackFacebook);
    }

    @OnClick({R.id.text_kakao, R.id.text_facebook, R.id.text_email})
    public void onLoginClick(TextView v) {
        if(v.getId() == R.id.text_kakao) {
            Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this);
        } else if(v.getId() == R.id.text_facebook) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        } else if(v.getId() == R.id.text_email){
            startActivity(new Intent(this, EmailSignUpActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callbackKaKao);
    }

    // 카카오톡 로그인 부분
    private ISessionCallback callbackKaKao = new ISessionCallback() {
        @Override
        public void onSessionOpened() {
            List<String> propertyKeys = new ArrayList<String>();
            propertyKeys.add("kaccount_email");
            propertyKeys.add("profile_image");
            propertyKeys.add("thumbnail_image");
            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onSuccess(UserProfile result) {
                    id = Long.toString(result.getId());
                    type = AccountType.TYPE_KAKAO;
                    checkAlreadyJoined();
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e("HTJ", errorResult.getErrorMessage());
                }

                @Override
                public void onNotSignedUp() {
                    Log.e("HTJ", "onNotSignedUp");
                }
            }, propertyKeys, false);
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Log.e("HTJ", exception.toString());
            }
        }
    };

    // 페이스북 로그인 부분
    private FacebookCallback<LoginResult> callbackFacebook = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("HTJ", "facebook onSuccess");
            Profile.fetchProfileForCurrentAccessToken();
            id = loginResult.getAccessToken().getUserId();
            type = AccountType.TYPE_FACEBOOK;
            checkAlreadyJoined();
        }

        @Override
        public void onCancel() {
            Log.d("HTJ", "facebook onCancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.e("HTJ", "facebook onError: " + error.toString());
        }
    };

    private void checkAlreadyJoined() {
        LoginDTO dto = new LoginDTO();
        dto.setId(id);
        dto.setType(type);
        RetrofitAdmin.getInstance().adminService.login(dto).enqueue(new Callback<LoginDTO>() {
            @Override
            public void onResponse(Call<LoginDTO> call, Response<LoginDTO> response) {
                Log.d("HTJ", "login onResponse: " + response.body().toString());
                if(response.body().getId() == null) {
                    Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                    intent.putExtra(ExtraType.EXTRA_LOGIN_ID, id);
                    intent.putExtra(ExtraType.EXTRA_LOGIN_TYPE, type);
                    startActivity(intent);
                    finish();
                } else {
                    SharedPreperenceHelper.getInstance(getApplicationContext()).saveLogin(response.body());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginDTO> call, Throwable t) {
                Log.e("HTJ", "login onFailure: " + t.getMessage());
            }
        });
    }
}
