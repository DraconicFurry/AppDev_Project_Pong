package com.example.who.pong;

import android.content.Context;
import android.preference.PreferenceManager;

public class HighscorePreferences {
    private static final String PREF_SCORE = "score";
    private static final String PREF_NAME = "name";

    public static int getScore(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_SCORE, 0);
    }

    public static String getName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_NAME, "Anonymous");
    }
    public static void setScore(Context context, int score) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_SCORE, score)
                .apply();
    }

    public static void setName(Context context, String name) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_NAME, name)
                .apply();
    }
}
