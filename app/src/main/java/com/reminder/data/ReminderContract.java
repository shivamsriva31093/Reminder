package com.reminder.data;

import android.provider.BaseColumns;

/**
 * Created by sHIVAM on 5/19/2016.
 */
public class ReminderContract implements BaseColumns {

    public static final String _ID = "_id";
    public static final String DICTIONARY_TABLE_NAME = "reminders";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DESC = "description";

}
