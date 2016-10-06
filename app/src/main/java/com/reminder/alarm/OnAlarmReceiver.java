package com.reminder.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sHIVAM on 6/14/2016.
 */
public class OnAlarmReceiver extends BroadcastReceiver {
    private static final String TAG = OnAlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        WakeReminderIntentService.acquireStaticLock(context);
        context.startService(new Intent(context, ReminderService.class));
    }
}
