package com.donteco.internetbookstore;

import android.app.Application;
import android.app.NotificationManager;
import android.util.Log;

import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.notification.MyNotificationBuilder;
import com.donteco.internetbookstore.notification.NotificationManagingHelper;
import com.donteco.internetbookstore.storage.Storage;
import com.google.firebase.iid.FirebaseInstanceId;

public class BookStorageApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Storage.loadStorage(getApplicationContext());
        Storage.pullAllFromStorage();

        NotificationManagingHelper.createInstance(getSystemService(NotificationManager.class));
        MyNotificationBuilder.createNotificationChannel();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task ->
                {
                    if (!task.isSuccessful()) {
                        Log.w(ConstantsForApp.LOG_TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    //cdk5H6MqQFo:APA91bGHFNK1EMZoWLYdI5x8CWq_kS-QJbwgUq1L8O0Elo30caalbyRLBN0z3A32Kmsoh5K8vh6VfG9MzQqPTtdSpY9xo7A8w5mKQNI3iByRcnzOlzzeSwGXLdZ9JjtDpyvUiD-lRCT6
                    // Log and toast
                    String msg = getString(R.string.msg_token_fmt, token);
                    Log.d(ConstantsForApp.LOG_TAG, msg);
                });
    }

}
