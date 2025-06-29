/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xobyx.xcontactj.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NotificationCompat; // AndroidX
import androidx.core.app.TaskStackBuilder; // AndroidX
import android.util.Log;

// import com.google.android.gms.gcm.GcmListenerService; // Old GCM
import com.google.firebase.messaging.FirebaseMessagingService; // FCM
import com.google.firebase.messaging.RemoteMessage; // FCM

import java.util.Map;

import xobyx.xcontactj.BuildConfig;
import xobyx.xcontactj.R;
import xobyx.xcontactj.activities.MainActivity;
import xobyx.xcontactj.until.DownlodService;
import xobyx.xcontactj.until.UpdateHandler;
import xobyx.xcontactj.until.mDownlodService;

public class MyFirebaseMessagingService extends FirebaseMessagingService { // Class name updated

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            String message = data.get("message");
            String title = data.get("title") != null ? data.get("title") : "Notification"; // Default title
            String channelId = xobyx.xcontactj.MyApp.GENERAL_MESSAGES_CHANNEL_ID; // Default channel

            // Example: Replicate old logic for "/topics/update"
            if (remoteMessage.getFrom() != null && remoteMessage.getFrom().startsWith("/topics/update")) {
                channelId = xobyx.xcontactj.MyApp.APP_UPDATES_CHANNEL_ID; // Use updates channel
                if (data.containsKey("ver") && data.containsKey("url")) {
                    try {
                        final int ver = Integer.parseInt(data.get("ver"));
                        if (ver > BuildConfig.VERSION_CODE) {
                            getSharedPreferences("update", 0).edit()
                                    .putBoolean("new_update", true)
                                    .putInt("ver", ver)
                                    .putString("url", data.get("url"))
                                    .apply();
                            final PendingIntent servicePendingIntent = PendingIntent.getService(
                                    this, // Use service context
                                    0,
                                    new Intent(this, mDownlodService.class), // Use service context
                                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                            sendNotification(title, message, channelId, servicePendingIntent);
                        }
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Error parsing version from FCM message", e);
                        com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }
            } else {
                 // Handle other data messages
                 if (message != null) { // Only send notification if there's a message
                    sendNotification(title, message, channelId, null);
                 }
            }
        }

        // Check if message contains a notification payload from FCM console, etc.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String title = remoteMessage.getNotification().getTitle() != null ? remoteMessage.getNotification().getTitle() : "Notification";
            String body = remoteMessage.getNotification().getBody();
            // Notification payloads from FCM often specify their own channel or use a default one.
            // If channel_id is present in notification payload, use it, otherwise default.
            String channelId = remoteMessage.getNotification().getChannelId();
            if (channelId == null) {
                channelId = xobyx.xcontactj.MyApp.GENERAL_MESSAGES_CHANNEL_ID;
            }
            sendNotification(title, body, channelId, null);
        }


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        // sendRegistrationToServer(token); // TODO: Implement this method
    }


    // [END receive_message]
    private static final int NOTIFICATION_ID = 602;
    private static final int REQUEST_CODE_START_ACTIVITY = 610;
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
      * @param channelId ID of the notification channel to use.
      * @param pendingIntent Optional PendingIntent for the notification action.
     */
    private void sendNotification(String title, String messageBody, String channelId, @Nullable PendingIntent pendingIntent) {
        PendingIntent resultPendingIntent = pendingIntent;
        if (resultPendingIntent == null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultPendingIntent = PendingIntent.getActivity(this, REQUEST_CODE_START_ACTIVITY, intent,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher) // TODO: Replace with a proper small icon (e.g., transparent silhouette)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Set priority
                .setDefaults(NotificationCompat.DEFAULT_ALL) // Will use channel's defaults on O+ if set there
                .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // NotificationChannel creation is done in MyApp.onCreate()
        // No need to create channel here if it's guaranteed to be done by Application start.

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        } else {
            Log.e(TAG, "NotificationManager not found, cannot send notification.");
        }
    }

    // This getBuilder method is now redundant as its logic is incorporated into sendNotification.
    // private NotificationCompat.Builder getBuilder(String Title, String message, PendingIntent resultPendingIntent) {
    //     return new NotificationCompat.Builder(this)
    //             .setSmallIcon(R.mipmap.ic_launcher)
    //             .setContentTitle(Title)
    //             .setContentText(message)
    //             .setAutoCancel(true)
    //             .setDefaults(NotificationCompat.DEFAULT_ALL)
    //             .setContentIntent(resultPendingIntent);
    // }
}


            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(Title, message, null);
        // [END_EXCLUDE]
    }


    // [END receive_message]
    private static final int NOTIFICATION_ID = 602;
    private static final int REQUEST_CODE_START_ACTIVITY = 610;

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String Title, String message, PendingIntent a) {
        PendingIntent resultPendingIntent = null;
        if (a == null) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            ComponentName componentName = intent.getComponent();
            if (componentName != null) {
                // The stack builder object will contain an artificial back
                // stack for the started Activity.
                // This ensures that navigating backward from the Activity leads out of
                // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                // Adds the back stack for the Intent (but not the Intent itself) <== This comment must be wrong!
                stackBuilder.addParentStack(componentName);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(intent);

                resultPendingIntent = stackBuilder.getPendingIntent(REQUEST_CODE_START_ACTIVITY, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            } else {
                resultPendingIntent = PendingIntent.getActivity(this, REQUEST_CODE_START_ACTIVITY, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            }
        } else
            resultPendingIntent = a;
        NotificationCompat.Builder notificationBuilder = getBuilder(Title, message, resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());


    }

    private NotificationCompat.Builder getBuilder(String Title, String message, PendingIntent resultPendingIntent) {
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Title)
                .setContentText(message)
                .setAutoCancel(true)

                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(resultPendingIntent);
    }
}
