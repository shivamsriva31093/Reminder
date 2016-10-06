package com.reminder.data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sHIVAM on 5/19/2016.
 */
public class RemindersDbAdapter {
    private static ReminderDBHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;
    private static  RemindersDbAdapter instance;

    public static  synchronized RemindersDbAdapter getInstance(Context context) {
        if (instance == null) {
            dbHelper = new ReminderDBHelper(context);
            instance = new RemindersDbAdapter();
        }
        return instance;
    }

    public static synchronized RemindersDbAdapter getInstance() {
        if (instance == null) {
            throw new IllegalStateException(RemindersDbAdapter.class.getSimpleName() +
                    " is not initialized, call getInstance(Context) method first.");
        }

        return instance;
    }

    public boolean isOpen() {
        return database!=null;
    }
    public synchronized SQLiteDatabase open() {
        database = dbHelper.openDatabase(true);
        return database;
    }

    public synchronized void close() {
        dbHelper.closeDatabase();
    }

    public long createReminder(String date, String time, String desc) {
        ContentValues values = new ContentValues();
        values.put(ReminderContract.COLUMN_DATE,date);
        values.put(ReminderContract.COLUMN_TIME, time);
        values.put(ReminderContract.COLUMN_DESC, desc);
        return database.insert(ReminderContract.DICTIONARY_TABLE_NAME,null,values);
    }

    public boolean deleteRow(long rowID) {
        String[] selectionArgs = {String.valueOf(rowID)};
        return database.delete(ReminderContract.DICTIONARY_TABLE_NAME,
                ReminderContract._ID+" LIKE ?",selectionArgs) > 0;
    }

    public Cursor fetchAllReminders() {
        database = dbHelper.openDatabase(false);
        return database.query(ReminderContract.DICTIONARY_TABLE_NAME,null,null,null,null,null,null,null);
    }

    public Cursor fetchReminder(long rowID) {
        database = dbHelper.openDatabase(false);
        Cursor cursor = database.query(true,ReminderContract.DICTIONARY_TABLE_NAME,
                null,ReminderContract._ID+"="+rowID,null,null,null,null,null);
        if (cursor!=null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public boolean updateReminder(long rowID, String date, String time, String desc) {
        ContentValues values = new ContentValues();
        values.put(ReminderContract.COLUMN_DATE,date);
        values.put(ReminderContract.COLUMN_TIME, time);
        values.put(ReminderContract.COLUMN_DESC, desc);
        database = dbHelper.openDatabase(false);
        String selection = ReminderContract._ID+" LIKE ?";
        String[] selectionArgs =   { String.valueOf(rowID)};
        return database.update(ReminderContract.DICTIONARY_TABLE_NAME,values,selection,selectionArgs)> 0;
    }
}
