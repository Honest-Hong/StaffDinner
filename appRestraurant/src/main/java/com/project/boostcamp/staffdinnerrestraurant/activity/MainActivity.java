package com.project.boostcamp.staffdinnerrestraurant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
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
import com.project.boostcamp.publiclibrary.api.RetrofitAdmin;
import com.project.boostcamp.publiclibrary.data.AccountType;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.data.NotiType;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.adapter.MainViewPagerAdapter;
import com.project.boostcamp.staffdinnerrestraurant.fragment.ApplicationFragment;
import com.project.boostcamp.staffdinnerrestraurant.fragment.ContactFragment;
import com.project.boostcamp.staffdinnerrestraurant.fragment.EstimateFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.drawer) DrawerLayout drawer;
    @BindView(R.id.bottom_nav) BottomNavigationView bottomNav;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.navigation)
    NavigationView navigationView;
    private MainViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

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
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNav.getChildAt(0);
        for(int i=0; i<menuView.getChildCount(); i++) {
            BottomNavigationItemView menu = (BottomNavigationItemView) menuView.getChildAt(i);
            menu.setShiftingMode(false);
            menu.setChecked(false);
        }
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.menu_application:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_estimate:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_contact:
                        viewPager.setCurrentItem(2);
                        break;

                }
                return true;
            }
        });
    }

    private void setupViewPager() {
        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch(position) {
                    case 0:
                        bottomNav.setSelectedItemId(R.id.menu_application);
                        break;
                    case 1:
                        bottomNav.setSelectedItemId(R.id.menu_estimate);
                        break;
                    case 2:
                        bottomNav.setSelectedItemId(R.id.menu_contact);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
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
            case NotiType.NOTIFICATION_TYPE_APPLICATION:
                viewPager.setCurrentItem(0);
                ApplicationFragment applicationFragment = (ApplicationFragment) getSupportFragmentManager().getFragments().get(1);
                applicationFragment.loadData();
                break;
            case NotiType.NOTIFICATION_TYPE_ESTIMATE:
                viewPager.setCurrentItem(1);
                EstimateFragment estimateFragment = (EstimateFragment) getSupportFragmentManager().getFragments().get(2);
                estimateFragment.loadData();
                break;
            case NotiType.NOTIFICATION_TYPE_CONTACT:
                viewPager.setCurrentItem(2);
                ContactFragment contactFragment = (ContactFragment) getSupportFragmentManager().getFragments().get(3);
                contactFragment.loadData();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_logout:
                logout();
                break;
            case R.id.nav_account:
                startActivity(new Intent(this, AccountActivity.class));
                break;
            case R.id.nav_review:
                startActivity(new Intent(this, ReviewsActivity.class));
                break;
            case R.id.nav_osl:
                startActivity(new Intent(this, OSLActivity.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        int loginType = SharedPreperenceHelper.getInstance(this).getLoginType();
        SharedPreperenceHelper.getInstance(this).saveLogin(new LoginDTO());
        if(loginType == AccountType.TYPE_KAKAO) {
            UserManagement.requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    finish();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
        } else if(loginType == AccountType.TYPE_FACEBOOK) {
            LoginManager.getInstance().logOut();
        }
        finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
