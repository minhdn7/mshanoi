package com.mkit.mshanoi.app.service;

import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.mkit.mshanoi.app.utils.ConfigNotification;
import com.mkit.mshanoi.app.utils.NotificationUtils;
import com.mkit.mshanoi.ui.activity.MainActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by MinhDN on 31/1/2018.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";
    private NotificationUtils notificationUtils;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage == null) return;
        else {
            try {


            } catch (Exception e){
                Log.e("Parse error: ", e.toString());
            }
        }
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Calling method to generate notification
        // Check if message contains a notification payload.
        String title = "";
        String content = "";
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            content = remoteMessage.getNotification().getBody();
            title = remoteMessage.getNotification().getTitle();
        }
        JSONObject json = null;
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                json = new JSONObject(remoteMessage.getData());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
//        handleNotification(title, content, json);
        if (title != null && !title.equals("") && content!= null && !content.equals("") && json != null) {
            handleNotification(title, content, json);
        }
    }

    private void handleNotification(String title, String message, JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        try {
            //String chat_id = json.getString("chat_id");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            Random rand = new Random();
            int notificationID = rand.nextInt(500) + 1;
            intent.putExtra(ConfigNotification.NOTIFICATION_DATA, json.toString());
            intent.putExtra(ConfigNotification.NOTIFICATION_TITLE, title);
            intent.putExtra(ConfigNotification.NOTIFICATION_MESAGE, message);
            showNotificationMessage(getApplicationContext(), title, message, new Date().toString(), intent, notificationID);
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent, int notificationID) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, defaultSoundUri, notificationID);
    }

}
