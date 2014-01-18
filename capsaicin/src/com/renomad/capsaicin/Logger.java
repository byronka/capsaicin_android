package com.renomad.capsaicin;

import android.util.Log;

public class Logger {

	private static boolean shouldDebug = true;

	/**
		* Rudimentary logging facility.
		* @param tag the class
		* @param msg explanatory message
		*/
	public static void mylog(String tag, String msg) {
		if (shouldDebug) {
			Log.d(tag, msg);
		}
	}

}
