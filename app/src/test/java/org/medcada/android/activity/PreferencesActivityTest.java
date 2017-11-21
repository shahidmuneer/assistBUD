package org.medcada.android.activity;

import org.medcada.android.RobolectricTest;
import org.medcada.android.db.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.Robolectric;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PreferencesActivityTest extends RobolectricTest {
    private PreferencesActivity activity;

    @Mock
    private User mockedUser;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(getDBHandler().getUser(1)).thenReturn(mockedUser);
        when(mockedUser.getPreferred_range()).thenReturn("Test");
        when(mockedUser.getD_type()).thenReturn(1);

        activity = Robolectric.buildActivity(PreferencesActivity.class).create().get();
    }

    @Test
    public void ShouldReportAnalytics_WhenCreated() throws Exception {

        verify(getAnalytics()).reportScreen("Preferences");
    }
}