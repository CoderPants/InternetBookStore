package com.donteco.internetbookstore.firebase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.notification.MyNotificationBuilder;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

//Token
//dOqRcDV31Ys:APA91bGHvdrzUwjhm1WxHOK1LRbRKEffLjq0qhkAEvVLP3DE3U2AUw64OboAAwVrSTdxQZ3gNWs6mHsZYjBx_40kTo4fltv96aTPH6Sm-hFMXLCddfV4Z1pzOKN3VDhLQYqKf6v3lN28
public class FireBaseInstance extends FirebaseMessagingService {
    public FireBaseInstance() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(ConstantsForApp.LOG_TAG, "From: " + remoteMessage.getFrom());
        Log.d(ConstantsForApp.LOG_TAG, "Data from message " + remoteMessage.getData().size());

        //There is needs to be some kind of check for long and short work
        Log.d(ConstantsForApp.LOG_TAG, "Message data payload: " + remoteMessage.getData());

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        MyNotificationBuilder.createNotification(getApplicationContext(), title, body);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        System.out.println("Get into it");
        Log.d(ConstantsForApp.LOG_TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }

}
