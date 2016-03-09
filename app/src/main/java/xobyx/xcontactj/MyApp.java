package xobyx.xcontactj;

import android.app.Application;

import com.google.android.gms.analytics.Tracker;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import xobyx.xcontactj.until.ReportSenderFac;

/**
 * Created by xobyx on 8/5/2015.
 * For xobyx.xcontactj/XContactj
 */
@ReportsCrashes(mailTo = "xobyxm@hotmail.com", mode = ReportingInteractionMode.DIALOG,
        resToastText = R.string.crash_toast_text, // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds
        resDialogText = R.string.crash_dialog_text,
        reportSenderFactoryClasses = ReportSenderFac.class,

        resDialogIcon = R.drawable.ic_launcher, //optional. default is a warning sign
        resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. When defined, adds a user text field input with this text resource as a label
        // optional. When defined, adds a user email text entry with this text resource as label. The email address will be populated from SharedPreferences and will be provided as an ACRA field if configured.
        resDialogOkToast = R.string.crash_dialog_ok_toast)// optional )


public class MyApp extends Application {
    public static Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();

        ACRA.init(this);
        AnalyticsTrackers.initialize(this);
        tracker = AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
        tracker.enableAutoActivityTracking(true);







    }
}
