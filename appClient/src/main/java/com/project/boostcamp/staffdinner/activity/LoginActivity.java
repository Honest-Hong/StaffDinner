package com.project.boostcamp.staffdinner.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.data.AccountType;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.data.RequestType;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.staffdinner.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 로그인을 하는 액티비티입니다
 * 카카오 로그인
 * 페이스북 로그인
 * 이메일 로그인
 */
public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private String id;
    private int type;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        Session.getCurrentSession().addCallback(callbackKaKao);
        Session.getCurrentSession().checkAndImplicitOpen();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, callbackFacebook);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestType.REQUEST_LOCATION_PERMISSION);
        }
    }

    /**
     * 로그인 버튼 클릭 이벤트
     * 카카오, 페이스북, 이메일
     * @param v
     */
    @OnClick({R.id.text_kakao, R.id.text_facebook, R.id.button_email})
    public void onLoginClick(TextView v) {
        if(v.getId() == R.id.text_kakao) {
            Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this);
        } else if(v.getId() == R.id.text_facebook) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        } else if(v.getId() == R.id.button_email) {
            startActivity(new Intent(this, EmailSignUpActivity.class));
        }
    }

    /**
     * 카카오 또는 페이스북 로그인 결과
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 권한 요청이 허가 되면 맵을 현재 위치로 지정한다
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == RequestType.REQUEST_LOCATION_PERMISSION && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            finish();
        }
    }
    /**
     * 액티비티가 파괴될 때 카카오 로그인 콜백 제거
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callbackKaKao);
    }

    /**
     * 카카오 로그인 콜백
     * 카카오 로그인 성공시 requestMe를 사용하여 이름을 가져온다
     * id값은 카카오에서 제공하는 id이다
     */
    private ISessionCallback callbackKaKao = new ISessionCallback() {
        @Override
        public void onSessionOpened() {
            List<String> propertyKeys = new ArrayList<String>();
            propertyKeys.add("kaccount_email");
            propertyKeys.add("nickname");
            propertyKeys.add("profile_image");
            propertyKeys.add("thumbnail_image");
            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onSuccess(UserProfile result) {
                    id = Long.toString(result.getId());
                    name = result.getNickname();
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

    /**
     * 페이스북 로그인 콜백
     * 페이스북 로그인 성공시 GraphAPI를 사용하여 이름을 가져온다
     * id는 페이스북에서 제공하는 id이다
     */
    private FacebookCallback<LoginResult> callbackFacebook = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("HTJ", "facebook onSuccess");
            Profile.fetchProfileForCurrentAccessToken();
            id = loginResult.getAccessToken().getUserId();
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            Log.d("HTJ", "graph onComplete");
                            try {
                                name = object.getString("name");
                                type = AccountType.TYPE_FACEBOOK;
                                checkAlreadyJoined();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "name");
            request.setParameters(parameters);
            request.executeAsync();
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

    /**
     * 해당 id와 type이 이미 등록되어있는 상태인지 확인하는 함수
     * 이미 등록되어 있으면 로그인 정보를 저장하고 메인화면으로 넘어간다
     * 등록되어있지 않으면 회원가입 화면으로 넘어간다
     */
    private void checkAlreadyJoined() {
        LoginDTO dto = new LoginDTO();
        dto.setId(id);
        dto.setType(type);
        RetrofitClient.getInstance().clientService.login(dto).enqueue(new Callback<LoginDTO>() {
            @Override
            public void onResponse(Call<LoginDTO> call, Response<LoginDTO> response) {
                Log.d("HTJ", "login onResponse: " + response.body());
                LoginDTO dto = response.body();
                if(dto.getId() == null) {
                    Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                    intent.putExtra(ExtraType.EXTRA_LOGIN_ID, id);
                    intent.putExtra(ExtraType.EXTRA_LOGIN_TYPE, type);
                    intent.putExtra(ExtraType.EXTRA_NAME, name);
                    startActivity(intent);
                    finish();
                } else {
                    redirectMainActivity(dto);
                }
            }

            @Override
            public void onFailure(Call<LoginDTO> call, Throwable t) {
                Log.e("HTJ", "login onFailure: " + t.getMessage());
            }
        });
    }

    private void redirectMainActivity(LoginDTO loginDTO) {
        RetrofitClient.getInstance().refreshToken(
                loginDTO.getId(),
                loginDTO.getType(),
                FirebaseInstanceId.getInstance().getToken());
        SharedPreperenceHelper.getInstance(LoginActivity.this).saveLogin(loginDTO);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
