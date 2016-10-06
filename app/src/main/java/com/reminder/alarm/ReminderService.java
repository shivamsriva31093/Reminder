package com.reminder.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.reminder.MainActivity;
import com.reminder.fragments.ReminderListFragment;

/**
 * Created by sHIVAM on 6/14/2016.
 */
public class ReminderService extends WakeReminderIntentService {
        private final static String TAG = "ReminderService";

        public ReminderService() {
            super("ReminderService");
        }

        @Override
        void doReminderWork(Intent intent) {
            Log.d(TAG,"Alarm!!!");
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent i2 = new Intent(this, MainActivity.class);
            PendingIntent pi2 = PendingIntent.getActivity(this,0,i2,PendingIntent.FLAG_ONE_SHOT);
            Intent notificationIntent = new Intent(this, ReminderListFragment.class );
            long[] pattern= {2000,3000};
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
            Notification n  = new Notification.Builder(this)
                    .setContentTitle("Reminder" + "test")
                    .setContentText("Subject")
                    .setSmallIcon(android.R.drawable.ic_media_play)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(pattern)
                    .addAction(android.R.drawable.ic_dialog_alert, "Stop", pi2)
                    .addAction(android.R.drawable.ic_menu_add, "More", pendingIntent)
                    .addAction(android.R.drawable.btn_dropdown, "And more", pendingIntent).build();


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0, n);
        }
}
