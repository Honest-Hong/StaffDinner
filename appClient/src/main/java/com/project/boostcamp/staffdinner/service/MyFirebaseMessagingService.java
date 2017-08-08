package com.project.boostcamp.staffdinner.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.boostcamp.publiclibrary.util.NotiHelper;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.ui.activity.MainActivity;
import com.project.boostcamp.staffdinner.ui.activity.SplashActivity;

/**
 * Created by Hong Tae Joon on 2017-07-27.
 * 파이어베이스의 알림을 수신하는 서비스
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "HTJ_Notification";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, NotiHelper.simple(this,
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(),
                R.drawable.ic_sms_white_24dp,
                getColor(R.color.colorPrimary),
                new Intent(this, MainActivity.class)));
    }
}
