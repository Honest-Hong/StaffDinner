package com.project.boostcamp.publiclibrary.util;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.boostcamp.publiclibrary.R;

/**
 * Created by Hong Tae Joon on 2017-07-26.
 * 마커를 간단하게 만들어주는 도구
 */

public class MarkerBuilder {
    /**
     * 간단한 아이콘으로 이루어진 마커를 만든다
     * @param latLng 경도 위도
     * @return 마커 옵션 객체
     */
    @NonNull
    public static MarkerOptions simple(LatLng latLng) {
        return new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_red));
    }
}
