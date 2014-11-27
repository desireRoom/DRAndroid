package com.room.desire.timelapse;

import android.util.Log;

/**
 * used for define public final static value && some static method.
 * @author desire
 *
 */
public class SU {
	public static final String TAG = "TimeLapse";
	private static final boolean DEBUG = true;


	/**
	 * Use to print log for debug.
	 * @param log The log will print with tag @@@@
	 */
	public static void pt(String log) {
		if (DEBUG) {
			Log.d("@@@@", log);
		}
	}
}
