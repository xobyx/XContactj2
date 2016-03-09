package xobyx.xcontactj.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.until.SettingHelp;

/**
 * A {@link PreferenceActivity} that presents TouchedItem set of application settings. On
 * handset devices, settings are presented as TouchedItem single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <TouchedItem href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</TouchedItem> for design guidelines and the <TouchedItem
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</TouchedItem> for more information on developing TouchedItem Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {
    private boolean change;
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */

    private SharedPreferences.OnSharedPreferenceChangeListener mChange = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("List_Mode")) {
                SettingsActivity.this.change = true;
                int i = Integer.valueOf(sharedPreferences.getString(key, "0"));

                findPreference(getString(R.string.key_show_number)).setEnabled(i == 0);


            } else if (key.equals(getString(R.string.key_show_number))) {

                SettingsActivity.this.change = true;
                boolean d = sharedPreferences.getBoolean(key, false);

                findPreference("List_Mode").setEnabled(!d);


            } else if (key.equals("PHOTO_MODE")) {
                SettingsActivity.this.change = true;


            } else if (key.equals("background")) {
                SettingsActivity.this.change = true;


            }


        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Determines whether the simplified settings UI should be shown. This is
     * true if this is forced via , or the device
     * doesn't have newer APIs like {@link PreferenceFragment}, or the device
     * doesn't have an extra-large screen. In these cases, TouchedItem single-pane
     * "simplified" settings UI should be shown.
     */
    private static boolean isSimplePreferences(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mChange);


    }

    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that TouchedItem simplified, single-pane UI should be
     * shown.
     */
    @SuppressWarnings("deprecation")
    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }

        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);
        int i = Integer.parseInt(getPreferenceManager().getSharedPreferences().getString(getString(R.string.key_list_mode), "0"));
        if (i == 1) {
            findPreference(getString(R.string.key_show_number)).setEnabled(false);
            SettingHelp.setShowNumb(this.getBaseContext(), false);
        }


        // Add 'data and sync' preferences, and TouchedItem corresponding header.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {

    }

    @Override
    public void finish() {
        if (change)
            setResult(RESULT_OK);
        super.finish();
    }
}