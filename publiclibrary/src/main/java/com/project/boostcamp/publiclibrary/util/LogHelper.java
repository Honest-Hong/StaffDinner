package com.project.boostcamp.publiclibrary.util;

import android.util.Log;

/**
 * Created by Hong Tae Joon on 2017-08-10.
 */

public class LogHelper {
    public static final String TAG = "HTJ";
    public static void debug(Object o, String message) {
        Log.d(TAG, o.getClass().getName() + ">>" + message);
    }

    public static void inform(Object o, String message) {
        Log.i(TAG, o.getClass().getName() + ">>" + message);
    }

    public static void error(Object o, String message) {
        Log.e(TAG, o.getClass().getName() + ">>" + message);
    }
}
