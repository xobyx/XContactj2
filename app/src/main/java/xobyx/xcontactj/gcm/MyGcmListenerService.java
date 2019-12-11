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
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import xobyx.xcontactj.BuildConfig;
import xobyx.xcontactj.R;
import xobyx.xcontactj.activities.MainActivity;
import xobyx.xcontactj.until.DownlodService;
import xobyx.xcontactj.until.UpdateHandler;
import xobyx.xcontactj.until.mDownlodService;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String Title = data.getString("title") != null ? data.getString("title") : "";
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/update")) {
            final int ver = data.getInt("ver");
            if (data.containsKey("ver")) {
                if (ver > BuildConfig.VERSION_CODE) {
                    getSharedPreferences("update", 0).edit().putBoolean("new_update", true).putInt("ver", ver).putString("url", data.getString("url")).apply();
                    final PendingIntent service = PendingIntent.getService(getBaseContext(), 0, new Intent(getApplicationContext(), mDownlodService.class), 0);

                    //UpdateHandler.handle(ver,data.getString("url"));
                    sendNotification(Title, message, service);
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
