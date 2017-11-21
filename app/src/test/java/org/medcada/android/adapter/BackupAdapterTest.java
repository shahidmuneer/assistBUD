package org.medcada.android.adapter;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.drive.DriveId;

import org.medcada.android.R;
import org.medcada.android.RobolectricTest;
import org.medcada.android.object.GlucosioBackup;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.Robolectric;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class BackupAdapterTest extends RobolectricTest {
    private AppCompatActivity activity;
    private ViewGroup viewGroup;
    private BackupAdapter adapter;

    @Mock
    private DriveId driveIDMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        activity = Robolectric.buildActivity(AppCompatActivity.class).create().get();
        viewGroup = (ViewGroup) activity.findViewById(android.R.id.content);

        List<GlucosioBackup> backupsArray = new ArrayList<>();
        backupsArray.add(new GlucosioBackup(driveIDMock, new Date(), 1024));
        adapter = new BackupAdapter(activity, R.layout.preferences_backup, backupsArray);
    }

    @Test
    public void ReturnViewWithSizeFormatted_WhenAsked() throws Exception {
        View view = adapter.getView(0, null, viewGroup);

        TextView sizeView = (TextView) view.findViewById(R.id.item_history_type);
        assertThat(sizeView).hasText("1.00KB");
    }
}
