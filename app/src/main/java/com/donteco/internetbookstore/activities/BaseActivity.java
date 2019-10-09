package com.donteco.internetbookstore.activities;

import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.donteco.internetbookstore.backgroundwork.BackgroundWork;
import com.donteco.internetbookstore.books.BookInCart;
import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.constants.IntentKeys;
import com.donteco.internetbookstore.storage.Storage;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public abstract class BaseActivity extends AppCompatActivity
{
    protected void getRidOfTopBar() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
