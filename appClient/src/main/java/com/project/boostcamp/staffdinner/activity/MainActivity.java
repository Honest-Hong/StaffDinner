package com.project.boostcamp.staffdinner.activity;

import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.project.boostcamp.publiclibrary.api.DataReceiver;
import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.data.AccountType;
import com.project.boostcamp.publiclibrary.data.Application;
import com.project.boostcamp.publiclibrary.data.ApplicationStateType;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.data.NotiType;
import com.project.boostcamp.publiclibrary.dialog.MyProgressDialog;
import com.project.boostcamp.publiclibrary.domain.ClientDTO;
import com.project.boostcamp.publiclibrary.inter.ContactEventListener;
import com.project.boostcamp.publiclibrary.inter.DialogResultListener;
import com.project.boostcamp.publiclibrary.dialog.MyAlertDialog;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;
import com.project.boostcamp.publiclibrary.inter.GuidePlayer;
import com.project.boostcamp.publiclibrary.inter.ReviewEventListener;
import com.project.boostcamp.publiclibrary.util.Logger;
import com.project.boostcamp.publiclibrary.util.SQLiteHelper;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.publiclibrary.util.StringHelper;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.MainViewPagerAdapter;
import com.project.boostcamp.staffdinner.fragment.ApplicationFragment;
import com.project.boostcamp.staffdinner.fragment.ContactFragment;
import com.project.boostcamp.staffdinner.fragment.EstimateFragment;
import com.project.boostcamp.staffdinner.fragment.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 메인 액티비티.
 * 신청서, 견적서, 계약서 탭 3가지가 존재한다.
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener, ContactEventListener, GuidePlayer, ReviewEventListener{
    public static final int SHOWCASE_DELAY_MILLIS = 150;
    @BindView(R.id.drawer) DrawerLayout drawer;
    @BindView(R.id.bottom_nav) BottomNavigationView bottomNav;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.navigation) NavigationView navigationView;
    private MainViewPagerAdapter pagerAdapter;

    private ShowcaseView showcaseView;
    private int showcaseCount = 0;
    private String[] showcaseTitles;
    private String[] showcaseTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setupToolbar();
        setupTabLayout();
        setupViewPager();
        setupShowcase();
        loadUserInformation();
        handleIntent(getIntent());

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        MyProgressDialog.show(getSupportFragmentManager(), 1500);
    }

    private void setupShowcase() {
        showcaseTitles = getResources().getStringArray(R.array.showcase_titles);
        showcaseTexts = getResources().getStringArray(R.array.showcase_texts);
        showcaseCount = 0;
        if(!SharedPreperenceHelper.getInstance(this).getShownGuide()) {
            SharedPreperenceHelper.getInstance(this).setShownGuide();
            showGuide();
        }
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
     * 홈, 신청서, 견적서, 계약 탭 4개를 추가해준다.
     */
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
                    case R.id.menu_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_application:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_estimate:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.menu_contact:
                        viewPager.setCurrentItem(3);
                        break;

                }
                return true;
            }
        });
    }

    /**
     * 뷰 페이저 설정 메소드
     * 뷰페이저의 어뎁터를 설정한다
     * 탭이 3개로 고정되어있기 때문에 Limit을 걸어준다.
     */
    private void setupViewPager() {
        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch(position) {
                    case 0:
                        bottomNav.setSelectedItemId(R.id.menu_home);
                        break;
                    case 1:
                        bottomNav.setSelectedItemId(R.id.menu_application);
                        break;
                    case 2:
                        bottomNav.setSelectedItemId(R.id.menu_estimate);
                        break;
                    case 3:
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
        Logger.i(this, "handleIntent type", type + "");
        switch(type) {
            case NotiType.NOTIFICATION_TYPE_APPLICATION:
                viewPager.setCurrentItem(1);
                break;
            case NotiType.NOTIFICATION_TYPE_ESTIMATE:
                viewPager.setCurrentItem(2);
                EstimateFragment estimateFragment = (EstimateFragment) getSupportFragmentManager().getFragments().get(3);
                estimateFragment.loadData();
                break;
            case NotiType.NOTIFICATION_TYPE_CONTACT:
                viewPager.setCurrentItem(3);
                ContactFragment contactFragment = (ContactFragment) getSupportFragmentManager().getFragments().get(4);
                contactFragment.loadData();
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
     * 쇼케이스 뷰가 보여지고있으면 끝내준다
     */
    private long backPressedTime = 0;
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(showcaseView != null && showcaseView.isShowing()) {
            showcaseView.hide();
            showcaseCount = 0;
        } else if(System.currentTimeMillis() - backPressedTime < 2000){
            Toast.makeText(this, R.string.one_more_click_for_exit, Toast.LENGTH_SHORT).show();
            backPressedTime = System.currentTimeMillis();
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
            case R.id.nav_logout:
                logout();
                break;
            case R.id.nav_account:
                startActivity(new Intent(this, AccountActivity.class));
                break;
            case R.id.nav_open_source_license:
                startActivity(new Intent(this, OSLActivity.class));
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
        SQLiteHelper.getInstance(this).removeContacts();
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
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
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

    /**
     * 계약 발생 이벤트
     * 신청서 프래그먼트를 업데이트 시켜준다
     *
     * 왜 인덱스를 1로해야하는가?
     * 인덱스 0은 무슨 프래그먼트인가?
     */
    @Override
    public void onContact() {
        viewPager.setCurrentItem(1);
        ApplicationFragment appFragment = (ApplicationFragment) getSupportFragmentManager().getFragments().get(2);
        appFragment.setState(ApplicationStateType.STATE_CONTACTED);
        ContactFragment contactFragment = (ContactFragment) getSupportFragmentManager().getFragments().get(4);
        contactFragment.loadData();
    }

    /**
     * 가이드 실행
     * 탭의 0번째 부터 3번째 까지 모두 설명해준다.
     */
    @Override
    public void showGuide() {
        if(showcaseCount < 4) {
            viewPager.setCurrentItem(showcaseCount);
            // 탭의 위치를 올바르게 가르키기 위한 딜레이
            viewPager.postDelayed(showcaseTask, SHOWCASE_DELAY_MILLIS);
        } else {
            viewPager.setCurrentItem(0);
            showcaseCount = 0;
        }
    }

    /**
     * 쇼케이스를 띄워주는 행동
     */
    private Runnable showcaseTask = new Runnable() {
        @Override
        public void run() {
            if(showcaseView != null && showcaseView.isShowing()) {
                showcaseView.hide();
            }
            View target = ((ViewGroup)bottomNav.getChildAt(0)).getChildAt(showcaseCount);
            showcaseView = new ShowcaseView.Builder(MainActivity.this)
                    .setTarget(new ViewTarget(target))
                    .setContentTitle(showcaseTitles[showcaseCount])
                    .setContentText(showcaseTexts[showcaseCount])
                    .setOnClickListener(showcaseClick)
                    .setStyle(R.style.CustomShowcaseTheme)
                    .build();
            showcaseView.show();
        }
    };

    /**
     * 쇼케이스 내부의 버튼 클릭 리스너
     */
    private View.OnClickListener showcaseClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showcaseView.hide();
            showcaseCount++;
            showGuide();
        }
    };

    private void loadUserInformation() {
        String id = SharedPreperenceHelper.getInstance(this).getLoginId();
        int type = SharedPreperenceHelper.getInstance(this).getLoginType();
        RetrofitClient.getInstance().getUserInformation(id, type, userDataReceiver);
    }

    private DataReceiver<ClientDTO> userDataReceiver = new DataReceiver<ClientDTO>() {
        @Override
        public void onReceive(ClientDTO data) {
            setupNavigation(data.getName(), data.getPhone());
        }

        @Override
        public void onFail() {
        }
    };

    /**
     * 네비게이션 텍스트 설정 메소드
     * @param name 사용자 이름
     * @param phone 사용자 전화번호
     */
    private void setupNavigation(String name, String phone) {
        View v = navigationView.getHeaderView(0);
        TextView textName = (TextView) v.findViewById(R.id.text_name);
        TextView textPhone = (TextView) v.findViewById(R.id.text_phone);
        textName.setText(name);
        textPhone.setText(StringHelper.toPhoneNumber(phone));
    }

    /**
     * 새로운 리뷰 추가 발생시 처리
     */
    @Override
    public void onNewReview() {
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().getFragments().get(1);
        fragment.onNewReview();
    }
}
