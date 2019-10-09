package com.donteco.internetbookstore.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.activities.MainActivity;
import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.constants.IntentKeys;

public class MyNotificationBuilder
{

    public static void createNotificationChannel()
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

    public static void createNotification(Context context, String title, String body, String fragment)
    {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,
                ConstantsForApp.NOTIFICATION_CHANNEL_ID )
                .setContentTitle(title)
                .setContentText(body)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(IntentKeys.PUSH_NOTIFICATION, fragment.toUpperCase());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, ConstantsForApp.PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);

        switch (fragment.toUpperCase())
        {
            case ConstantsForApp.SEARCH_FRAGMENT:
                notificationBuilder.setSmallIcon(R.drawable.ic_new_notification_24dp);
                break;

            case ConstantsForApp.SHOPPING_CART_FRAGMENT:
                notificationBuilder
                        .setSmallIcon(R.drawable.ic_shopping_cart_notification_24dp)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(body));
                break;
        }

        Log.d(ConstantsForApp.LOG_TAG, "Passed notification creation");
        notificationManager.notify(ConstantsForApp.PENDING_INTENT_ID, notificationBuilder.build());
    }

    /*public static void createCartNotification(Context context, String title, String text, String body)
    {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,
                ConstantsForApp.NOTIFICATION_CHANNEL_ID )
                .setSmallIcon(R.drawable.ic_shopping_cart_notification_24dp)
                .setContentTitle(title)
                .setContentText(text)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setAutoCancel(true);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(IntentKeys.PUSH_NOTIFICATION, ConstantsForApp.SHOPPING_CART_FRAGMENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, ConstantsForApp.PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);

        notificationManager.notify(ConstantsForApp.PENDING_INTENT_ID, notificationBuilder.build());
    }*/
}
