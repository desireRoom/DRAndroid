package com.room.desire.timelapse;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by desire on 14-10-15.
 */
public class TLSPUtils {
    public static final String STARING_TIME = "start_time";
    public static final String Diff_SEC = "diff_sec";
    private static final String SP_FILE_NAME = "time_lapse";

    private static SharedPreferences getSP(Context c) {
        return c.getSharedPreferences(SP_FILE_NAME, Activity.MODE_PRIVATE);
    }

    public static void removeKey(Context c, String key) {
        if (c == null) return;

        SharedPreferences.Editor editor = getSP(c).edit();
        editor.remove(key);
        editor.commit();
    }

    public static long getStartTime(Context c, long defaultValue) {
        if (c == null) return defaultValue;

        SharedPreferences sp = getSP(c);
        return sp.getLong(STARING_TIME, defaultValue);
    }

    public static void setStartTime(Context c, long start) {
        if (c == null) return;

        SharedPreferences.Editor editor = getSP(c).edit();
        editor.putLong(STARING_TIME, start);
        editor.commit();
    }

    public static int getDiffSec(Context c, int defaultValue) {
        if (c == null) return defaultValue;

        SharedPreferences sp = getSP(c);
        return sp.getInt(Diff_SEC, defaultValue);
    }

    public static void setDiffSec(Context c, int diffSec) {
        if (c == null) return;

        SharedPreferences.Editor editor = getSP(c).edit();
        editor.putInt(Diff_SEC, diffSec);
        editor.commit();
    }
}
