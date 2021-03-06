package com.project.boostcamp.staffdinnerrestraurant.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.data.RequestType;
import com.project.boostcamp.publiclibrary.util.PermissionHelper;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.staffdinnerrestraurant.GlideApp;
import com.project.boostcamp.staffdinnerrestraurant.R;

public class SplashActivity extends AppCompatActivity {
    boolean isLogined = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = (ImageView) findViewById(R.id.image_background);
        GlideApp.with(this)
                .load(R.drawable.orange_background)
                .centerCrop()
                .into(imageView);

        if(PermissionHelper.checkAndRequestPermissions(
                this,
                new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE },
                RequestType.REQUEST_PERMISSIONS)) {
            redirectNextActivity();
        }
    }

    private void redirectNextActivity() {
        String loginId = SharedPreperenceHelper.getInstance(this).getLoginId();
        isLogined = !loginId.equals("");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLogined) {
                    handleNotification();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 1000);
    }

    private void handleNotification() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        if(getIntent().getExtras() != null) {
            String type = getIntent().getStringExtra(ExtraType.EXTRA_NOTIFICATION_TYPE);
            if(type != null) {
                intent.putExtra(ExtraType.EXTRA_NOTIFICATION_TYPE, Integer.valueOf(type));
            }
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * 권한 요청이 허가 되면 다음 액티비티로 이동한다.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for(int result: grantResults) {
            if(result == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "필수 권한이 거부 당했습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        redirectNextActivity();
    }
}
