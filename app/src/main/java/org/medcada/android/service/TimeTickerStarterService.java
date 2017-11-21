package org.medcada.android.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import org.medcada.android.receivers.TimeTickerReciever;

public class TimeTickerStarterService extends Service {
    public TimeTickerStarterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(new TimeTickerReciever(), new IntentFilter(Intent.ACTION_TIME_TICK));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            unregisterReceiver(new TimeTickerReciever());
        }catch (Exception e){

        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
