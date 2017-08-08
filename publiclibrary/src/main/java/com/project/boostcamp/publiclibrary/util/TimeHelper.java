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
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis();
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

    public static String getTimeDiffString(long time) {
        long diff = now() - time;
        diff /= 1000 * 60;
        return String.format("%d분 전", diff);
    }
}
