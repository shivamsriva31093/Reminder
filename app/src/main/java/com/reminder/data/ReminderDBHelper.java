package com.reminder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sHIVAM on 5/19/2016.
 */
public class ReminderDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "remind.db";
    private SQLiteDatabase mDatabase;
    private ReminderDBHelper dbHelper;
    private Context mContext;

    public ReminderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        dbHelper = this;
    }

    public  SQLiteDatabase openDatabase(boolean type) {
        if (type)   mDatabase = dbHelper.getWritableDatabase();
        else    mDatabase = dbHelper.getReadableDatabase();
        return mDatabase;
    }

    public void closeDatabase() {
        if(mDatabase!=null)
            mDatabase.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_REMINDERS_TABLE =
                "create table if not exists "+ReminderContract.DICTIONARY_TABLE_NAME+" ( "
                +ReminderContract._ID+" Integer primary key autoincrement, "
                +ReminderContract.COLUMN_DATE+" text not null, "
                +ReminderContract.COLUMN_TIME+" text not null, "
                +ReminderContract.COLUMN_DESC+" text not null );";
        db.execSQL(SQL_CREATE_REMINDERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists "+ReminderContract.DICTIONARY_TABLE_NAME);
        onCreate(db);
    }
}
