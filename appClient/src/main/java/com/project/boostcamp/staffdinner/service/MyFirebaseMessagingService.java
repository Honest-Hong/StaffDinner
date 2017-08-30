package com.project.boostcamp.staffdinner.service;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.boostcamp.publiclibrary.data.ExtraType;
import com.project.boostcamp.publiclibrary.util.NotiHelper;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.activity.MainActivity;

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

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ExtraType.EXTRA_NOTIFICATION_TYPE, Integer.parseInt(remoteMessage.getData().get("type")));
        startActivity(intent);
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, NotiHelper.simple(this,
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(),
                R.drawable.ic_sms_white_24dp,
                ContextCompat.getColor(MyFirebaseMessagingService.this, R.color.colorPrimary),
                intent));
    }
}
