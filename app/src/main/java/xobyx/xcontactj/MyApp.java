package xobyx.xcontactj;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.drm.DrmManagerClient;
import android.location.Country;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import com.google.android.gms.analytics.Tracker;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.Locale;

import xobyx.xcontactj.until.ReportSenderFac;
/**
 * Created by xobyx on 8/5/2015.
 * For xobyx.xcontactj/XContactj
 */
@ReportsCrashes(mailTo = "xobyxm@hotmail.com", mode = ReportingInteractionMode.DIALOG,
        resToastText = R.string.crash_toast_text, // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds
        resDialogText = R.string.crash_dialog_text,
        reportSenderFactoryClasses = {ReportSenderFac.class},


        resDialogIcon = R.mipmap.ic_launcher, //optional. default is a warning sign
        resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. When defined, adds a user text field input with this text resource as a label
        // optional. When defined, adds a user email text entry with this text resource as label. The email address will be populated from SharedPreferences and will be provided as an ACRA field if configured.
        resDialogOkToast = R.string.crash_dialog_ok_toast)// optional )


public class MyApp extends Application {

    private String mCountryIso;

    public String getCurrentCountryIso() {
        if (mCountryIso == null) {
            Country country = new Country(Locale.getDefault().getCountry(), Country.COUNTRY_SOURCE_LOCALE);
            mCountryIso = country.getCountryIso();
        }
        return mCountryIso;
    }

    static class mTraker
    {

    }
    public static Tracker tracker;
    private static MyApp mApp;
    public static mTraker tracker$;

    private TelephonyManager mTelephonyManager;
    private DrmManagerClient drmManagerClient;

    @Override
    public void onCreate() {
        super.onCreate();

        ACRA.init(MyApp.this);
        AsyncTask m=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {

                AnalyticsTrackers.initialize(MyApp.this);
                tracker = AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
                tracker.enableAutoActivityTracking(true);
                return null;
            }
        };
        m.execute();


        loadDefaultPreferenceValues();


        Country country = new Country(Locale.getDefault().getCountry(), Country.COUNTRY_SOURCE_LOCALE);
        mCountryIso = country.getCountryIso();
        Context context = getApplicationContext();







        //activePendingMessages();



        mApp=this;




    }
    @SuppressLint("CommitPrefEdits")
    private void loadDefaultPreferenceValues() {
        // Load the default values
        PreferenceManager.setDefaultValues(this,R.xml.pref_general, false);

    }
    public static MyApp getApplication() {
        return mApp;
    }


    public TelephonyManager getTelephonyManager() {
        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager) getApplicationContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
        }
        return mTelephonyManager;
    }

    public DrmManagerClient getDrmManagerClient() {
        return drmManagerClient;
    }
}
