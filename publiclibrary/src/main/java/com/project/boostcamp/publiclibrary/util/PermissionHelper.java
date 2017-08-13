package com.project.boostcamp.publiclibrary.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.project.boostcamp.publiclibrary.data.RequestType;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-13.
 */

public class PermissionHelper {
    private static boolean needPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_DENIED;
    }

    private static void requestPermissions(Activity activity, ArrayList<String> permissions, int requestCode) {
        String[] temp = new String[permissions.size()];
        permissions.toArray(temp);
        ActivityCompat.requestPermissions(activity, temp, requestCode);
    }

    /**
     * 넘겨준 권한들에 권한이 있는지 판단하고 필요한 권한만 요청하는 함수
     * @param activity
     * @param permissions
     * @param requestCode
     * @return 권한이 필요 없으면 true, 권한이 하나라도 필요하면 false
     */
    public static boolean checkAndRequestPermissions(Activity activity, String[] permissions, int requestCode) {
        ArrayList<String> needPermissions = new ArrayList<>();
        for(String str: permissions) {
            if(needPermission(activity, str)) {
                needPermissions.add(str);
            }
        }
        if(needPermissions.size() == 0) {
            return true;
        } else {
            requestPermissions(activity, needPermissions, requestCode);
            return false;
        }
    }
}
