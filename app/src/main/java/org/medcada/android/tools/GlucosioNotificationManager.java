package org.medcada.android.tools;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;

import org.medcada.android.R;
import org.medcada.android.activity.AddGlucoseActivity;

import java.util.Random;

public class GlucosioNotificationManager {
    //private static final String REMOTE_INPUT_KEY = "glucosio_remote_key";
    private static final int NOTIFICATION_ID = 11;
    private Context context;

    public GlucosioNotificationManager(Context context) {
        this.context = context;
    }

    public void sendReminderNotification(String label) {
        String notificationTitle = label + " " + "\u23f0";
        String[] arrayString = context.getResources().getStringArray(R.array.reminder_title_array);
        String notificationText = arrayString[generateRandomNumber(0, 1)];

           if (label.equals("General")) {
               notificationTitle = label + " " + "\u23f0";
               notificationText = "You have a task to perform";
           } else if (label.equals("Birthday")) {
               notificationTitle = label + " " + "\uD83C\uDF82";
               notificationText = "You have a Birthday to celebrate";
           }else if (label.equals("Appointment")) {
               notificationTitle = label + " " + "\uD83D\uDEC4";
               notificationText = "You have a Appointment";


       }

        //String NOTIFICATION_ACTION = context.getString(R.string.reminders_notification_action);

        Intent intent = new Intent(context, AddGlucoseActivity.class);
        intent.putExtra("glucose_reminder_notification", true);
      //  PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder;

        notificationBuilder = new Notification.Builder(context)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1000, 1000})
              //  .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.app_logo_sm);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationBuilder.setColor(context.getColor(R.color.glucosio_pink));
        }

        Notification notification = notificationBuilder.build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }

    private int generateRandomNumber(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }
}
