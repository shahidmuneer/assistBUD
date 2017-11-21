package org.medcada.android.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.medcada.android.R;
import org.medcada.android.adapter.ContactsAdapter;
import org.medcada.android.object.Contacts;
import org.medcada.android.tools.ContactManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectContactActivity extends AppCompatActivity {

    @BindView(R.id.lv_contacts)
    ListView lvContacts;
    ContactsAdapter adapter;
    ArrayList<Contacts> contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        ButterKnife.bind(this);

        if (isPermissionAllowed()){
            getContacts();
        }else {
            requestStoragePermission();
        }

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent returnIntent = new Intent();

                returnIntent.putExtra("number", contacts.get(position).getNumber());
                returnIntent.putExtra("name",contacts.get(position).getName());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
//        Intent returnIntent = new Intent();
//
//        returnIntent.putExtra("place", mPlace.getName().toString());
//        returnIntent.putExtra("latlng", mLatLng.latitude + "," + mLatLng.longitude);
//        setResult(Activity.RESULT_OK, returnIntent);
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, STORAGE_PERMISSION_CODE);
    }

    final int STORAGE_PERMISSION_CODE = 100;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts();
            } else {

                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean isPermissionAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    ProgressDialog  pd;
    public void getContacts(){
        new AsyncTask<String,Void,ArrayList<Contacts>>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(SelectContactActivity.this);
                pd.setMessage("Fetching Contacts, Please wait...");
                pd.show();
            }

            @Override
            protected ArrayList<Contacts> doInBackground(String... params) {
                contacts = new ContactManager(SelectContactActivity.this).getContacts();

                return contacts;
            }

            @Override
            protected void onPostExecute(ArrayList<Contacts> contacts) {
                super.onPostExecute(contacts);
                pd.dismiss();
                adapter = new ContactsAdapter(SelectContactActivity.this, contacts);
                lvContacts.setAdapter(adapter);
            }
        }.execute("");
    }
}
