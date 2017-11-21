package org.medcada.android.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.medcada.android.R;
import org.medcada.android.db.DatabaseHandler;
import org.medcada.android.object.Contacts;
import org.medcada.android.object.ProfileDataBean;
import org.medcada.android.service.ChatHeadService;
import org.medcada.android.service.GPSTracker;
import org.medcada.android.tools.Preferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CallToCircleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallToCircleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.btn_sendSMS)
    Button btnSendSMS;
    @BindView(R.id.btn_makeCall)
    Button btnMakeCall;
    Unbinder unbinder;

    @BindView(R.id.btn_makeCallToPolice)
    Button btn_makeCallToPolice;
    @BindView(R.id.btn_takeMeHome)
    Button btnTakeMeHome;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Preferences preferences;

    public CallToCircleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CallToCircleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CallToCircleFragment newInstance(String param1, String param2) {
        CallToCircleFragment fragment = new CallToCircleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    PendingIntent sentPendingIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_to_circle, container, false);
        unbinder = ButterKnife.bind(this, view);
        preferences = new Preferences(getActivity());

        String SMS_SENT = "SMS_SENT";
        sentPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SMS_SENT), 0);
        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK: {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + preferences.getPoliceContact()));

                        startActivity(intent);
                    }
                }
            }
        }, new IntentFilter(SMS_SENT));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static int REQUEST_CODE = 123;

    @OnClick({R.id.btn_sendSMS, R.id.btn_makeCall, R.id.btn_makeCallToPolice, R.id.btn_mylocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sendSMS:
                int permission2 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS);

                if (permission2 != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.SEND_SMS},
                            REQUEST_CODE
                    );
                    return;
                }

                if (preferences.getEmergencyContacts().size() > 0) {
                    Toast.makeText(getActivity(), "Sending sms to " + preferences.getEmergencyContacts().size() + " contacts", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "No Contacts Added to circle", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_makeCall:
                if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getActivity()))) {
                    getActivity().startService(new Intent(getActivity(), ChatHeadService.class));
                }
                numberIndex = 0;
                b = new ArrayList<String>();

                for (Contacts contacts : preferences.getEmergencyContacts()) {
                    b.add(contacts.getNumber());
                }
                int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CODE
                    );
                    return;
                }

                if (b.size() > 0) {

                    call(numberIndex++);
                    // add PhoneStateListener
                    PhoneCallListener phoneListener = new PhoneCallListener();
                    TelephonyManager telephonyManager = (TelephonyManager) getActivity()
                            .getSystemService(Context.TELEPHONY_SERVICE);
                    telephonyManager.listen(phoneListener,
                            PhoneStateListener.LISTEN_CALL_STATE);
                }
                break;
            case R.id.btn_makeCallToPolice: {
                // String contact = etContact.getText().toString();

                if (!preferences.getPoliceContact().isEmpty()) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling


                        ActivityCompat.requestPermissions(
                                getActivity(),
                                new String[]{Manifest.permission.CALL_PHONE},
                                123
                        );
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getActivity()))) {
                        getActivity().startService(new Intent(getActivity(), ChatHeadService.class));
                    }
                    GPSTracker tracker = new GPSTracker(getActivity());

                    getAddress(String.valueOf(tracker.getLatitude()), String.valueOf(tracker.getLongitude()));

                } else {
                    Toast.makeText(getActivity(), "Police Contact Not Set", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btn_mylocation: {
                int locationLermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);

                if (locationLermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_CODE
                    );
                    return;
                }
                GPSTracker tracker = new GPSTracker(getActivity());
                if (!tracker.canGetLocation()) {
                    tracker.showSettingsAlert();
                }

                getAddressForSMS(String.valueOf(tracker.getLatitude()), String.valueOf(tracker.getLongitude()));

                break;
            }
        }
    }

    Handler handler;

    public void sendSMS(final ArrayList<Contacts> contacts, final String msg) {

        handler = new Handler();
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < contacts.size(); i++) {
                            SmsManager manager = SmsManager.getDefault();
                            manager.sendTextMessage(contacts.get(i).getNumber().toString(), null, msg, null, null);
                            Log.i("=====", "run: SMS SENT " + i + " - " + contacts.get(i).getName());
                            try {
                                sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (i == contacts.size() - 1) {
                                Toast.makeText(getActivity(), "SMS Sent successfully!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
            }
        };
        thread.start();
    }


    ArrayList<String> b;

    int numberIndex = 0;

    void call(int index) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + b.get(index)));
        startActivity(callIntent);

    }

    String LOG_TAG = "EMERGENCY CALL";

    @OnClick(R.id.btn_takeMeHome)
    public void onViewClicked() {
        ProfileDataBean profileData = new Preferences(getActivity()).getProfiledata();
boolean shouldShowLocation = profileData!=null?
        profileData.getMyLocation().isEmpty() ?false:true
        :false;

        if (shouldShowLocation){
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q="+new Preferences(getActivity()).getProfiledata().getLatlng()+
                            " (" + new Preferences(getActivity()).getProfiledata().getMyLocation() + ")"));
            startActivity(intent);
        }else{
            Toast.makeText(getActivity(), "Home location is not set please add Home location first", Toast.LENGTH_SHORT).show();
        }

    }

    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;


        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

