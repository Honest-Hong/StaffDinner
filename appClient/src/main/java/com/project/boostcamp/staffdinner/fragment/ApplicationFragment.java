package com.project.boostcamp.staffdinner.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.boostcamp.publiclibrary.api.RetrofitClient;
import com.project.boostcamp.publiclibrary.data.ApplicationStateType;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.data.Geo;
import com.project.boostcamp.publiclibrary.data.RequestType;
import com.project.boostcamp.publiclibrary.dialog.ArrayResultListener;
import com.project.boostcamp.publiclibrary.domain.ClientApplicationDTO;
import com.project.boostcamp.publiclibrary.domain.ResultIntDTO;
import com.project.boostcamp.publiclibrary.domain.ResultStringDTO;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.publiclibrary.data.Application;
import com.project.boostcamp.staffdinner.activity.MapDetailActivity;
import com.project.boostcamp.staffdinner.adapter.TextWheelAdapter;
import com.project.boostcamp.publiclibrary.dialog.DialogResultListener;
import com.project.boostcamp.publiclibrary.dialog.MyAlertDialog;
import com.project.boostcamp.publiclibrary.view.WheelPicker;
import com.project.boostcamp.publiclibrary.util.TimeHelper;
import com.project.boostcamp.publiclibrary.util.GeocoderHelper;
import com.project.boostcamp.publiclibrary.util.MarkerBuilder;
import com.project.boostcamp.publiclibrary.util.SharedPreperenceHelper;
import com.project.boostcamp.publiclibrary.util.StringHelper;
import com.project.boostcamp.staffdinner.dialog.StyleSelectDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hong Tae Joon on 2017-07-25.
 * 신청서 프래그먼트
 * 현재의 신청서를 보여주는 화면이다
 * 신청서를 작성하거나 취소할 수 있다.
 */

public class ApplicationFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, DialogResultListener {
    public static final int MAX_NUMBER = 99;
    public static final int MIN_NUMBER = 1;
    public static final int MAX_HOUR = 24;
    public static final int MAX_MINUTE = 60;
    public static final int MAX_DATE = 14;
    @BindView(R.id.scroll_view) NestedScrollView scrollView;
    @BindView(R.id.image_state) ImageView imageState; // 상단의 신청서 상태 이미지
    @BindView(R.id.text_state) TextView textState; // 상단의 신청서 상태 텍스트
    @BindView(R.id.edit_title) EditText editTitle; // 신청서의 제목 입력창
    @BindView(R.id.edit_number) EditText editNumber; // 신청서의 인원 입력창
    @BindView(R.id.text_style) TextView textStyle;
    @BindView(R.id.edit_menu) EditText editMenu; // 신청서의 메뉴 입력창
    @BindView(R.id.text_location) TextView textLocation; // 신청서의 위치 텍스트
    @BindView(R.id.button_apply) Button btnApply; // 신청 버튼
    @BindView(R.id.button_up) ImageButton btnUp;
    @BindView(R.id.button_down) ImageButton btnDown;
    @BindView(R.id.button_search) ImageButton btnSearch;
    @BindView(R.id.wheel_hour) WheelPicker wheelHour; // 선청서의 시간 선택 도구
    @BindView(R.id.wheel_minute) WheelPicker wheelMinute; // 신청서의 분 선택 도구
    @BindView(R.id.wheel_date) WheelPicker wheelDate;
    private TextWheelAdapter wheelAdapterHour; //  신청서의 시간 뷰 어댑터
    private TextWheelAdapter wheelAdapterMinute; // 신청서의 분 뷰 어댑터
    private TextWheelAdapter wheelAdapterDate;
    private GoogleMap googleMap; // 신청서의 위치 지도
    private Marker marker; // 신청서의 위치 지도 마커
    private FusedLocationProviderClient fusedLocationClient; // 현재 위치를 가져오는 서비스
    private Application application = new Application(); // 현재 신청서 정보

