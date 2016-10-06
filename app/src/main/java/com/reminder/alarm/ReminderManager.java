package com.reminder.alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.util.Log;

import com.reminder.data.RemindersDbAdapter;

import java.util.Calendar;

/**
 * Created by sHIVAM on 6/14/2016.
 */
public class ReminderManager {
    private static final String TAG = ReminderManager.class.getSimpleName();
    private static Activity context;
    private static AlarmManager alarmManager;

    public ReminderManager(Activity context) {
        this.context = context;
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setReminder(long rowId, Calendar when) {
        Intent intent = new Intent(context, OnAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context,(int)rowId,intent,
                PendingIntent.FLAG_ONE_SHOT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                when.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pi);
//        ComponentName receiver = new ComponentName(context, BootReceiver.class);
//        PackageManager pm = context.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);

    }

    public static void cancelAlarm(long rowId) {
        try {
            RemindersDbAdapter dbHelper = RemindersDbAdapter.getInstance(context);
            dbHelper.open();
            Cursor reminder = dbHelper.fetchReminder(rowId);
            String time = "";
            if(reminder.moveToFirst()) {
                 time = reminder.getString(1)+reminder.getString(2);
            }
            alarmManager.cancel(PendingIntent.getBroadcast(context, (int)rowId,
                    new Intent(context, OnAlarmReceiver.class ),
                    PendingIntent.FLAG_UPDATE_CURRENT));
            Log.d(TAG, time);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
