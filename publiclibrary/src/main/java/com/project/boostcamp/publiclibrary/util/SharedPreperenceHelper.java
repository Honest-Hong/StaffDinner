package com.project.boostcamp.publiclibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.project.boostcamp.publiclibrary.data.Application;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.domain.GeoDTO;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;

/**
 * Created by Hong Tae Joon on 2017-07-28.
 * SharedPreperence를 쉽게 사용하는 도구
 */

public class SharedPreperenceHelper {
    // 데이터 이름
    private static final String PREFERENCE_NAME = "StaffDinner";
    // 싱글톤 인스턴스
    private static SharedPreperenceHelper instance;
    // 실제 SharedPreferences 인스턴스
    private SharedPreferences preferences;
    // 지슨 객체
    private Gson gson;

    /**
     * 인스턴스 가져오기
     * @param context 컨텍스트
     * @return 싱글톤 인스턴스
     */
    public static SharedPreperenceHelper getInstance(Context context) {
        if(instance == null) {
            instance = new SharedPreperenceHelper(context);
        }
        return instance;
    }

    /**
     * 기본 생성자
     * @param context
     */
    private SharedPreperenceHelper(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    /**
     * 신청서 정보를 로컬에 저장하는 함수
     * @param application 신청서 정보
     */
    public void saveApplication(Application application) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Application.class.getName(), gson.toJson(application));
        editor.apply();
    }

    /**
     * 로컬에 저장되어있는 신청서를 받아오는 함수
     * @return 신청서 정보
     */
    public Application getApply() {
        String str = preferences.getString(Application.class.getName(), "");
        return gson.fromJson(str, Application.class);
    }

    /**
     * 로그인 정보를 로컬에 저장하는 함수
     * @param loginDTO 로그인 정보
     */
    public void saveLogin(LoginDTO loginDTO) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ExtraType.EXTRA_LOGIN_ID, loginDTO.getId());
        editor.putInt(ExtraType.EXTRA_LOGIN_TYPE, loginDTO.getType());
        editor.apply();
    }

    /**
     * 로컬에 저장된 로그인 아이디를 가져오는 함수
     * @return 로그인 아이디
     */
    public String getLoginId() {
        return preferences.getString(ExtraType.EXTRA_LOGIN_ID, "");
    }

    /**
     * 로컬에 저장된 로그인 타입을 가져오는 함수
     * @return 로그인 타입
     */
    public int getLoginType() {
        return preferences.getInt(ExtraType.EXTRA_LOGIN_TYPE, -1);
    }

    /**
     * 식당용
     * 식당의 위치를 로컬에 저장하는 함수
     * @param geo 식당의 위치
     */
    public void saveGeo(GeoDTO geo) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ExtraType.EXTRA_GEO, gson.toJson(geo));
        editor.apply();
    }

    /**
     * 식당용
     * 로컬에 저장된 식당의 위치를 가져오는 함수
     * @return 식당의 위치
     */
    public GeoDTO getGeo() {
        String str = preferences.getString(ExtraType.EXTRA_GEO, "");
        return gson.fromJson(str, GeoDTO.class);
    }

    /**
     * 최초 가이드 판단용
     */
    public void setShownGuide() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ExtraType.EXTRA_SHOWN_GUIDE, true);
        editor.apply();
    }

    public boolean getShownGuide() {
        return preferences.getBoolean(ExtraType.EXTRA_SHOWN_GUIDE, false);
    }
}
