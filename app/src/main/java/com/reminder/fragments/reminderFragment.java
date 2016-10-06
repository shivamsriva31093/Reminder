package com.reminder.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.reminder.R;
import com.reminder.ReminderTimeObject;
import com.reminder.alarm.ReminderManager;
import com.reminder.data.RemindersDbAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by sHIVAM on 6/13/2016.
 */
public class reminderFragment extends Fragment {
    private static final String TAG = reminderFragment.class.getSimpleName();
    private SimpleDateFormat dateFormat;
    private Activity context;
    private DatePickerDialog date;
    private Calendar myCalendar;
    private ReminderTimeObject reminderObject;
    private TimePickerDialog time;
    private RemindersDbAdapter dbHelper;
    private GetSaveStatus mStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = RemindersDbAdapter.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        context = getActivity();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        View rootView = inflater.inflate(R.layout.new_reminder_fragment, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        setDateField();
        setTimeField();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.show();
            }
        });
        return rootView;
    }

    public void setDateField() {
        Calendar mCalendar = Calendar.getInstance();
        date = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,monthOfYear,dayOfMonth);
                myCalendar = newDate;
                reminderObject = new ReminderTimeObject(dateFormat.format(newDate.getTime()));
                time.show();
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public void setTimeField() {
        Calendar mCalendar = Calendar.getInstance();
        time =  new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar newTime = Calendar.getInstance();
                        newTime.set(Calendar.HOUR, hourOfDay);
                        newTime.set(Calendar.MINUTE, minute);
                        newTime.set(Calendar.SECOND, 0);
                        showDialog(hourOfDay, minute);

                    }

                }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE),false);
    }

    public void showDialog(int h, int m) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.make_reminder, null);
        final EditText description = (EditText)dialogView.findViewById(R.id.description);
        myCalendar.set(Calendar.HOUR_OF_DAY, h);
        myCalendar.set(Calendar.MINUTE, m);
        myCalendar.set(Calendar.SECOND,0);
        reminderObject.setRemiderTime(myCalendar);
        Log.d(TAG,"reminder time is:"+myCalendar.getTime()+"");
        AlertDialog alertDialog = alertBuilder.setView(dialogView)
                .setPositiveButton(R.string.ADD, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reminderObject.setReminder(description.getText() + "");
                        boolean flag = saveState();
                        mStatus.indicator(flag);
                        Log.d(TAG, "reminderObject successfully created!");
                    }
                }).create();
        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        dbHelper.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        dbHelper.close();
    }

    private boolean saveState() {
        long id =  dbHelper.createReminder(reminderObject.getDate(),
                reminderObject.getTime(), reminderObject.getReminder());
        Log.d("saveToDB", "inserted :" + (id>0)+"");
        new ReminderManager(getActivity()).setReminder(id,reminderObject.getReminderTime());
        return id>0;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mStatus = (GetSaveStatus)activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement GetSaveStatus");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mStatus = (GetSaveStatus)activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement GetSaveStatus");
        }
    }

    public interface GetSaveStatus {
        void indicator(boolean flag);
    }
}
