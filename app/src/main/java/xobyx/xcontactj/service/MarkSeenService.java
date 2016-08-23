package xobyx.xcontactj.service;

import android.app.IntentService;
import android.content.Intent;
import xobyx.xcontactj.until.NotificationManager;
import xobyx.xcontactj.until.SmsHelper;

public class MarkSeenService extends IntentService {
    private final String TAG = "MarkSeenService";

    public MarkSeenService() {
        super("MarkSeenService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SmsHelper.markSmsSeen(this);
        SmsHelper.markMmsSeen(this);
        NotificationManager.update(this);
    }
}
