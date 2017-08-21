package com.project.boostcamp.publiclibrary.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hong Tae Joon on 2017-07-28.
 * 시간을 손쉽게 조작하는 도구
 */

public class TimeHelper {
    /**
     * 시와 분으로 밀리초를 가져오는 함수(날짜는 현재날짜)
     * @param hour 시
     * @param minute 분
     * @return 밀리초
     */
    public static long getTime(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        return cal.getTimeInMillis();
    }

    /**
     * 월, 일, 시, 분으로 밀리초를 가져오는 함수(올해)
     * @param month 월
     * @param day 일
     * @param hour 시
     * @param minute 분
     * @return 밀리초
     */
    public static long getTime(int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        return cal.getTimeInMillis();
    }

    /**
     * 현재 밀리초를 가져오는 함수
     * @return 밀리초
     */
    public static long now() {
        return System.currentTimeMillis();
    }

    /**
     * 밀리초로부터 시를 가져오는 함수
     * @param time 밀리초
     * @return 시
     */
    public static int getHour(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 밀리초로부터 분을 가져오는 함수
     * @param time 밀리초
     * @return 분
     */
    public static int getMinute(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.MINUTE);
    }

    /**
     * DateFormat으로 패턴을 사용하여 밀리초를 문자열로 변환하는 함수
     * @param time 밀리초
     * @param pattern 패턴
     * @return 문자열
     */
    public static String getTimeString(long time, String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(time));
    }

    /**
     * 입력된 시간을 ~분 전 / ~시간 전 문자열로 변환시켜주는 함수
     * @param time 시간
     * @return 변환된 문자열
     */
    public static String getTimeDiffString(long time) {
        long diff = now() - time;
        final int oneMinute = 60 * 1000;
        final int oneHour = 60 * oneMinute;
        final int oneDay = 24 * oneHour;
        final int oneWeek = 7 * oneDay;
        if(diff < oneHour) {
            return (diff / oneMinute) + "분 전";
        } else if(diff < oneDay) {
            return (diff / oneHour) + "시간 전";
        } else if(diff < oneWeek) {
            return (diff / oneDay) + "일 전";
        } else {
            return (diff / oneWeek) + "주 전";
        }
    }
}
