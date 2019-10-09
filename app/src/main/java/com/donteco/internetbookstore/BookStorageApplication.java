package com.donteco.internetbookstore;

import android.app.Application;
import android.app.NotificationManager;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.donteco.internetbookstore.backgroundwork.BackgroundWork;
import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.notification.MyNotificationBuilder;
import com.donteco.internetbookstore.notification.NotificationManagingHelper;
import com.donteco.internetbookstore.storage.Storage;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.TimeUnit;

public class BookStorageApplication extends Application implements LifecycleObserver {

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

                    // Log and toast
                    String msg = getString(R.string.msg_token_fmt, token);
                    Log.i(ConstantsForApp.LOG_TAG, msg);
                });

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void doOnFinish(){
        Log.d(ConstantsForApp.LOG_TAG, "Passed lifecycle");
        Storage.pushAllToStorage();
        createWork();
    }

    private void createWork()
    {
        OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(BackgroundWork.class)
                .setInitialDelay(ConstantsForApp.AMOUNT_OF_DELAY, TimeUnit.SECONDS)
                .build();

        //WorkManager.getInstance(this).enqueue(myWorkRequest);
        WorkManager.getInstance(this).enqueueUniqueWork(ConstantsForApp.WORKER_UNIQUE_ID,
                ExistingWorkPolicy.REPLACE, myWorkRequest);
    }
}
