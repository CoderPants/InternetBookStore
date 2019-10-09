package com.donteco.internetbookstore.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.notification.MyNotificationBuilder;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FireBaseInstance extends FirebaseMessagingService {
    public FireBaseInstance() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(ConstantsForApp.LOG_TAG, "From: " + remoteMessage.getFrom());
        Log.d(ConstantsForApp.LOG_TAG, "Data from message " + remoteMessage.getData().size());
        Log.d(ConstantsForApp.LOG_TAG, "Message data payload: " + remoteMessage.getData());

        Map<String, String> pushData = remoteMessage.getData();
        String title = pushData.get("body");
        String body = pushData.get("title");
        String fragment = pushData.get("fragment");

        assert fragment != null;
        MyNotificationBuilder.createNotification(getApplicationContext(), title, body, fragment);
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
