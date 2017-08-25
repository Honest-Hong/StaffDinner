package com.project.boostcamp.publiclibrary.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by Hong Tae Joon on 2017-07-26.
 * Geocoder를 손쉽게 사용할 수 있는 도구
 */

public class GeocoderHelper {
    private static Geocoder geocoder;

    /**
     * 위도 경도 객체로 부터 주소를 가져오는 함수
     * @param context 컨텍스트
     * @param latLng 경도 위도
     * @return 해당하는 주소
     */
    public static String getAddress(Context context, LatLng latLng) {
        if (geocoder == null) {
            geocoder = new Geocoder(context);
        }
        String str = "";
        try {
            List<Address> list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(list.size() > 0) {
                Address address = list.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex() + 1 ; i++) {
                    if (i != 0) {
                        str += " ";
                    }
                    str += address.getAddressLine(i);
                }
            } else {
                str = "주소 없음";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.replace("대한민국 ", "");
    }
}
