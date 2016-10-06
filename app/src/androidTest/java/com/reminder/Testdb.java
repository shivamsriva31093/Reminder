package com.reminder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.reminder.data.ReminderContract;
import com.reminder.data.ReminderDBHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by sHIVAM on 5/19/2016.
 */
public class Testdb extends AndroidTestCase {
    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(ReminderDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new ReminderDBHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb(){
        String testDate = "19-05-2016";
        String testTime = "10.36pm";
        String testDesc = "X-Men";
        String testID = "2";

        ReminderDBHelper dbHelper = new ReminderDBHelper(this.mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReminderContract._ID, testID);
        values.put(ReminderContract.COLUMN_DATE, testDate);
        values.put(ReminderContract.COLUMN_TIME, testTime);
        values.put(ReminderContract.COLUMN_DESC, testDesc);

        long rowID = db.insert(ReminderContract.DICTIONARY_TABLE_NAME,null,values);
        assertTrue(rowID!=1);
        Log.d("TestingDB","New RowID: "+rowID);


        Cursor cursor = db.query(ReminderContract.DICTIONARY_TABLE_NAME, null, null,null,null,null,null);

        if(cursor.moveToFirst()) {
            validateCursor(values,cursor);
        }
        else {
            fail("No values returned: (");
        }
    }

    static public void validateCursor(ContentValues expectedValues, Cursor valueCursor) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for(Map.Entry<String, Object> entry: valueSet ) {
            String column = entry.getKey();
            int index = valueCursor.getColumnIndex(column);
            assertFalse(-1 == index);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(index));
        }
    }
}
