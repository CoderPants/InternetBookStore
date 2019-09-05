package com.donteco.internetbookstore;

import android.app.Application;

import com.donteco.internetbookstore.storage.Storage;
//import com.facebook.stetho.Stetho;

public class BookStorageApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Storage.loadStorage(getApplicationContext());
        Storage.pullAllFromStorage();

        /*Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());*/
    }

}
