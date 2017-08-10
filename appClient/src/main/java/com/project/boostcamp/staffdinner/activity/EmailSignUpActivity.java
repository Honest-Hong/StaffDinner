package com.project.boostcamp.staffdinner.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.data.AccountType;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.publiclibrary.util.StringHelper;
import com.project.boostcamp.staffdinner.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 이메일로 회원가입을 시작하기 위해서 이메일과 비밀번호를 입력받는 액티비티
 */
public class EmailSignUpActivity extends AppCompatActivity {
    @BindView(R.id.edit_email) EditText editEmail;
    @BindView(R.id.edit_password) EditText editPassword;
    @BindView(R.id.edit_password2) EditText editPassword2;
    private FirebaseAuth auth;
    private String id;
    private final int type = AccountType.TYPE_EMAIL;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_up);
        ButterKnife.bind(this);
        initFirebaseAuth();
    }

    /**
     * 파이어베이스 인증 초기화 작업
     */
    private void initFirebaseAuth() {
        auth = FirebaseAuth.getInstance();
    }

    /**
     * 이전 버튼 클릭 작업
     * @param v
     */
    @OnClick(R.id.button_prev)
    public void doPrev(View v) {
        finish();
    }

    /**
     * 다음 버튼을 클릭했을 때 이메일로 가입하기를 진행한다.
     * 이메일과 패스워드가 유효한 값인지를 먼저 판단한다.
     * 파이어베이스에 이미 가입이 되어있으면 서버에 회원정보가 있는지 확인한 후에 다음으로 진행한다
     * 가입이 되어있지 않으면 파이어베이스에 가입을 시킨후 회원가입으로 넘어간다
     */
    @OnClick(R.id.button_next)
    public void doEmailSignUp() {
        if(checkValidate()) {
            progressDialog = ProgressDialog.show(this, "", "잠시만 기다려주세요");
            final String email = editEmail.getText().toString();
            final String password = editPassword.getText().toString();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        id = task.getResult().getUser().getUid();
                        checkAlreadyJoined();
                    } else {
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("HTJ", "doEmailSignUp-onComplete!");
                                if(task.isSuccessful()) {
                                    id = task.getResult().getUser().getUid();
                                    moveJoinActivity();
                                } else {
                                    Log.d("HTJ", "doEmailSignUp warning: " + task.getException());
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    /**
     * 입력한 값의 유효성을 판단하는 함수
     * @return 모두 유효하면 true
     */
    private boolean checkValidate() {
        if(!editPassword.getText().toString().equals(editPassword2.getText().toString())) {
            Toast.makeText(this, "비밀번호 불일치", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!StringHelper.isValidEmail(editEmail.getText().toString())) {
            Toast.makeText(this, "올바르지 않은 이메일 형식", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 서버에 회원정보가 등록되어있는지 확인하는 함수
     * 등록되어있으면 로그인 정보를 로컬에 저장하고 메인으로 넘어간다
     * 그렇지 않으면 회원가입 화면으로 넘어간다
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
                    moveJoinActivity();
                } else {
                    SharedPreperenceHelper.getInstance(EmailSignUpActivity.this).saveLogin(dto);
                    startActivity(new Intent(EmailSignUpActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginDTO> call, Throwable t) {
                Log.e("HTJ", "login onFailure: " + t.getMessage());
            }
        });
    }

    /**
     * 회원가입 화면으로 이동하는 함수
     */
    private void moveJoinActivity() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
        Intent intent = new Intent(EmailSignUpActivity.this, JoinActivity.class);
        intent.putExtra(ExtraType.EXTRA_ID, id);
        intent.putExtra(ExtraType.EXTRA_TYPE, type);
        startActivity(intent);
        finish();
    }
}