//            TelephonyManager.CALL_STATE_IDLE
            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended, need detect flag
                // from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "CALL...");

                    // restart app
                    Intent i = getActivity().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getActivity().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    if (numberIndex < b.size()) {
                        call(numberIndex++);
                    }
                    isPhoneCalling = false;
                }

            }
        }
    }

    ProgressDialog dialog;

    public void getAddress(final String lat, final String lng) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng;
        Log.i("=======", "getAddress: url" + url);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching Current location Please wait...!");
        dialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(1000 * 60 * 10);
        client.post(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("=======", "onSuccess: " + response.toString());
                try {
                    dialog.dismiss();
                    JSONArray arr = response.getJSONArray("results");
                    if (arr.length() > 0) {
                        JSONObject object = arr.getJSONObject(0);
                        String address = object.getString("formatted_address");
                        SmsManager.getDefault().sendTextMessage(new DatabaseHandler(getActivity()).getUser(1).getContactNumber()
                                , "", "My Current Location is " + address, sentPendingIntent, null
                        );


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void getAddressForSMS(final String lat, final String lng) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng;
        Log.i("=======", "getAddress: url" + url);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching Current location Please wait...!");
        dialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(1000 * 60 * 10);
        client.post(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("=======", "onSuccess: " + response.toString());
                try {
                    dialog.dismiss();
                    JSONArray arr = response.getJSONArray("results");
                    if (arr.length() > 0) {
                        JSONObject object = arr.getJSONObject(0);
                        String address = object.getString("formatted_address");
                        SmsManager.getDefault().sendTextMessage(new DatabaseHandler(getActivity()).getUser(1).getContactNumber()
                                , "", "My Current Location is " + address, null, null
                        );


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void getAddressForSMSCircle(final String lat, final String lng) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng;
        Log.i("=======", "getAddress: url" + url);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching Current location Please wait...!");
        dialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(1000 * 60 * 10);
        client.post(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("=======", "onSuccess: " + response.toString());
                try {
                    dialog.dismiss();
                    JSONArray arr = response.getJSONArray("results");
                    if (arr.length() > 0) {
                        JSONObject object = arr.getJSONObject(0);
                        String address = object.getString("formatted_address");
                        String locationUrl = "";
                        if (preferences.getShouldIncludeLocation()) {
                            GPSTracker tracker = new GPSTracker(getActivity());
                            locationUrl += "\nAddress: " + address;
                            locationUrl += " \nhttp://maps.google.com/maps?daddr=" + tracker.getLatitude() + "," + tracker.getLongitude();

                        }
                        sendSMS(preferences.getEmergencyContacts(), preferences.getEmergencyMessage() + "\n" + locationUrl);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
