package com.reminder;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by sHIVAM on 1/11/2016.
 */
public class ReminderTimeObject implements Parcelable {

    private Calendar remiderTime;
    private String reminder;
    private String date;
    private String time;

    public ReminderTimeObject(Calendar calendar,String reminder) {
        this.remiderTime = calendar;
        this.reminder = reminder;
    }

    public ReminderTimeObject(String reminder, String date) {
        this.reminder = reminder;
        this.date = date;
    }

    public ReminderTimeObject(String date) {
        this.date = date;
    }

    public ReminderTimeObject(Calendar calendar) {
        this.remiderTime = calendar;
    }


    protected ReminderTimeObject(Parcel in) {
        date = in.readString();
        time = in.readString();
        reminder = in.readString();
    }

    public static final Creator<ReminderTimeObject> CREATOR = new Creator<ReminderTimeObject>() {
        @Override
        public ReminderTimeObject createFromParcel(Parcel in) {
            return new ReminderTimeObject(in);
        }

        @Override
        public ReminderTimeObject[] newArray(int size) {
            return new ReminderTimeObject[size];
        }
    };

    public String getReminder() {
        return reminder;
    }

    public Calendar getReminderTime(){
        return remiderTime;
    }

    public String getDate() {
        return date;
    }

    public String getTime() { calcTime(); return time;}

    public void calcTime() {
        int hours = remiderTime.get(Calendar.HOUR_OF_DAY);
        int min = remiderTime.get(Calendar.MINUTE);
        if(hours > 12) {
            hours -= 12;
            time =  hours+":"+min+" PM";
        }
        else if(hours == 12) {
            time =  hours+":"+min+" PM";
        }
        else {
            time =  hours+":"+min+" AM";
        }
    }

    public int getHour() {
        return remiderTime.get(Calendar.HOUR_OF_DAY);
    }



    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public void setRemiderTime(Calendar calendar) {
        this.remiderTime = calendar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(reminder);
    }


}
