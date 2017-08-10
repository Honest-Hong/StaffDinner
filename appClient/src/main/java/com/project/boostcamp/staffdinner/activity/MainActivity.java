package com.project.boostcamp.staffdinner.activity;

import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.project.boostcamp.publiclibrary.data.AccountType;
import com.project.boostcamp.publiclibrary.data.Application;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.data.NotiType;
import com.project.boostcamp.publiclibrary.dialog.DialogResultListener;
import com.project.boostcamp.publiclibrary.dialog.MyAlertDialog;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.MainViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 메인 액티비티.
 * 신청서, 견적서, 계약서 탭 3가지가 존재한다.
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.drawer) DrawerLayout drawer;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.navigation) NavigationView navigationView;
    private MainViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("HTJ", "token: " + FirebaseInstanceId.getInstance().getToken());

        ButterKnife.bind(this);
        setupToolbar();
        setupTabLayout();
        setupViewPager();
        handleIntent(getIntent());

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    /**
     * 액티비티가 회면에 보여질 때마다 GPS 기능의 On/Off를 체크하여 키도록 유도한다
     */
    @Override
    protected void onStart() {
        super.onStart();
        checkGPS();
    }

    /**
     * 툴바 설정 메소드
     * Drawer를 연결해준다.
     */
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 탭 설정 메소드
     * 신청서, 견적서, 계약 탭 3개를 추가해준다.
     */
    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_title_application));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_title_estimate));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_title_contact));
    }

    /**
     * 뷰 페이저 설정 메소드
     * 뷰페이저의 어뎁터를 설정한다
     * 탭이 3개로 고정되어있기 때문에 Limit을 걸어준다.
     */
    private void setupViewPager() {
        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    /**
     * Noti로 넘어온 Intent를 처리하는 메소드
     * Noti의 Type에 따라서 탭의 위치를 변경시켜준다.
     * 새로운 견적서 알람
     */
    private void handleIntent(Intent intent) {
        if(intent == null) {
            return;
        }
        int type = intent.getIntExtra(ExtraType.EXTRA_NOTIFICATION_TYPE, NotiType.NOTIFICATION_TYPE_NONE);
        switch(type) {
            case NotiType.NOTIFICATION_TYPE_NONE:
                viewPager.setCurrentItem(0);
                break;
            case NotiType.NOTIFICATION_TYPE_ESTIMATE:
                viewPager.setCurrentItem(1);
                break;
            case NotiType.NOTIFICATION_TYPE_CONTACT:
                viewPager.setCurrentItem(2);
                break;
            default:
                viewPager.setCurrentItem(0);
        }
    }

    /**
     * 구글 API 연결 실패 이벤트
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("HTJ", "onConnectionFailed:" + connectionResult.getErrorMessage());
    }

    /**
     * 백버튼을 눌렀을 때, 드로우어가 열려있으면 닫아준다
     */
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 내비게이션 메뉴 선택 이벤트
     * 이벤트를 처리한 후에 내비게이션을 닫아준다.
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_logout:
                logout();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 로그아웃 처리 메소드
     * 로컬에 저장된 로그인 데이터를 삭제시켜준다.
     * 로컬에 저장된 신청서 데이터를 삭제시켜준다.
     * 로그인 타입에 따라서 세션을 제거해준다.
     */
    private void logout() {
        int type = SharedPreperenceHelper.getInstance(this).getLoginType();
        SharedPreperenceHelper.getInstance(this).saveLogin(new LoginDTO());
        SharedPreperenceHelper.getInstance(this).saveApplication(new Application());
        if(type == AccountType.TYPE_KAKAO) {
            UserManagement.requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            });
        } else if(type == AccountType.TYPE_FACEBOOK) {
            LoginManager.getInstance().logOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            finish();
        }
    }

    /**
     * GPS 기능이 켜있는지 확인하고 알람을 띄워 GPS기능을 키도록 유도하는 함수
     */
    private void checkGPS() {
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            MyAlertDialog.newInstance(
                    getString(R.string.dialog_alert_title),
                    getString(R.string.need_gps_on_help),
                    gpsDialogListener)
                    .show(getSupportFragmentManager(), null);
        }
    }

    /**
     * GPS 다이얼로그 결과
     * GPS 기능을 킬 수 있도록 설정으로 연결하거나 아무 행동도 하지 않는다
     */
    private DialogResultListener gpsDialogListener = new DialogResultListener() {
        @Override
        public void onPositive() {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        }

        @Override
        public void onNegative() {
        }
    };
}
