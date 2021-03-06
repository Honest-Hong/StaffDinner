package com.project.boostcamp.staffdinnerrestraurant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.project.boostcamp.publiclibrary.api.DataReceiver;
import com.project.boostcamp.publiclibrary.api.RetrofitAdmin;
import com.project.boostcamp.publiclibrary.data.AccountType;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.dialog.MyProgressDialog;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;
import com.project.boostcamp.publiclibrary.util.Logger;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.staffdinnerrestraurant.GlideApp;
import com.project.boostcamp.staffdinnerrestraurant.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.image_view) ImageView imageView;
    @BindView(R.id.edit_email) EditText editEmail;
    @BindView(R.id.edit_password) EditText editPassword;
    @BindView(R.id.button_kakao) View btnKaKao;
    @BindView(R.id.button_facebook) View btnFacebook;
    @BindView(R.id.button_email) View btnEmail;
    @BindView(R.id.button_login) View btnLogin;
    @BindView(R.id.button_email_sign_up) View btnEmailSignUp;
    private FirebaseAuth auth;
    private CallbackManager callbackManager;
    private String id;
    private int type;
    private MyProgressDialog progressDialog;
    private boolean showLoginView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        GlideApp.with(this)
                .load(R.drawable.orange_background)
                .centerCrop()
                .into(imageView);
        auth = FirebaseAuth.getInstance();
        Session.getCurrentSession().addCallback(callbackKaKao);
        Session.getCurrentSession().checkAndImplicitOpen();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, callbackFacebook);
    }

    /**
     * 로그인 버튼 클릭 이벤트
     * 카카오, 페이스북, 이메일
     * @param v
     */
    @OnClick({R.id.button_kakao, R.id.button_facebook, R.id.button_email})
    public void onLoginClick(View v) {
        if(v.getId() == R.id.button_kakao) {
            progressDialog = MyProgressDialog.show(getSupportFragmentManager());
            Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this);
        } else if(v.getId() == R.id.button_facebook) {
            progressDialog = MyProgressDialog.show(getSupportFragmentManager());
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        } else if(v.getId() == R.id.button_email) {
            showEmailInput();
        }
    }

    /**
     * 로그인 입력창을 보여주는 과정
     */
    private void showEmailInput() {
        showLoginView = true;
        appearView(editEmail);
        appearView(editPassword);
        appearView(btnLogin);
        appearView(btnEmailSignUp);
        disappearView(btnEmail);
        disappearView(btnKaKao);
        disappearView(btnFacebook);
    }

    /**
     * 로그인 입력창을 보여주는 과정
     */
    private void hideEmailInput() {
        showLoginView = false;
        disappearView(editEmail);
        disappearView(editPassword);
        disappearView(btnLogin);
        disappearView(btnEmailSignUp);
        appearView(btnEmail);
        appearView(btnKaKao);
        appearView(btnFacebook);
    }

    private void appearView(View v) {
        v.setVisibility(View.VISIBLE);
    }

    private void disappearView(View v) {
        v.setVisibility(View.GONE);
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
        RetrofitAdmin.getInstance().login(id, type, loginReceiver);
    }

    private DataReceiver<LoginDTO> loginReceiver = new DataReceiver<LoginDTO>() {
        @Override
        public void onReceive(LoginDTO data) {
            if(data.getId() == null) {
                redirectJoinActivity();
            } else {
                redirectMainActivity(data);
            }
        }

        @Override
        public void onFail() {
        }
    };

    private void redirectJoinActivity() {
        Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
        intent.putExtra(ExtraType.EXTRA_LOGIN_ID, id);
        intent.putExtra(ExtraType.EXTRA_LOGIN_TYPE, type);
        startActivity(intent);
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void redirectMainActivity(LoginDTO loginDTO) {
        RetrofitAdmin.getInstance().refreshToken(loginDTO.getId(), loginDTO.getType(), FirebaseInstanceId.getInstance().getToken());
        SharedPreperenceHelper.getInstance(getApplicationContext()).saveLogin(loginDTO);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.button_login)
    public void doLogin() {
        final String email = editEmail.getText().toString();
        final String password = editPassword.getText().toString();
        if(email.isEmpty() || password.isEmpty()) {
            Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.empty_input_text), Snackbar.LENGTH_LONG).show();
            return;
        }
        progressDialog = MyProgressDialog.show(getSupportFragmentManager());
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onSignInComplete);
    }

    @OnClick(R.id.button_email_sign_up)
    public void doEmailSignUp() {
        startActivity(new Intent(this, EmailSignUpActivity.class));
    }

    private OnCompleteListener<AuthResult> onSignInComplete = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()) {
                Logger.i(this, "onComplete", "isSuccessful");
                id = task.getResult().getUser().getUid();
                type = AccountType.TYPE_EMAIL;
                checkAlreadyJoined();
            } else {
                Logger.i(this, "onComplete", task.getException().getMessage());
                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.not_exist_email_or_password, Snackbar.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
    };

    @Override
    public void onBackPressed() {
        if(showLoginView) {
            hideEmailInput();
        } else {
            super.onBackPressed();
        }
    }
}
