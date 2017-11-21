package org.medcada.android.service;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.medcada.android.R;
import org.medcada.android.activity.MainActivity;

import cz.msebera.android.httpclient.Header;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewListener;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewManager;

public class ChatHeadService extends Service implements FloatingViewListener {
    public ChatHeadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    FloatingViewManager mFloatingViewManager;
    TextView tv_address;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final DisplayMetrics metrics = new DisplayMetrics();
        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout iconView = (LinearLayout) inflater.inflate(R.layout.floating_view, null);
       tv_address = (TextView) iconView.findViewById(R.id.tv_address);
        GPSTracker tracker = new GPSTracker(getApplicationContext());
        getAddress(String.valueOf(tracker.getLatitude()),String.valueOf(tracker.getLongitude()));



//        iconView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//////                final ClipboardManager clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//////                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//////                intent.putExtra("url", clipBoard.getText().toString());
//////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//////                startActivity(intent);
////                destroy();
//
//            }
//        });

        mFloatingViewManager = new FloatingViewManager(this, this);

        mFloatingViewManager.setFixedTrashIconImage(R.drawable.ic_close_white_24dp);
        mFloatingViewManager.setActionTrashIconImage(R.drawable.ic_close_white_24dp);

        final FloatingViewManager.Options options = new FloatingViewManager.Options();
        options.overMargin = (int) (1 * metrics.density);
        options.moveDirection = FloatingViewManager.MOVE_DIRECTION_RIGHT;

        mFloatingViewManager.addViewToWindow(iconView, options);

      //  selfKillerThread();
        return START_REDELIVER_INTENT;

    }

    private void destroy() {
        if (mFloatingViewManager != null) {
            mFloatingViewManager.removeAllViewToWindow();
            mFloatingViewManager = null;
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroy();
    }

    @Override
    public void onFinishFloatingView() {
        stopSelf();
    }

    @Override
    public void onTouchFinished(boolean isFinishing, int x, int y) {

    }

    private void selfKillerThread(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000*10);
                    stopSelf();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void getAddress(final String lat, final String lng) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng;
        Log.i("=======", "getAddress: url" + url);
        AsyncHttpClient client = new AsyncHttpClient(); client.setTimeout(1000*60*10);
        client.post(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("=======", "onSuccess: "+ response.toString());
                try {
                    JSONArray arr = response.getJSONArray("results");
                    if (arr.length() > 0) {
                        JSONObject object = arr.getJSONObject(0);
                        String  address = object.getString("formatted_address");
                        tv_address.setText(address);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
