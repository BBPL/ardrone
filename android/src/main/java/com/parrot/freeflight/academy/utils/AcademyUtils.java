package com.parrot.freeflight.academy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.parrot.freeflight.academy.model.Profile;
import org.mortbay.jetty.HttpVersions;

public class AcademyUtils {
    public static final String ACADEMY_PREFS_FILE_NAME = "ACADEMY_SECURE_STORAGE";
    public static boolean isConnectedAcademy = false;
    public static String login = HttpVersions.HTTP_0_9;
    public static String password = HttpVersions.HTTP_0_9;
    public static boolean password_save_ok = true;
    public static Profile profile = new Profile();
    public static int responseCode = 0;

    public static void clearCredentials(Context context) {
        new AcademySharedPreferences(context, context.getSharedPreferences(ACADEMY_PREFS_FILE_NAME, 0)).edit().putBoolean("academyconnection", false).commit();
        profile = new Profile();
    }

    public static void getCredentials(Context context) {
        SharedPreferences academySharedPreferences = new AcademySharedPreferences(context, context.getSharedPreferences(ACADEMY_PREFS_FILE_NAME, 0));
        login = academySharedPreferences.getString("login", null);
        password = academySharedPreferences.getString("password", null);
        isConnectedAcademy = academySharedPreferences.getBoolean("academyconnection", false);
    }

    public static void saveCredentials(Context context) {
        SharedPreferences academySharedPreferences = new AcademySharedPreferences(context, context.getSharedPreferences(ACADEMY_PREFS_FILE_NAME, 0));
        academySharedPreferences.edit().putString("login", password_save_ok ? login : HttpVersions.HTTP_0_9).commit();
        academySharedPreferences.edit().putString("password", password_save_ok ? password : HttpVersions.HTTP_0_9).commit();
        academySharedPreferences.edit().putBoolean("academyconnection", isConnectedAcademy).commit();
    }
}
