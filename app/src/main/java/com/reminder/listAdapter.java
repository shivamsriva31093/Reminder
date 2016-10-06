package com.reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sHIVAM on 1/11/2016.
 */
public class listAdapter extends ArrayAdapter<ReminderTimeObject> {



    public listAdapter(Context context, int resource) {
        super(context, resource);
    }
    public listAdapter(Context context, int resource, List<ReminderTimeObject> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_reminder_layout,parent,false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.reminder);
            holder.time = (TextView) convertView.findViewById(R.id.Time);
            holder.date = (TextView) convertView.findViewById(R.id.Date);


            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ReminderTimeObject reminder = getItem(position);
        holder.text.setText(reminder.getReminder()+"");
        holder.time.setText(reminder.getTime());
        holder.date.setText(reminder.getDate()+"");

        return convertView;

    }

    static  class ViewHolder {
        private TextView text;
        private TextView time;
        private TextView date;


    }
}
