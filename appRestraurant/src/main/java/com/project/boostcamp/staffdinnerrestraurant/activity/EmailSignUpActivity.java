package com.project.boostcamp.staffdinnerrestraurant.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.project.boostcamp.publiclibrary.api.RetrofitAdmin;
import com.project.boostcamp.publiclibrary.data.AccountType;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.dialog.MyProgressDialog;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.publiclibrary.util.StringHelper;
import com.project.boostcamp.staffdinnerrestraurant.R;

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
    private MyProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_up);
        ButterKnife.bind(this);
        initFirebaseAuth();
    }

    private void initFirebaseAuth() {
        auth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.button_prev)
    public void doPrev(View v) {
        finish();
    }

    /**
     * 다음 버튼을 클릭했을 때 이메일로 가입하기를 진행한다.
     * 파이어베이스에 가입을 시킨후 회원가입으로 넘어간다
     */
    @OnClick(R.id.button_next)
    public void doEmailSignUp() {
        if(checkValidate()) {
            progressDialog = MyProgressDialog.show(getSupportFragmentManager());
            final String email = editEmail.getText().toString();
            final String password = editPassword.getText().toString();
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
     * 회원가입 화면으로 이동하는 함수
     */
    private void moveJoinActivity() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
        Intent intent = new Intent(EmailSignUpActivity.this, JoinActivity.class);
        intent.putExtra(ExtraType.EXTRA_LOGIN_ID, id);
        intent.putExtra(ExtraType.EXTRA_LOGIN_TYPE, type);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
