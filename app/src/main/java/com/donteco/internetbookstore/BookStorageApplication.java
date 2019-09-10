package com.donteco.internetbookstore;

import android.app.Application;

import com.donteco.internetbookstore.storage.Storage;

public class BookStorageApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Storage.loadStorage(getApplicationContext());
        Storage.pullAllFromStorage();
    }

}
