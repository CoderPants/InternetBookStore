package com.donteco.internetbookstore;

import android.app.Application;
import android.app.NotificationManager;

import com.donteco.internetbookstore.notification.MyNotificationBuilder;
import com.donteco.internetbookstore.notification.NotificationManagingHelper;
import com.donteco.internetbookstore.storage.Storage;

public class BookStorageApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Storage.loadStorage(getApplicationContext());
        Storage.pullAllFromStorage();

        NotificationManagingHelper.createInstance(getSystemService(NotificationManager.class));
        MyNotificationBuilder.createNotifivationChannel();
    }

}
