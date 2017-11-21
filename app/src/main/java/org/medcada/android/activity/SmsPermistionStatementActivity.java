package org.medcada.android.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.medcada.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmsPermistionStatementActivity extends AppCompatActivity {
    @BindView(R.id.tv_policy)
    TextView tvPolicy;
    @BindView(R.id.tv_policy_title)
    TextView tvPolicyTitle;
    @BindView(R.id.btn_accept)
    Button btnAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_permistion_statement);
        ButterKnife.bind(this);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        tvPolicy.setTypeface(tf);
        tvPolicyTitle.setTypeface(tf);
        btnAccept.setTypeface(tf);
        String policy_title = "<u><b>SMS Permission Statement</b></u>";

        String policy = "I authorize <b>AssistBUD</b> app to send text\n" +
                "messages to/from my cell phone to deliver\n" +
                "services part of the application.<br><br>I understand\n" +
                "that standard text messaging rates will apply to\n" +
                "any messages received from <b>AssistBUD</b>\n" +
                "depending on my cellular carrier and my\n" +
                "cellular plan.";
        tvPolicy.setText(Html.fromHtml(policy));
        tvPolicyTitle.setText(Html.fromHtml(policy_title));
    }

    @OnClick(R.id.btn_accept)
    public void onViewClicked() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.SEND_SMS},
                    111
            );
            return;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 111: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   startActivity(new Intent(this,HelloActivity.class));

                } else {

                    Toast.makeText(this, "SMS Permission is necessary to continue.", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
