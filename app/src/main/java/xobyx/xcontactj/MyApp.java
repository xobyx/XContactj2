package xobyx.xcontactj;

import android.annotation.SuppressLint;
import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel; // Added
import android.app.NotificationManager; // Added
import android.content.Context;
import android.drm.DrmManagerClient;
import android.location.Country;
// import android.os.AsyncTask; // Replaced with ExecutorService
import android.os.Build; // Added
import androidx.preference.PreferenceManager; // AndroidX

import java.util.concurrent.ExecutorService; // Added
import java.util.concurrent.Executors; // Added
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

    public static final String GENERAL_MESSAGES_CHANNEL_ID = "general_messages_channel";
    public static final String APP_UPDATES_CHANNEL_ID = "app_updates_channel";

    // Executor for application scope background tasks
    private final ExecutorService applicationScopeExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this; // Set static instance early

        createNotificationChannels();

        applicationScopeExecutor.execute(() -> {
            AnalyticsTrackers.initialize(MyApp.this);
            tracker = AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
            if (tracker != null) { // Good practice to check
                tracker.enableAutoActivityTracking(true);
            }
        });

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

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // General Messages Channel
            NotificationChannel generalChannel = new NotificationChannel(
                    GENERAL_MESSAGES_CHANNEL_ID,
                    "General Messages", // User visible name
                    NotificationManager.IMPORTANCE_DEFAULT);
            generalChannel.setDescription("Channel for general app notifications"); // User visible description

            // App Updates Channel
            NotificationChannel updatesChannel = new NotificationChannel(
                    APP_UPDATES_CHANNEL_ID,
                    "App Updates", // User visible name
                    NotificationManager.IMPORTANCE_HIGH); // Updates might be more important
            updatesChannel.setDescription("Channel for app update notifications");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(generalChannel);
                manager.createNotificationChannel(updatesChannel);
                Log.d("MyApp", "Notification channels created.");
            } else {
                Log.e("MyApp", "NotificationManager not found, channels not created.");
            }
        }
    }
}
