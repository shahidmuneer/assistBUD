package org.medcada.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.medcada.android.service.TimeTickerStarterService;
import org.medcada.android.tools.Preferences;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(this, TimeTickerStarterService.class));
        Intent intent = null;
        if (!new Preferences(this).getIsPolicyAccepted()){
            intent = new Intent(this, PolicyStatementActivity.class);

        }else {
        intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}