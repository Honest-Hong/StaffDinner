package com.project.boostcamp.publiclibrary.util;

import android.util.Log;

/**
 * Created by Hong Tae Joon on 2017-08-10.
 */

public class Logger {
    public static final String TAG = "HTJ";

    public static void i(Object o, String title, String message) {
        Log.i(TAG, getFrom(o) + ">>" + title + ":" + message);
    }

    public static void e(Object o, String title, String message) {
        Log.e(TAG, getFrom(o) + ">>" + title + ":" + message);
    }

    private static String getFrom(Object o) {
        String[] temp = o.getClass().getName().split(".");
        if(temp.length == 0) {
            return "null";
        }
        return temp[temp.length-1];
    }
}
