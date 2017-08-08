package com.project.boostcamp.publiclibrary.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Hong Tae Joon on 2017-08-08.
 */

public class NotiHelper {
    public static Notification simple(Context context, String title, String body, int icon, int color, Intent intent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(icon)
                .setColor(color)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long [] { 0, 250, 250, 250 }
                )
                .setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        return notificationBuilder.build();
    }
}
