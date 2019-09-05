package com.donteco.internetbookstore.activities;

import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.donteco.internetbookstore.storage.Storage;

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
