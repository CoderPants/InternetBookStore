package com.donteco.internetbookstore.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.activities.MainActivity;
import com.donteco.internetbookstore.constants.ConstantsForApp;

public class MyNotificationBuilder
{

    public static void createNotifivationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(
                    ConstantsForApp.NOTIFICATION_CHANNEL_ID,
                    ConstantsForApp.NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription(ConstantsForApp.NOTIFICATION_DESCRIPTION);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            //Only by this i can get rid of vibration
            channel.enableVibration(true);
            long[] vibration = new long[0];
            channel.setVibrationPattern(vibration);

            NotificationManager manager = NotificationManagingHelper.getNotificationManager();
            manager.createNotificationChannel(channel);
        }
    }

    public static void createNotification(Context context, String title, String body)
    {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,
                ConstantsForApp.NOTIFICATION_CHANNEL_ID )
                .setSmallIcon(R.drawable.ic_new_notification_24dp)
                .setContentTitle(title)
                .setContentText(body)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, ConstantsForApp.PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);

        notificationManager.notify(ConstantsForApp.PENDING_INTENT_ID, notificationBuilder.build());
    }
}
