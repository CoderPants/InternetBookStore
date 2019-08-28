package com.donteco.internetbookstore.help;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ActivityHelper
{
    public ActivityHelper() {
    }

    public static void getRidOfTopBar(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
