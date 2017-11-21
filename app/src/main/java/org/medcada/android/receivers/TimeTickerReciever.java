package org.medcada.android.receivers;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import org.medcada.android.R;
import org.medcada.android.db.DatabaseHandler;
import org.medcada.android.db.MedicationBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimeTickerReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
      //  Toast.makeText(context, "Working...", Toast.LENGTH_SHORT).show();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        String currentTime = format.format(new Date());
        ArrayList<MedicationBean> list = new DatabaseHandler(context).getMedicationData();
        for (int i = 0; i < list.size(); i++) {
            MedicationBean bean = list.get(i);
            Log.i("=====", "bean.getReminderTime(): "+ bean.getReminderTime());
            Log.i("=====", "currentTime: "+ currentTime);
            String[] times = bean.getReminderTime().split(",");
            for (String time : times){
                Log.i("=====", "time: "+ time);
                if (currentTime.equals(time.trim())){
                //    Toast.makeText(context, "Matched Found...", Toast.LENGTH_SHORT).show();

                    showNotification(context,"Its time to take "+bean.getMedicationName());
                }
            }

        }


    }


    public void showNotification(Context context,String description){
        Notification.Builder notificationBuilder;

        notificationBuilder = new Notification.Builder(context)
                .setContentTitle("Time for medication")
                .setContentText(description)
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
        notificationManagerCompat.notify(123, notification);
    }
}
