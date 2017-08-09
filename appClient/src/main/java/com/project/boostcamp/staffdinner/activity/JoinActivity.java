package com.project.boostcamp.staffdinner.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;
import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.domain.ClientJoinDTO;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.publiclibrary.util.StringHelper;
import com.project.boostcamp.staffdinner.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 회원가입을 위해서 기본 정보를 입력받는 액티비티
 */
public class JoinActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
    private View rootView;
    @BindView(R.id.edit_name) EditText editName;
    @BindView(R.id.edit_phone) EditText editPhone;
    @BindView(R.id.button_join) CardView btnJoin;
    private String id;
    private int type;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        if(getIntent() != null) {
            id = getIntent().getStringExtra(ExtraType.EXTRA_ID);
            type = getIntent().getIntExtra(ExtraType.EXTRA_TYPE, -1);
            name = getIntent().getStringExtra(ExtraType.EXTRA_NAME);
        }
        ButterKnife.bind(this);
        setupView();
    }

    private void setupView() {
        rootView = getWindow().getDecorView().getRootView();
        btnJoin.setOnClickListener(this);

        CheckBox checkBox = (CheckBox)findViewById(R.id.check_box);
        checkBox.setOnCheckedChangeListener(this);

        editName.setText(name);
    }

    @Override
    public void onClick(View view) {
        if (checkValidate()) {
            String name = editName.getText().toString();
            String phone = editPhone.getText().toString();
            ClientJoinDTO dto = new ClientJoinDTO();
            dto.setId(id);
            dto.setType(type);
            dto.setName(name);
            dto.setPhone(phone);
            dto.setToken(FirebaseInstanceId.getInstance().getToken());
            RetrofitClient.getInstance().clientService.join(dto).enqueue(joinCallback);
        }
    }

    /**
     * 입력한 값의 유효성을 판단하는 함수
     * @return 모두 유효하면 true
     */
    private boolean checkValidate() {
        String name = editName.getText().toString();
        String phone = editPhone.getText().toString();

        if(name.equals("")) {
            Snackbar.make(rootView, R.string.snack_need_name, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if(phone.equals("")) {
            Snackbar.make(rootView, R.string.snack_need_phone, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if(!StringHelper.isValidCellPhoneNumber(phone)) {
            Snackbar.make(rootView, "올바르지 않은 전화번호 형식입니다.", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 서버에 회원가입 요청 후 콜백 메소드
     * 회원가입을 성공하면 로그인 정보가 반환되어 로컬에 저장하고 메인 액티비티로 넘어간다
     * 실패할 경우 오류를 표시해준다
     */
    private Callback<LoginDTO> joinCallback = new Callback<LoginDTO>() {
        @Override
        public void onResponse(Call<LoginDTO> call, Response<LoginDTO> response) {
            Log.d("HTJ", "join onResponse: " + response.body());
            LoginDTO dto = response.body();
            if(dto.getId() != null) {
                SharedPreperenceHelper.getInstance(JoinActivity.this).saveLogin(dto);
                startMainActivity();
            } else {
                Log.e("HTJ", "Fail to join");
            }
        }

        @Override
        public void onFailure(Call<LoginDTO> call, Throwable t) {
            Log.e("HTJ", "join onFailure: " + t.getMessage());
        }
    };

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 동의합니다 체크박스 변화 이벤트
     * @param compoundButton
     * @param b
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        btnJoin.setVisibility(b
                ? View.VISIBLE
                : View.INVISIBLE);
    }
}