    private ArrayList<String> styles;

    public static ApplicationFragment newInstance() {
        return new ApplicationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_application, container, false);
        ButterKnife.bind(this, v);
        setupView(v);
        setupWheel(v);
        loadApplication();
        return v;
    }

    private void setupView(View v) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        styles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.styles)));

        // TODO: 2017-07-28 키보드로 잘못 된 입력 예외 처리 하기
        // TODO: 2017-07-31 분위기를 선택하면서 해시태깅을 하도록 추가!
    }

    /**
     * 구글맵이 준비가 되면 지도를 움직일 수 없도록 한다
     * 기본적으로 현재 위치를 보여준다
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setScrollGesturesEnabled(false);
        uiSettings.setZoomGesturesEnabled(false);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.4973932,127.02744170000005), 16));

        // TODO: 2017-08-04 신청서가 존재하는 경우 현재 위치로 하지 않도록 하기
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, RequestType.REQUEST_LOCATION_PERMISSION);
        } else {
            setMyLocation();
        }
    }

    /**
     * 권한 요청이 허가 되면 맵을 현재 위치로 지정한다
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == RequestType.REQUEST_LOCATION_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setMyLocation();
        }
    }

    /**
     * 현재 위치를 가져와서 지도에 표시해주는 함수
     */
    private void setMyLocation() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && application.getGeo() != null) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null) {
                        LatLng latLng = new LatLng(
                                location.getLatitude(),
                                location.getLongitude());
                        setLocation(latLng);
                    } else {
                        textLocation.setText(R.string.text_no_address);
                    }
                }
            });
        }
    }

    private void setupWheel(View v) {
        wheelAdapterHour = new TextWheelAdapter();
        ArrayList<String> hours = new ArrayList<>();
        for(int i = 0; i< MAX_HOUR; i++) {
            hours.add(String.format("%02d", i));
        }
        wheelAdapterHour.setData(hours);
        wheelHour.setAdapter(wheelAdapterHour);

        wheelAdapterMinute = new TextWheelAdapter();
        ArrayList<String> minutes = new ArrayList<>();
        for(int i = 0; i< MAX_MINUTE; i+= 10) {
            minutes.add(String.format("%02d", i));
        }
        wheelAdapterMinute.setData(minutes);
        wheelMinute.setAdapter(wheelAdapterMinute);

        wheelAdapterDate = new TextWheelAdapter();
        ArrayList<String> dates = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        for(int i = 0; i< MAX_DATE; i++) {
            dates.add(String.format("%02d/%02d", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE)));
            calendar.add(Calendar.DATE, 1);
        }
        wheelAdapterDate.setData(dates);
        wheelDate.setAdapter(wheelAdapterDate);
    }

    /**
     * 신청서의 데이터를 불러옵니다.
     * 서버에서 데이터를 불러올 수 있다면 서버의 데이터를 사용하고
     * 불러올 수 없다면 로컬 데이터를 사용합니다.
     */
    private void loadApplication() {
        String id = SharedPreperenceHelper.getInstance(getContext()).getLoginId();
        RetrofitClient.getInstance().clientService.getApplication(id).enqueue(new Callback<ClientApplicationDTO>() {
            @Override
            public void onResponse(Call<ClientApplicationDTO> call, Response<ClientApplicationDTO> response) {
                Log.d("HTJ", "ApplicationFragment-loadApplication-onReponse: " + response.body());
                ClientApplicationDTO dto = response.body();
                application = new Application();
                if(dto.get_id() != null) {
                    application.setId(dto.get_id());
                    application.setTitle(dto.getTitle());
                    application.setNumber(dto.getNumber());
                    application.setWantedTime(dto.getTime());
                    application.setWantedStyle(dto.getStyle());
                    application.setWantedMenu(dto.getMenu());
                    application.setGeo(dto.getGeo().toGeo());
                    application.setState(ApplicationStateType.STATE_APPLIED);
                }
                setupTexts(application);
            }

            @Override
            public void onFailure(Call<ClientApplicationDTO> call, Throwable t) {
                Log.e("HTJ", "ApplicattionFragment-loadApplication-onFailuer: " + t.getMessage());
                application = SharedPreperenceHelper.getInstance(getContext()).getApply();
                if(application == null) {
                    application = new Application();
                }
                setupTexts(application);
            }
        });
    }

    /**
     * 신청서 데이터를 뷰에 뿌려주는 함수
     * @param application 신청서 데이터
     */
    private void setupTexts(Application application) {
        if(application.getNumber() == 0) {
            application.setNumber(1);
        }
        if(application.getWantedTime() == 0) {
            application.setWantedTime(TimeHelper.now());
        }

        editTitle.setText(application.getTitle());
        editNumber.setText(application.getNumber() + "");
        textStyle.setText(application.getWantedStyle());
        editMenu.setText(application.getWantedMenu());
        wheelHour.setSelectedIndex(TimeHelper.getHour(application.getWantedTime()));
        wheelMinute.setSelectedIndex(TimeHelper.getMinute(application.getWantedTime()) / 10);
        // TODO: 2017-08-03 정확한 날짜를 가리키도록 하기
        setState(application.getState());
        // TODO: 2017-07-28 저장된 위치 맵에 출력하기
    }

    @OnClick({R.id.button_up, R.id.button_down, R.id.button_apply, R.id.button_search, R.id.button_style})
    public void onClick(View view) {
        int number = application.getNumber();
        switch(view.getId()) {
            case R.id.button_up:
                if(number < MAX_NUMBER) {
                    number++;
                    editNumber.setText(Integer.toString(number));
                    application.setNumber(number);
                }
                break;
            case R.id.button_down:
                if(number > MIN_NUMBER) {
                    number--;
                    editNumber.setText(Integer.toString(number));
                    application.setNumber(number);
                }
                break;
            case R.id.button_apply:
                handleApplyButton();
                break;
            case R.id.button_search:
                Intent intentMap = new Intent(getContext(), MapDetailActivity.class);
                intentMap.putExtra(ExtraType.EXTRA_LATITUDE, googleMap.getCameraPosition().target.latitude);
                intentMap.putExtra(ExtraType.EXTRA_LONGITUDE, googleMap.getCameraPosition().target.longitude);
                intentMap.putExtra(ExtraType.EXTRA_READ_ONLY, false);
                startActivityForResult(intentMap, RequestType.REQUEST_LOCATION);
                break;
            case R.id.button_style:
                StyleSelectDialog dialog = StyleSelectDialog.newInstance(styleResult);
                dialog.show(getFragmentManager(), null);
                break;
        }
    }

    /**
     * 하단의 보내기 버튼을 눌렀을 때 처리해주는 함수
     * 작성중 상태의 경우 신청서를 보낼건지에 대해 다이얼로그가 나타난다
     * 신청됨 상태의 경우 신청서를 취소할건지에 대해 다이얼로그가 나타난다
     * 취소됨 상태의 경우 신청서를 다시 작성하도록 입력창이 비워진다
     */
    private void handleApplyButton() {
        if(application.getState() == ApplicationStateType.STATE_EDITING) {
            MyAlertDialog.newInstance(getString(R.string.dialog_alert_title), getString(R.string.dialog_apply_message), this)
                    .show(getChildFragmentManager(), null);
        } else if(application.getState() == ApplicationStateType.STATE_APPLIED) {
            MyAlertDialog.newInstance(getString(R.string.dialog_alert_title), getString(R.string.dialog_cancel_message), this)
                    .show(getChildFragmentManager(), null);
        } else {
            application = new Application();
            setupTexts(application);
            setState(ApplicationStateType.STATE_EDITING);
        }
    }

    /**
     * 다이얼로그 결과 콜백 함수 (예 버튼)
     */
    @Override
    public void onPositive() {
        if(application.getState() == ApplicationStateType.STATE_EDITING) {
            submitApplication();
        } else {
            cancelApplication();
        }
        scrollView.smoothScrollTo(0,0);
    }

    /**
     * 다이얼로그 결과 콜백 함수 (아니오 버튼)
     */
    @Override
    public void onNegative() {
    }

    /**
     * 경도와 위도를 전달하면 맵의 위치를 변경시켜주는 함수
     * @param latLng 경도와 위도
     */
    private void setLocation(LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        marker = googleMap.addMarker(MarkerBuilder.simple(latLng));
        String add = GeocoderHelper.getAddress(getContext(), latLng);
        textLocation.setText(StringHelper.cutStart(add, 18));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestType.REQUEST_LOCATION) {
            if(resultCode == Activity.RESULT_OK) {
                LatLng latLng = new LatLng(
                        data.getDoubleExtra(ExtraType.EXTRA_LATITUDE, 0)
                        , data.getDoubleExtra(ExtraType.EXTRA_LONGITUDE, 0));
                if(marker != null) {
                    marker.remove();
                }
                setLocation(latLng);
            }
        }
    }

    /**
     * 신청서를 등록하는 함수
     */
    public void submitApplication() {
        String appId = application.getId();
        application = getApplicationFromEditText(appId);
        // 로컬에 저장
        SharedPreperenceHelper.getInstance(getContext()).saveApplication(application);

        // 서버에 전달
        ClientApplicationDTO dto = new ClientApplicationDTO();
        dto.setTitle(application.getTitle());
        dto.setNumber(application.getNumber());
        dto.setTime(application.getWantedTime());
        dto.setStyle(application.getWantedStyle());
        dto.setMenu(application.getWantedMenu());
        dto.setGeo(application.getGeo().toGeoDTO());
        dto.setWritedTime(TimeHelper.now());
        String clientId = SharedPreperenceHelper.getInstance(getContext()).getLoginId();
        RetrofitClient.getInstance().clientService.setApplication(clientId, dto).enqueue(new Callback<ResultStringDTO>() {
            @Override
            public void onResponse(Call<ResultStringDTO> call, Response<ResultStringDTO> response) {
                Log.d("HTJ", "ApplicationFragment-submitApplication-onResponse: " + response.body());
                if(response.body().getResult() != null) {
                    application.setId(response.body().getResult());
                    SharedPreperenceHelper.getInstance(getContext()).saveApplication(application);
                    setState(ApplicationStateType.STATE_APPLIED);;
                } else {
                    Log.d("HTJ", "Not receive application id");
                }
            }

            @Override
            public void onFailure(Call<ResultStringDTO> call, Throwable t) {
                Log.e("HTJ", "ApplicationFragment-submitApplication-onFailure: " + t.getMessage());
            }
        });
    }

    /**
     * 현재 입력되어있는 값들을 Application 객체로 반환시켜주는 함수
     * @param appId 신청서 아이디
     */
    private Application getApplicationFromEditText(String appId) {
        Application application = new Application();
        // 데이터 최신화 작업
        application.setId(appId);
        application.setTitle(editTitle.getText().toString());
        application.setNumber(Integer.parseInt(editNumber.getText().toString()));
        String hour = wheelAdapterHour.getItem(wheelHour.getSelectedIndex());
        String minute = wheelAdapterMinute.getItem(wheelMinute.getSelectedIndex());
        String date = wheelAdapterDate.getItem(wheelDate.getSelectedIndex());
        application.setWantedTime(TimeHelper.getTime(
                Integer.parseInt(date.substring(0,2)) - 1,
                Integer.parseInt(date.substring(3,5)),
                Integer.parseInt(hour)
                , Integer.parseInt(minute)));
        application.setWantedStyle(textStyle.getText().toString());
        application.setWantedMenu(editMenu.getText().toString());
        application.setGeo(new Geo("Point",
                marker.getPosition().longitude,
                marker.getPosition().latitude));
        application.setWritedTime(TimeHelper.now());
        application.setState(ApplicationStateType.STATE_APPLIED);
        return application;
    }

    /**
     * 신청서를 취소하는 함수
     */
    private void cancelApplication() {
        Log.d("HTJ", "cancelApplication: " + application.getId());
        RetrofitClient.getInstance().clientService.cancelApplication(application.getId()).enqueue(new Callback<ResultIntDTO>() {
            @Override
            public void onResponse(Call<ResultIntDTO> call, Response<ResultIntDTO> response) {
                // 취소 성공
                Log.d("HTJ", "ApplicationFragment-cancelApplication-onResponse: " + response.body());
                if(response.body().getResult() == 1) {
                    application = new Application();
                    setupTexts(application);
                    application.setState(ApplicationStateType.STATE_EDITING);
                    setState(ApplicationStateType.STATE_EDITING);
                    SharedPreperenceHelper.getInstance(getContext()).saveApplication(application);
                } else {
                    Log.e("HTJ", "Fail to canceling application");
                }
            }

            @Override
            public void onFailure(Call<ResultIntDTO> call, Throwable t) {
                // 취소 실패
                Log.e("HTJ", "ApplicationFragment-cancelApplication-onFailure: " + t.getMessage());
                Toast.makeText(getContext(), "서버 오류", Toast.LENGTH_SHORT).show();
                application = new Application();
                setupTexts(application);
                application.setState(ApplicationStateType.STATE_EDITING);
                setState(ApplicationStateType.STATE_EDITING);
                SharedPreperenceHelper.getInstance(getContext()).saveApplication(application);
            }
        });
    }

    /**
     * 신청서의 상태에 따라서 화면을 다르게 표시해주는 함수
     * @param state
     */
    private void setState(int state) {
        switch(state) {
            case ApplicationStateType.STATE_EDITING:
                btnApply.setText(R.string.text_apply);
                imageState.setImageResource(R.drawable.ic_error_orange_24dp);
                textState.setText(R.string.text_need_input);
                textState.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow));
                blockView(false);
                break;
            case ApplicationStateType.STATE_APPLIED:
                btnApply.setText(R.string.text_cancel);
                imageState.setImageResource(R.drawable.ic_check_circle_green_24dp);
                textState.setText(R.string.text_apply_success);
                textState.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                blockView(true);
                break;
            case ApplicationStateType.STATE_CANCELED:
                btnApply.setText(R.string.text_rewrite);
                imageState.setImageResource(R.drawable.ic_cancel_red_24dp);
                textState.setText(R.string.text_apply_canceled);
                textState.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                blockView(true);
                break;
        }
    }

    /**
     * 스타일 선택 다이얼로그의 결과를 받는 인터페이스
     */
    private ArrayResultListener<String> styleResult = new ArrayResultListener<String>() {
        @Override
        public void onResult(ArrayList<String> array) {
            String str = "";
            for(int i=0; i<array.size(); i++) {
                str += array.get(i);
                if(i < array.size() - 1) {
                    str += ", ";
                }
            }
            textStyle.setText(str);
        }
    };

    /**
     * 입력 폼을 사용 불가능하게 하는 함수
     * @param block
     */
    private void blockView(boolean block) {
        editTitle.setEnabled(!block);
        editNumber.setEnabled(!block);
        wheelHour.setEnableScroll(!block);
        wheelMinute.setEnableScroll(!block);
        wheelDate.setEnableScroll(!block);
//        editStyle.setEnabled(!block);
        textStyle.setEnabled(!block);
        editMenu.setEnabled(!block);
        editMenu.setEnabled(!block);
        btnUp.setEnabled(!block);
        btnDown.setEnabled(!block);
        btnSearch.setEnabled(!block);
    }
}
