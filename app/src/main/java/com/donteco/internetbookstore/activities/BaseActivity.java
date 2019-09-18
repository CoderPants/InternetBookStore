package com.donteco.internetbookstore.activities;

import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.donteco.internetbookstore.backgroundwork.BackgroundWork;
import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.storage.Storage;

import java.util.concurrent.TimeUnit;

public abstract class BaseActivity extends AppCompatActivity
{
    protected void getRidOfTopBar() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        Storage.pushAllToStorage();
        super.onDestroy();
    }
}
