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
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.project.boostcamp.publiclibrary.data.AccountType;
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
    public static final String EXTRA_NOTIFICATION_TYPE = "noti_type";
    public static final int NOTIFICATION_TYPE_NONE = 0x00;
    public static final int NOTIFICATION_TYPE_ESTIMATE = 0x01;
    @BindView(R.id.drawer) DrawerLayout drawer;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.navigation) NavigationView navigationView;
    private MainViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setupToolbar();
        setupTabLayout();
        setupViewPager();
        handleIntent();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkGPS();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = (DrawerLayout)findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupTabLayout() {
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_title_application));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_title_estimate));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_title_contact));
    }

    private void setupViewPager() {
        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.view_pager);
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

    private void handleIntent() {
        int type = getIntent().getIntExtra(EXTRA_NOTIFICATION_TYPE, NOTIFICATION_TYPE_NONE);
        switch(type) {
            case NOTIFICATION_TYPE_NONE:
                viewPager.setCurrentItem(0);
                break;
            case NOTIFICATION_TYPE_ESTIMATE:
                viewPager.setCurrentItem(1);
                break;
            default:
                viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("HTJ", "onConnectionFailed:" + connectionResult.getErrorMessage());
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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

    private void logout() {
        int type = SharedPreperenceHelper.getInstance(this).getLoginType();
        SharedPreperenceHelper.getInstance(this).saveLogin(new LoginDTO());
        if(type == AccountType.TYPE_KAKAO) {
            UserManagement.requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    finish();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
        } else if(type == AccountType.TYPE_FACEBOOK) {
            LoginManager.getInstance().logOut();
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
