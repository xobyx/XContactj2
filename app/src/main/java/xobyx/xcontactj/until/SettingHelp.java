package xobyx.xcontactj.until;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import xobyx.xcontactj.R;

/**
 * Created by xobyx on 4/20/2015.
 * c# to java
 */
public class SettingHelp {

    public static int getListMode(Context m) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(m);
        final String mode = pref.getString("List_Mode", "0");
        return Integer.parseInt(mode);

    }

    public static void setListMode(Context m, int value) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(m);
        pref.edit().putString("List_Mode", String.valueOf(value)).apply();


    }

    public static boolean getShowNumb(Context m) {
        final String string = m.getResources().getString(R.string.key_show_number);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(m);
        final boolean result = pref.getBoolean(string, false);
        return result;

    }

    public static void setShowNumb(Context m, boolean value) {
        final String string = m.getResources().getString(R.string.key_show_number);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(m);
        pref.edit().putBoolean(string, value).apply();


    }

    public static int getPhotoMode(Context m) {

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(m);
        final String result = pref.getString("PHOTO_MODE", "0");
        return Integer.valueOf(result);

    }

    public static void setPhotoMode(Context m, int value) {
        final String string = m.getResources().getString(R.string.key_show_number);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(m);
        pref.edit().putString("PHOTO_MODE", String.valueOf(value)).apply();

    }

    public static int
    getBackground(Context m) {

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(m);

        final String result = pref.getString("background", "#fff2f7f0");
        final int color = Color.parseColor(result);

        return color;

    }

    public static void setBackground(Context m, String value) {

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(m);
        pref.edit().putString("background", String.valueOf(value)).apply();

    }
}
