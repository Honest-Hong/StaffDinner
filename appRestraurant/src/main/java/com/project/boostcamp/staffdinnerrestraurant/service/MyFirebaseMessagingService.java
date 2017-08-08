package com.project.boostcamp.staffdinnerrestraurant.service;

import android.app.NotificationManager;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.boostcamp.publiclibrary.util.NotiHelper;
import com.project.boostcamp.staffdinnerrestraurant.R;
import com.project.boostcamp.staffdinnerrestraurant.activity.MainActivity;

/**
 * Created by Hong Tae Joon on 2017-07-27.
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        manager.notify(0, NotiHelper.simple(this,
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(),
                R.drawable.ic_sms_white_24dp,
                getColor(R.color.colorPrimary),
                intent));
    }
}
