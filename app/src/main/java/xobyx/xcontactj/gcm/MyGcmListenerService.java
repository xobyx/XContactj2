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

// Renaming to MyFirebaseMessagingService for clarity, though file name remains for now
public class MyGcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService"; // Renamed TAG

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
            String title = data.get("title") != null ? data.get("title") : "";

            // Example: Replicate old logic for "/topics/update"
            // This part needs careful review and adaptation to FCM data structure
            if (remoteMessage.getFrom() != null && remoteMessage.getFrom().startsWith("/topics/update")) {
                if (data.containsKey("ver") && data.containsKey("url")) {
                    try {
                        final int ver = Integer.parseInt(data.get("ver"));
                        if (ver > BuildConfig.VERSION_CODE) {
                            getSharedPreferences("update", 0).edit()
                                    .putBoolean("new_update", true)
                                    .putInt("ver", ver)
                                    .putString("url", data.get("url"))
                                    .apply();
                            // Ensure mDownlodService is appropriate for FCM context
                            final PendingIntent servicePendingIntent = PendingIntent.getService(getBaseContext(), 0,
                                    new Intent(getApplicationContext(), mDownlodService.class), PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE); // Added mutability flag
                            sendNotification(title, message, servicePendingIntent);
                        }
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Error parsing version from FCM message", e);
                    }
                }
            } else {
                 // Handle other data messages
                 // For now, just show a generic notification if it has title and message
                 if (title != null && message != null) {
                    sendNotification(title, message, null);
                 }
            }
        }

        // Check if message contains a notification payload.
        // Note: Notification messages received while app is in background are handled by the system tray.
        // Notification messages received while app is in foreground are delivered here.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String title = remoteMessage.getNotification().getTitle() != null ? remoteMessage.getNotification().getTitle() : "Notification";
            String body = remoteMessage.getNotification().getBody();
            sendNotification(title, body, null);
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
     */
    private void sendNotification(String title, String messageBody, PendingIntent pendingIntent) { // Parameter changed for clarity
        PendingIntent resultPendingIntent = pendingIntent;
        if (resultPendingIntent == null) {
            Intent intent = new Intent(this, MainActivity.class); // Changed getBaseContext() to this
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // ComponentName componentName = intent.getComponent(); // Not needed for this simple case
            // if (componentName != null) {
            //     TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            //     stackBuilder.addParentStack(componentName);
            //     stackBuilder.addNextIntent(intent);
            //     resultPendingIntent = stackBuilder.getPendingIntent(REQUEST_CODE_START_ACTIVITY, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE); // Added mutability
            // } else {
            //     resultPendingIntent = PendingIntent.getActivity(this, REQUEST_CODE_START_ACTIVITY, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE); // Added mutability
            // }
            // Simplified PendingIntent creation for foreground notification click
             resultPendingIntent = PendingIntent.getActivity(this, REQUEST_CODE_START_ACTIVITY, intent,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE); // Added mutability flags
        }

        // String channelId = getString(R.string.default_notification_channel_id); // TODO: Create notification channels for Android O+
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this /*, channelId */) // Pass 'this' for context
                .setSmallIcon(R.mipmap.ic_launcher) // Ensure this is a valid small icon (often white/transparent)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // TODO: Since android Oreo notification channel is needed.
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //     NotificationChannel channel = new NotificationChannel(channelId,
        //             "Channel human readable title",
        //             NotificationManager.IMPORTANCE_DEFAULT);
        //     notificationManager.createNotificationChannel(channel);
        // }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }


    private NotificationCompat.Builder getBuilder(String Title, String message, PendingIntent resultPendingIntent) {
        return new NotificationCompat.Builder(this) // Pass 'this' for context
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Title)
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(resultPendingIntent);
    }
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
