package com.danmana.countme;

import static com.danmana.countme.DbUtil.formatDate;
import static com.danmana.countme.DbUtil.parseBoolean;
import static com.danmana.countme.DbUtil.parseDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelper implementation for performing CRUD operations on Counter objects.
 * 
 * @author Dan Manastireanu
 * 
 */
public class CounterDatabase extends SQLiteOpenHelper {

	// Database Details
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "CountMe";

	// Table name
	private static final String TABLE_COUNTER = "counter";

	// Column names
	private static final String KEY_ID = "id";
	private static final String KEY_CREATED_DATE = "created_date";
	private static final String KEY_NAME = "name";
	private static final String KEY_VALUE = "value";
	private static final String KEY_DELETED = "deleted";

	// SQL statements
	private static final String SQL_CREATE_TABLE_COUNTER = "CREATE TABLE counter (id INTEGER PRIMARY KEY, created_date DATETIME, name TEXT, value INTEGER, deleted INTEGER)";
	private static final String SQL_GET_BY_ID = "SELECT * FROM counter WHERE id = ?";
	private static final String SQL_GET_ACTIVE = "SELECT * FROM counter WHERE deleted = 0 ORDER BY created_date DESC";
	private static final String SQL_GET_DELETED = "SELECT * FROM counter WHERE deleted = 1 ORDER BY created_date DESC";

	public CounterDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE_COUNTER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// nothing to update, first version
	}

	/** Create and persist a new counter having the given name, and default <code>value=0</code> */
	public Counter createCounter(String name) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CREATED_DATE, formatDate(new Date()));
		values.put(KEY_NAME, name);
		values.put(KEY_VALUE, 0);
		values.put(KEY_DELETED, 0);

		// insert row
		long counterId = db.insert(TABLE_COUNTER, null, values);

		return getCounter(counterId);
	}

	/** Update all fields except <code>id</code> and <code>createdDate</code> for the given counter. */
	public void updateCounter(Counter counter) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, counter.getName());
		values.put(KEY_VALUE, counter.getValue());
		values.put(KEY_DELETED, counter.isDeleted());

		db.update(TABLE_COUNTER, values, "id = ?", new String[] { String.valueOf(counter.getId()) });
	}

	/** Get a counter by its id, or <code>null</code> if not found. */
	public Counter getCounter(long counterId) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(SQL_GET_BY_ID, new String[] { String.valueOf(counterId) });
		if (cursor.moveToFirst()) {
			return mapRow(cursor);
		}

		return null;
	}

	/**
	 * Retrieve all active counters ordered by creation date (last one on top).
	 * 
	 * @return a list of counters, never <code>null</code>
	 */
	public List<Counter> getActiveCounters() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(SQL_GET_ACTIVE, null);

		return list(cursor);
	}

	/**
	 * Retrieve all inactive/deleted counters ordered by creation date (last one on top).
	 * 
	 * @return a list of counters, never <code>null</code>
	 */
	public List<Counter> getDeletedCounters() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(SQL_GET_DELETED, null);

		return list(cursor);
	}

	public void softDelete(long counterId) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DELETED, true);

		db.update(TABLE_COUNTER, values, "id = ?", new String[] { String.valueOf(counterId) });
	}

	public void delete(long counterId) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_COUNTER, "id = ?", new String[] { String.valueOf(counterId) });
	}

	private List<Counter> list(Cursor cursor) {
		List<Counter> counters = new ArrayList<Counter>();
		while (cursor.moveToNext()) {
			counters.add(mapRow(cursor));
		}
		return counters;
	}

	private Counter mapRow(Cursor cursor) {
		Counter counter = new Counter();

		counter.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
		counter.setCreatedDate(parseDate(cursor.getString(cursor.getColumnIndex(KEY_CREATED_DATE))));
		counter.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
		counter.setValue(cursor.getInt(cursor.getColumnIndex(KEY_VALUE)));
		counter.setDeleted(parseBoolean(cursor.getInt(cursor.getColumnIndex(KEY_DELETED))));

		return counter;
	}

}
