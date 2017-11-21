package org.medcada.android.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.medcada.android.R;
import org.medcada.android.db.DatabaseHandler;
import org.medcada.android.object.ProfileDataBean;
import org.medcada.android.tools.LabelledSpinner;
import org.medcada.android.tools.Preferences;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {
    static final int CUSTOM_TYPE_SPINNER_VALUE = 11;
    @BindView(R.id.activity_main_toolbar)
    Toolbar activityMainToolbar;
    @BindView(R.id.done_fab)
    FloatingActionButton doneFab;
    @BindView(R.id.activity_pro_fname)
    EditText activityProFname;
    @BindView(R.id.activity_pro_lname)
    EditText activityProLname;
    @BindView(R.id.activity_pro_ad1)
    EditText activityProAd1;
    @BindView(R.id.activity_pro_ad2)
    EditText activityProAd2;
    @BindView(R.id.activity_pro_city)
    EditText activityProCity;
    @BindView(R.id.activity_pro_state)
    EditText activityProState;
    @BindView(R.id.activity_pro_zip)
    EditText activityProZip;
    @BindView(R.id.activity_pro_llname)
    EditText activityProLlname;
    @BindView(R.id.activity_hello_spinner_country)
    LabelledSpinner activityHelloSpinnerCountry;
    @BindView(R.id.activity_hello_spinner_blood)
    LabelledSpinner activityHelloSpinnerBlood;

    @BindView(R.id.add_condition)
    ImageView addCondition;
    @BindView(R.id.medicalid_txtView)
    TextView medicalidTxtView;
    @BindView(R.id.medicalid_iv)
    ImageView medicalidIv;
    @BindView(R.id.compatSwitch)
    SwitchCompat compatSwitch;
    @BindView(R.id.conditon_container)
    LinearLayout conditonContainer;
    @BindView(R.id.insuranceImage)
    ImageView insuranceImage;
    ProfileDataBean dataBean;
    @BindView(R.id.activity_pro_condition)
    EditText activityProCondition;
    @BindView(R.id.et_passcode)
    EditText etPasscode;
    @BindView(R.id.activity_my_place)
    EditText activityMyPlace;

    private DatabaseHandler dB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        dB = new DatabaseHandler(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(2);
            setTitle("Add Profile Details");
        }

        activityHelloSpinnerBlood.setItemsArray(R.array.profile_add_bloodtype_list);
        activityHelloSpinnerCountry.setItemsArray(R.array.profile_add_country_list);

        new Thread() {
            @Override
            public void run() {
                super.run();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displaySavedDate();
                    }
                });
            }
        }.start();
        compatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPasscode.setVisibility(View.VISIBLE);
                } else {
                    etPasscode.setVisibility(View.GONE);
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();

        }
        return (super.onOptionsItemSelected(menuItem));
    }


    //    public void onViewClicked() {
