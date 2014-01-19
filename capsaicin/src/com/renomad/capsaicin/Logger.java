package com.renomad.capsaicin;

import android.util.Log;

public class Logger {

	private static boolean shouldDebug = true;

	/**
		* Rudimentary logging facility.
		* @param tag the class
		* @param msg explanatory message
		*/
	public static void log(String msg) {

		final String TAG = "Capsaicin";
		if (shouldDebug) {
			Log.d(TAG, msg);
		}
	}

}
