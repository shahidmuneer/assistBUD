package org.medcada.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.medcada.android.R;
import org.medcada.android.db.Reminder;
import org.medcada.android.presenter.RemindersPresenter;
import org.medcada.android.tools.AnimationTools;

import java.util.Calendar;

public class RemindersActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private FloatingActionButton addFab;
    private RemindersPresenter presenter;
    private ListView listView;
    private String label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        presenter = new RemindersPresenter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(2);
            getSupportActionBar().setTitle(getResources().getString(R.string.activity_reminders_title));
        }

        addFab = (FloatingActionButton) findViewById(R.id.activity_reminders_fab_add);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final CharSequence[] items = {
                        "General", "Birthday", "Appointment"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(RemindersActivity.this);
                builder.setTitle(R.string.activity_reminder_add_label);



                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        label = (items[item].toString());
                        Intent intent = new Intent(RemindersActivity.this,AddReminderDetailsActivity.class);
                        intent.putExtra("label",label);
                        startActivity(intent);
                       // openTimePicker();
                    }
                });


                builder.show();
         }
        });

        listView = (ListView) findViewById(R.id.activity_reminders_listview);
        listView.setEmptyView(findViewById(R.id.activity_reminders_listview_empty));
        listView.setAdapter(presenter.getAdapter());
        addFab.postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationTools.startCircularReveal(addFab);
            }
        }, 600);
    }


    public void updateReminder(Reminder reminder) {
        presenter.updateReminder(reminder);
        presenter.saveReminders();
    }

    public void updateRemindersList() {
        listView.setAdapter(presenter.getAdapter());
        listView.invalidate();
    }



    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        // Id is HOURS+MINUTES to avoid duplicates
        String concatenatedId = hourOfDay + "" + minute;
        // Metric is always glucose until I write support for other metrics so...
        // TODO: Add Reminders for other metrics
        // Also oneTime is always set to false until I implement one time alarms
        // TODO: Implement one time alarms
        presenter.addReminder(Long.parseLong(concatenatedId), cal.getTime(), label, "glucose", false, true);
    }

    public void showDuplicateError() {
        View parentLayout = findViewById(R.id.activity_reminders_root_view);
        Snackbar.make(parentLayout, R.string.activitiy_reminders_error_duplicate, Snackbar.LENGTH_SHORT).show();
    }

    public void showBottomSheetDialog(final long id) {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.fragment_reminders_bottom_sheet, null);
        LinearLayout delete = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_delete);
        LinearLayout edit = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(RemindersActivity.this,AddReminderDetailsActivity.class);
                intent.putExtra("isUpdate",true);
                intent.putExtra("remId",id);
                startActivity(intent);
                mBottomSheetDialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.deleteReminder(id);
                updateRemindersList();
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            presenter.saveReminders();
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRemindersList();
    }
}
