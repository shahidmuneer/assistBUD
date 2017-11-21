package org.medcada.android.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.medcada.android.R;


import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;


public class SelectLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btn_setLocation;
    LatLng mLatLng;
    Place mPlace;
    String tagId = "";
    ProgressDialog pd;
    boolean locationTakenFromMap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        Log.i("MyLocationFind", "onCreate: SelectLocationActivity");
        ButterKnife.bind(this);
        btn_setLocation = (Button) findViewById(R.id.btn_setLocation);
        // Obtain the SupportMapFragment and getList notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.setCanceledOnTouchOutside(false);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                mMap.clear();
                locationTakenFromMap = false;
                mLatLng = place.getLatLng();
                mPlace = place;
                mMap.addMarker(new MarkerOptions().position(mLatLng).title(place.getName().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 16));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                //Log.i(TAG, "An error occurred: " + status);
            }
        });
        btn_setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!locationTakenFromMap) {
                    if (mLatLng != null && mPlace != null) {
                        Intent returnIntent = new Intent();

                        returnIntent.putExtra("place", mPlace.getAddress().toString());
                        returnIntent.putExtra("latlng", mLatLng.latitude + "," + mLatLng.longitude);
                        setResult(Activity.RESULT_OK, returnIntent);

                      //  AppSharedData.locationName = mPlace.getName().toString();

                        finish();//finishing activity

                    } else {
//                        AKSnackFunctions.ShowSnackbar(SelectLocationActivity.this, "Please Select Location First", AKSnackFunctions.SnackType.Alert);
                        Toast.makeText(SelectLocationActivity.this, "Please Select Location First", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    getLocationName(mLatLng.latitude+","+mLatLng.longitude);
                }
            }
        });


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        boolean success = googleMap.setMapStyle(new MapStyleOptions(AppSharedData.mapJson));
       mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
           @Override
           public void onMapClick(LatLng latLng) {
               mMap.clear();
               mLatLng = latLng;

               locationTakenFromMap = true;
               mMap.clear();
               mMap.addMarker(new MarkerOptions().position(mLatLng).title("Chosen Location"));
               mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 16));

           }
       });
    }


    public void getLocationName(String latlng) {
        AsyncHttpClient client = new AsyncHttpClient(); client.setTimeout(1000*60*10);
        pd.show();
        String URL = "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&language=en&latlng=" + latlng;

        client.get(URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    pd.dismiss();
                    JSONArray results = response.getJSONArray("results");
                    JSONObject obj = results.getJSONObject(0);
                    String placeName = obj.getString("formatted_address");
                    if (mLatLng != null ) {
                        Intent returnIntent = new Intent();

                        returnIntent.putExtra("place", placeName);
                        returnIntent.putExtra("latlng", mLatLng.latitude + "," + mLatLng.longitude);
                        setResult(Activity.RESULT_OK, returnIntent);



                        finish();//finishing activity

                    } else {
                      //  AKSnackFunctions.ShowSnackbar(SelectLocationActivity.this, "Please Select Location First", AKSnackFunctions.SnackType.Alert);
                        Toast.makeText(SelectLocationActivity.this, "Please Select Location First", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
