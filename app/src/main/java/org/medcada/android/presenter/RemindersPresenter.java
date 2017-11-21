package org.medcada.android.presenter;

import android.app.Activity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.medcada.android.R;
import org.medcada.android.activity.RemindersActivity;
import org.medcada.android.adapter.RemindersAdapter;
import org.medcada.android.db.DatabaseHandler;
import org.medcada.android.db.Reminder;
import org.medcada.android.tools.GlucosioAlarmManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RemindersPresenter {

    private Activity activity;
    private DatabaseHandler db;

    public RemindersPresenter(Activity activity) {
        this.activity = activity;
        db = new DatabaseHandler(activity);
    }

    public Calendar getCalendar() {
        return Calendar.getInstance();
    }

    public void updateReminder(Reminder reminder) {
        // Create a new object RealM unattached

        db.updateReminder(reminder);
    }

    public ListAdapter getAdapter() {
        List<Reminder> items = db.getReminders();
        List<Reminder> filtered = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            Reminder reminder = items.get(i);
            if (reminder.isReminder()) continue;
                filtered.add(reminder);
        }
        return new RemindersAdapter(activity, R.layout.activity_reminder_item, filtered);
    }

    public Reminder getReminder(long id){
        return db.getReminder(id);
    }

    public void addReminder(long id, Date alarmTime, String label, String metric, boolean oneTime, boolean active) {
        Reminder reminder = new Reminder(id, alarmTime, label, metric, oneTime, active);
        boolean added = db.addReminder(reminder);
        if (added) {
            //  activity.updateRemindersList();
            saveReminders();
        } else {

            // activity.showDuplicateError();
        }
    }

    public void deleteReminder(long id) {
//        Toast.makeText(activity, "id:"+id, Toast.LENGTH_SHORT).show();
//        return;
        db.deleteReminder(id);
        saveReminders();
    }

    public void saveReminders() {
        GlucosioAlarmManager alarmManager = new GlucosioAlarmManager(activity.getApplicationContext());
        alarmManager.setAlarms();
    }
}
