package com.donteco.internetbookstore.backgroundwork;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.donteco.internetbookstore.books.BookInCart;
import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.models.RepositoryViewModel;
import com.donteco.internetbookstore.notification.MyNotificationBuilder;

import java.util.List;

public class BackgroundWork extends Worker
{

    public BackgroundWork(@NonNull Context context, @NonNull WorkerParameters workerParams)
    {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        //Data data = getInputData();
        try{
            Log.d(ConstantsForApp.LOG_TAG, "Got into worker!" );
            MyNotificationBuilder.createCartNotification(getApplicationContext(), "You have some not ordered books! ", "Big cart!");

            return Result.success();
        }
        catch (Exception e){
            Log.e(ConstantsForApp.LOG_TAG, "Exception in work manager in doWork method()", e);
            return Result.failure();
        }
    }
}
