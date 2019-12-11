package xobyx.xcontactj;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.drm.DrmManagerClient;
import android.location.Country;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.analytics.Tracker;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import java.util.Locale;

import xobyx.xcontactj.until.MReportSenderFactory;

/**
 * Created by xobyx on 8/5/2015.
 * For xobyx.xcontactj/XContactj
 */
@ReportsCrashes(
        // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds

        mode = ReportingInteractionMode.TOAST,
        formUri = "https://parseapi.back4app.com",
        reportType = org.acra.sender.HttpSender.Type.JSON,

        httpMethod = HttpSender.Method.POST,
        reportSenderFactoryClasses = MReportSenderFactory.class,

        resToastText = R.string.crash_toast_text

)// optional )


public class MyApp extends Application {

    private String mCountryIso;

    public String getCurrentCountryIso() {
        if (mCountryIso == null) {
            Country country = new Country(Locale.getDefault().getCountry(), Country.COUNTRY_SOURCE_LOCALE);
            mCountryIso = country.getCountryIso();
        }
        return mCountryIso;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
       ACRA.DEV_LOGGING = true;
        ACRA.init(this);


    }



    public static Tracker tracker;
    private static MyApp mApp;

    private TelephonyManager mTelephonyManager;
    private DrmManagerClient drmManagerClient;

    @Override
    public void onCreate() {
        super.onCreate();


        AsyncTask m = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {

                AnalyticsTrackers.initialize(MyApp.this);
                tracker = AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
                tracker.enableAutoActivityTracking(true);
                return null;
            }
        };
        m.execute();


        //ACRA.isACRASenderServiceProcess();
        loadDefaultPreferenceValues();


        Country country = new Country(Locale.getDefault().getCountry(), Country.COUNTRY_SOURCE_LOCALE);
        mCountryIso = country.getCountryIso();
        Log.d("Xcontactj","Country :"+ mCountryIso);
        Context context = getApplicationContext();


        //activePendingMessages();


        mApp = this;


    }

    @SuppressLint("CommitPrefEdits")
    private void loadDefaultPreferenceValues() {
        // Load the default values
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

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
