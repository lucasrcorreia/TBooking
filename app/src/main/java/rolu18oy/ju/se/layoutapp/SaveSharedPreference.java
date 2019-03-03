package rolu18oy.ju.se.layoutapp;

import android.content.SharedPreferences;
import android.content.Context;
import android.preference.PreferenceManager;

import static rolu18oy.ju.se.layoutapp.PreferencesUtility.*;

public class SaveSharedPreference {

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setLoggedIn(Context context, boolean loggedIn, String email) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString("Email",email);
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }
    public static String getLoggedEmail(Context context){
        return getPreferences(context).getString("Email","");
    }
}
