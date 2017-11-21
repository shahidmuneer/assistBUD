package org.medcada.android.receivers;

import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import org.medcada.android.db.DatabaseHandler;
import org.medcada.android.tools.GlucosioAlarmManager;
import org.medcada.android.tools.GlucosioNotificationManager;

import java.net.ConnectException;

public class GlucosioBroadcastReceiver extends android.content.BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            setAlarms(context);
        } else {


            if (intent.getBooleanExtra("glucosio_reminder", false)) {
                GlucosioNotificationManager notificationManager = new GlucosioNotificationManager(context);
                String reminderLabel = intent.getStringExtra("reminder_label");
                boolean isReminder = intent.getBooleanExtra("isReminder", false);
                String sms = intent.getStringExtra("reminder_sms");
                if (isReminder) {
                    sendSMS(context, sms);
                } else {
                    notificationManager.sendReminderNotification(reminderLabel);
                }
            } else {
                setAlarms(context);
            }
        }
    }

    private void setAlarms(Context context) {
        GlucosioAlarmManager alarmManager = new GlucosioAlarmManager(context);
        alarmManager.setAlarms();
    }

    public void sendSMS(Context context, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(new DatabaseHandler(context).getUser(1).getContactNumber(), null, msg, null, null);

        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}