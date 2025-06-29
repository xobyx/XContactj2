package xobyx.xcontactj.until;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider; // Required for N+

import java.io.File;

import xobyx.xcontactj.BuildConfig; // Assuming this is for app version/id
import xobyx.xcontactj.MyApp; // For Notification Channel ID
import xobyx.xcontactj.R; // For notification icon

public class mDownlodService extends Service { // TODO: Rename class to DownloadService (standard convention)
    private static final String TAG = "DownloadService";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";
    public static final String EXTRA_FILE_NAME = "extra_file_name"; // Optional: to suggest a filename

    private DownloadManager downloadManager;
    private long downloadId = -1L;

    // Using a static field for downloadId is problematic if multiple downloads could ever occur.
    // For simplicity in this example, assuming one download at a time managed by this service.
    // A better approach for multiple downloads would be to pass downloadId back to a component
    // or use a database/sharedpref map if the service is restarted.
    // For now, we'll rely on the BroadcastReceiver being registered for the duration of the download.

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId == id) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);
                Cursor cursor = null;
                try {
                    cursor = downloadManager.query(query);
                    if (cursor != null && cursor.moveToFirst()) {
                        int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int reasonIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                        int titleIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE);
                        int localUriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);

                        int status = cursor.getInt(statusIndex);
                        String title = cursor.getString(titleIndex);

                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            Log.d(TAG, "Download successful for " + title);
                            String localUriString = cursor.getString(localUriIndex);
                            if (localUriString != null) {
                                Uri downloadedFileUri = Uri.parse(localUriString);
                                promptInstall(context, downloadedFileUri, title);
                            } else {
                                Log.e(TAG, "Downloaded file URI is null.");
                                showDownloadFailedNotification(context, title, "Failed to get file location.");
                            }
                        } else {
                            int reason = cursor.getInt(reasonIndex);
                            Log.e(TAG, "Download failed for " + title + ". Status: " + status + ", Reason: " + reason);
                            showDownloadFailedNotification(context, title, "Download failed. Reason: " + getDownloadErrorReason(reason));
                        }
                    } else {
                        Log.e(TAG, "DownloadManager query returned no results for ID: " + id);
                         showDownloadFailedNotification(context, "Unknown Download", "Failed to query download status.");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error processing download completion for ID: " + id, e);
                    com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().recordException(e);
                    showDownloadFailedNotification(context, "Download Error", "An error occurred while processing the download.");
                }
                finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                    unregisterReceiverSafe(this);
                    stopSelf(); // Stop the service after handling completion
                }
            }
        }
    };

    private void unregisterReceiverSafe(BroadcastReceiver receiver) {
        try {
            unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            // Receiver was not registered or already unregistered.
            Log.w(TAG, "Receiver not registered or already unregistered: " + e.getMessage());
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        // Note: Receiver is registered in onStartCommand after a download is initiated.
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || !intent.hasExtra(EXTRA_DOWNLOAD_URL)) {
            Log.e(TAG, "Download URL not provided.");
            Toast.makeText(this, "Download URL missing.", Toast.LENGTH_SHORT).show();
            stopSelf(); // Stop if no URL
            return START_NOT_STICKY;
        }

        String downloadUrl = intent.getStringExtra(EXTRA_DOWNLOAD_URL);
        String fileName = intent.getStringExtra(EXTRA_FILE_NAME);
        if (fileName == null || fileName.isEmpty()) {
            fileName = "app_update_" + System.currentTimeMillis() + ".apk"; // Default filename
        }

        if (downloadUrl == null || downloadUrl.isEmpty()) {
            Log.e(TAG, "Download URL is empty after getting from intent.");
            Toast.makeText(this, "Download URL is invalid.", Toast.LENGTH_SHORT).show();
            stopSelf();
            return START_NOT_STICKY;
        }

        // Cancel any previous download this service instance might have started
        // This simple implementation assumes only one download is managed by a service instance.
        // If downloadId is persisted, this logic might need adjustment.
        if (downloadId != -1L) {
            downloadManager.remove(downloadId);
        }

        // Register receiver before starting download
        try {
            registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch (Exception e) {
            // Handle case where receiver might already be registered if service is restarted unexpectedly.
            // This is unlikely with START_NOT_STICKY and stopSelf logic but good for robustness.
            Log.w(TAG, "Could not register download complete receiver, it might already be registered.", e);
        }


        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setTitle("App Update"); // TODO: Make this configurable or use app name
        request.setDescription("Downloading update...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/vnd.android.package-archive");

        // Save to app-specific directory on external storage (recommended)
        // Ensure this directory exists or handle potential errors
        File destinationDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (destinationDir != null) {
            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }
            request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_DOWNLOADS, fileName);
        } else {
            Log.e(TAG, "External files directory not available.");
            Toast.makeText(this, "Storage not available for download.", Toast.LENGTH_LONG).show();
            unregisterReceiverSafe(onDownloadComplete);
            stopSelf();
            return START_NOT_STICKY;
        }

        try {
            downloadId = downloadManager.enqueue(request);
            Log.d(TAG, "Download enqueued with ID: " + downloadId + " for URL: " + downloadUrl);
            Toast.makeText(this, "Download started...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Failed to enqueue download request.", e);
            com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().recordException(e);
            Toast.makeText(this, "Failed to start download.", Toast.LENGTH_LONG).show();
            unregisterReceiverSafe(onDownloadComplete);
            stopSelf();
            return START_NOT_STICKY;
        }

        // The service should not be sticky for a one-shot download task.
        // It will stop itself when the download is complete (or fails) via the receiver.
        return START_NOT_STICKY;
    }

    private void promptInstall(Context context, Uri apkUri, String downloadTitle) {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        Uri actualApkUri = apkUri;

        // TODO: CRITICAL - For Android N (API 24) and above, FileProvider is required to share file URIs.
        // TODO: CRITICAL - For Android O (API 26) and above, REQUEST_INSTALL_PACKAGES permission is needed.
        // This current implementation will likely fail on N+ without FileProvider and on O+ without the permission.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Assuming getExternalFilesDir was used for DownloadManager.
            // The path needs to be correctly configured in filepaths.xml for FileProvider.
            // Example: context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + fileName
            // For DownloadManager, the URI might already be content:// URI if it's from its own provider.
            // If apkUri is a file:// URI, it MUST be converted via FileProvider.
            if ("file".equalsIgnoreCase(apkUri.getScheme())) {
                 File apkFile = new File(apkUri.getPath());
                 actualApkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", apkFile);
                 installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                // If DownloadManager provides a content:// URI from its own provider, that might be okay.
                // However, it's safer to copy to app's own FileProvider controlled location if there are doubts.
                // For now, assume direct usage might work for some DownloadManager URIs or needs testing.
                 installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant permission if it's a content URI
            }
        }

        installIntent.setDataAndType(actualApkUri, "application/vnd.android.package-archive");
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Create a notification to prompt user to install
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = MyApp.APP_UPDATES_CHANNEL_ID; // Use the updates channel

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, installIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // TODO: Use a proper notification icon
                .setContentTitle(downloadTitle != null ? downloadTitle : "Update Ready")
                .setContentText("Download complete. Tap to install.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build()); // Use unique ID for notification
        } else {
             Log.e(TAG, "NotificationManager is null, cannot show install prompt notification.");
        }
    }

    private void showDownloadFailedNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = MyApp.APP_UPDATES_CHANNEL_ID;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // TODO: Use a proper error icon
                .setContentTitle(title != null ? title : "Download Failed")
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        } else {
            Log.e(TAG, "NotificationManager is null, cannot show download failed notification.");
        }
    }

    private String getDownloadErrorReason(int reason) {
        // Basic reasons, can be expanded
        switch (reason) {
            case DownloadManager.ERROR_CANNOT_RESUME: return "Cannot resume.";
            case DownloadManager.ERROR_DEVICE_NOT_FOUND: return "Device not found.";
            case DownloadManager.ERROR_FILE_ALREADY_EXISTS: return "File already exists.";
            case DownloadManager.ERROR_FILE_ERROR: return "File error.";
            case DownloadManager.ERROR_HTTP_DATA_ERROR: return "HTTP data error.";
            case DownloadManager.ERROR_INSUFFICIENT_SPACE: return "Insufficient space.";
            case DownloadManager.ERROR_TOO_MANY_REDIRECTS: return "Too many redirects.";
            case DownloadManager.ERROR_UNHANDLED_HTTP_CODE: return "Unhandled HTTP code.";
            case DownloadManager.ERROR_UNKNOWN: return "Unknown error.";
            default: return "Unknown error code: " + reason;
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiverSafe(onDownloadComplete); // Ensure receiver is unregistered
        super.onDestroy();
        Log.d(TAG, "DownloadService destroyed.");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // This service is not designed to be bound.
        return null;
    }
}
