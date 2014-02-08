package com.danmana.countme;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

/**
 * Utility class for working with SQLite databases.
 * 
 * @author Dan Manastireanu
 * 
 */
public class DbUtil {

	/** Parse a date from an SQLite date string. */
	public static Date parseDate(String dateString) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(dateString);
		} catch (ParseException e) {
			Log.w("DB", "Unable to parse Date from string \"" + dateString + "\", returning null");
			return null;
		}
	}

	/** Format a java date to a string that can be stored in SQLite. */
	public static String formatDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(date);
	}

	/** Format a boolean to an integer that can be stored in SQLite. */
	public static int formatBoolean(boolean bool) {
		return bool ? 1 : 0;
	}

	/** Parse a boolean from a SQLite integer. */
	public static boolean parseBoolean(int boolValue) {
		return boolValue != 0;
	}

}
