package com.donteco.internetbookstore.notification;

import android.app.NotificationManager;

public class NotificationManagingHelper {

    private static NotificationManager notificationManager;

    public static void createInstance(NotificationManager manager) {
        if(notificationManager == null)
            notificationManager = manager;
    }

    static NotificationManager getNotificationManager(){
        return notificationManager;
    }
}
