package org.medcada.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import org.medcada.android.R;
import org.medcada.android.activity.RemindersActivity;
import org.medcada.android.db.Reminder;
import org.medcada.android.tools.FormatDateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class RemindersAdapter extends ArrayAdapter<Reminder> {
    private Context context;
    private List<Reminder> items;
    private Calendar calendar;
    private FormatDateTime formatDateTime;

    public RemindersAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public RemindersAdapter(Context context, int resource, List<Reminder> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        calendar = Calendar.getInstance();
        formatDateTime = new FormatDateTime(context);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.activity_reminder_item, parent, false);
        }

        LinearLayout rootView = (LinearLayout) v.findViewById(R.id.activity_reminders_root_view);
        TextView timeTextView = (TextView) v.findViewById(R.id.activity_reminders_item_time);
        TextView labelTextView = (TextView) v.findViewById(R.id.activity_reminders_label);
        Switch activeSwitch = (Switch) v.findViewById(R.id.activity_reminders_item_enabled);
        final Reminder reminder = items.get(position);

        final long reminderId = reminder.getId();
        Log.i("=====", position + ": getView: " + reminder.toString());

        calendar.setTime(reminder.getAlarmTime());
        SimpleDateFormat newformater = new SimpleDateFormat("MMM dd - hh:mm");
        timeTextView.setText(newformater.format(reminder.getAlarmTime())
                /*formatDateTime.getTime(calendar)*/);
        labelTextView.setText(reminder.getLabel());
        activeSwitch.setChecked(reminder.isActive());

        activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Reminder updatedReminder = new Reminder(reminder.getId(), reminder.getAlarmTime(), reminder.getLabel(), reminder.getMetric(),
                        reminder.isOneTime(), reminder.isActive());
                updatedReminder.setActive(b);
                updateReminder(updatedReminder);
            }
        });

//        rootView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                showBottomSheetMenu(reminderId);
//                return true;
//            }
//        });
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetMenu(reminderId);
            }
        });




        return v;
    }

    private void showBottomSheetMenu(long id) {
        ((RemindersActivity) context).showBottomSheetDialog(id);
    }

    private void updateReminder(Reminder reminder) {
        // Should be always true.
        // I HOPE...
        if (context instanceof RemindersActivity) {
            ((RemindersActivity) context).updateReminder(reminder);
        }
    }
}