//
//
//    }

    @OnClick(R.id.done_fab)
    public void saveProfileData() {

        dataBean = new ProfileDataBean();
        dataBean.setBloodType(activityHelloSpinnerBlood.getSpinner().getSelectedItem().toString());
        dataBean.setCity(activityProCity.getText().toString());
        dataBean.setCountry(activityHelloSpinnerCountry.getSpinner().getSelectedItem().toString());
        dataBean.setFirstName(activityProFname.getText().toString());
        dataBean.setLastName(activityProLname.getText().toString());
        dataBean.setStreetAddress1(activityProAd1.getText().toString());
        dataBean.setStreetAddress2(activityProAd2.getText().toString());
        dataBean.setState(activityProState.getText().toString());
        dataBean.setZip(activityProZip.getText().toString());
        dataBean.setLatlng(latlng);
        dataBean.setMyLocation(activityMyPlace.getText().toString());
        int childCount = conditonContainer.getChildCount();
        ArrayList<String> strConditions = new ArrayList<>();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                EditText et = (EditText) conditonContainer.getChildAt(i);
                strConditions.add(et.getText().toString());
            }
        }
        dataBean.setMedContion(strConditions);

        if (imageBytes != null) {
            if (imageBytes.length > 10) {
                dataBean.setImageBytes(imageBytes);
            }
        }
        dataBean.setPinEnabled(compatSwitch.isChecked());
        if (compatSwitch.isChecked()) {
            if (etPasscode.getText().toString().length() > 3) {
                dataBean.setPinEnabled(compatSwitch.isEnabled());
                if (new Preferences(this).getProfiledata().getPinCode() != null) {
                    if (!new Preferences(this).getProfiledata().getPinCode().equals(etPasscode.getText().toString())) {
                        dataBean.setPinCode(etPasscode.getText().toString());
                        sendSMS(dB.getUser(1).getContactNumber(), "Your New Pin is : " + etPasscode.getText().toString());
                    } else {
                        dataBean.setPinCode(etPasscode.getText().toString());
                    }

                } else {
                    dataBean.setPinCode(etPasscode.getText().toString());
                    sendSMS(dB.getUser(1).getContactNumber(), "Your Pin is : " + etPasscode.getText().toString());
                }


            } else {
                Toast.makeText(this, "Please provide 4 digit pin", Toast.LENGTH_SHORT).show();
//                dataBean.setPinEnabled(false);
                return;

            }
        } else {
            dataBean.setPinEnabled(false);
        }
        new Preferences(this).setProfileData(dataBean.toJson());
        Log.i("====", "saveProfileData: " + dataBean.toJson());
        finish();

    }

    @OnClick(R.id.add_condition)
    void addCondition() {
        int childCount = conditonContainer.getChildCount();
        if (childCount < 5) {
            EditText et = new EditText(this);
            et.setHint("Medical Condition#" + (childCount + 1));
            et.setTextSize(14);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            conditonContainer.addView(et, layoutParams);
        }
    }

    private static final int CAMERA_REQUEST = 1888;

    @OnClick(R.id.medicalid_iv)
    public void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    byte[] imageBytes;
    String place,latlng;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            insuranceImage.setVisibility(View.VISIBLE);
            insuranceImage.setImageBitmap(photo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imageBytes = stream.toByteArray();

        }
        if (resultCode == RESULT_OK) {
             place = data.getStringExtra("place");

             latlng = data.getStringExtra("latlng");

                activityMyPlace.setText(place);

                activityMyPlace.setTag("1");

        }
    }

    public void displaySavedDate() {
        dataBean = new Preferences(this).getProfiledata();
        activityProCity.setText(dataBean.getCity());
        activityProFname.setText(dataBean.getFirstName());
        activityProLname.setText(dataBean.getLastName());
        activityProAd1.setText(dataBean.getStreetAddress1());
        activityProAd2.setText(dataBean.getStreetAddress2());
        activityProState.setText(dataBean.getState());
        activityProCity.setText(dataBean.getCity());
        activityProZip.setText(dataBean.getZip());
        activityMyPlace.setText(dataBean.getMyLocation());
        latlng = dataBean.getLatlng();
        if (dataBean.getPinCode() != null) {
            etPasscode.setText(dataBean.getPinCode());
        }
        if (dataBean.getMedContion() != null) {
            int childCount = dataBean.getMedContion().size();
            ArrayList<String> strConditions = dataBean.getMedContion();
            if (childCount != 0) {
                conditonContainer.removeAllViews();
                for (int i = 0; i < childCount; i++) {
                    EditText et = new EditText(this);
                    et.setHint("Medical Condition#" + (childCount + 1));
                    et.setText(strConditions.get(i));
                    et.setTextSize(14);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    conditonContainer.addView(et, layoutParams);
                }
            }
        }

        // dataBean.setMedContion(strConditions);
        // dataBean.setPinEnabled(compatSwitch.isEnabled());
        imageBytes = dataBean.getImageBytes();
        if (imageBytes != null) {
            if (imageBytes.length > 10) {
                Glide.with(this).load(imageBytes).into(insuranceImage);
            }
        }
        compatSwitch.setChecked(dataBean.isPinEnabled());
        if (!dataBean.isPinEnabled()) {
            etPasscode.setVisibility(View.GONE);
        }
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    final int START_LOCATION = 1;
    @OnClick(R.id.activity_my_place)
    public void onViewClicked() {
        Intent locationIntent = new Intent(this, SelectLocationActivity.class);
        startActivityForResult(locationIntent, START_LOCATION);
    }

}
