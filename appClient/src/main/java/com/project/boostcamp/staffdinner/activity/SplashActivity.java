package com.project.boostcamp.staffdinner.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.Target;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.data.RequestType;
import com.project.boostcamp.publiclibrary.util.HashkeyHelper;
import com.project.boostcamp.publiclibrary.util.Logger;
import com.project.boostcamp.publiclibrary.util.PermissionHelper;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.staffdinner.GlideApp;
import com.project.boostcamp.staffdinner.R;


/**
 * 앱의 처음화면
 * 앱의 이름과 아이콘을 보여준다
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Logger.i(this, "Hashkey", HashkeyHelper.getHashkey(this));
        GlideApp.with(this)
                .load(R.drawable.green_background)
                .centerCrop()
                .into((ImageView)findViewById(R.id.image_background));

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(1500);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(PermissionHelper.checkAndRequestPermissions(
                        SplashActivity.this,
                        new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_PHONE_STATE },
                        RequestType.REQUEST_PERMISSIONS)) {
                    redirectNextActivity();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        TextView textView = (TextView) findViewById(R.id.text_view);
        imageView.startAnimation(animation);
        textView.startAnimation(animation);
    }

    private void redirectNextActivity() {
        String id = SharedPreperenceHelper.getInstance(this).getLoginId();
        if(!id.isEmpty()) {
            handleNotification();
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
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

    @Override
    public void onBackPressed() {
    }
}
