package com.example.diadailyproject;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class Notification2 extends BroadcastReceiver {

    public static final String reminderExtra = "reminderExtra";
    public static final String messageExtra = "messageExtra";
    public static final String channelID = "channel1";
    public static final int notificationID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(intent.getStringExtra(reminderExtra))
                .setContentText(intent.getStringExtra(messageExtra));
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, builder.build());
    }
}
