package org.medcada.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import org.medcada.android.R;
import org.medcada.android.adapter.MedicationAdapter;
import org.medcada.android.db.DatabaseHandler;
import org.medcada.android.db.MedicationBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicationActivity extends AppCompatActivity {
    @BindView(R.id.activity_medication_listview)
    ListView lv_medicationdata;
    private FloatingActionButton addFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(2);
            getSupportActionBar().setTitle("Medication");
            addFab = (FloatingActionButton) findViewById(R.id.activity_reminders_fab_add);

            addFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    startAddMedicationActivity();
                }
            });
        }

    }

    private void startAddMedicationActivity() {
        Intent intent = new Intent(this, AddMedicationActivity.class);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
//                Intent homeIntent = new Intent(this, HomeActivity.class);
//                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    ArrayList<MedicationBean> list;

    @Override
    protected void onResume() {
        super.onResume();
        list = new DatabaseHandler(this).getMedicationData();
        MedicationAdapter adapter = new MedicationAdapter(this, list);
        lv_medicationdata.setAdapter(adapter);
        for (MedicationBean bean : list) {
            Log.i("======", "onResume: " + bean.toString());
        }

    }
}
