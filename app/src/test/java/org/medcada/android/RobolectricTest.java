package org.medcada.android;

import org.medcada.android.analytics.Analytics;
import org.medcada.android.backup.Backup;
import org.medcada.android.db.DatabaseHandler;
import org.medcada.android.presenter.HelloPresenter;
import org.medcada.android.tools.LocaleHelper;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.RobolectricTestRunner;

@Ignore
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public abstract class RobolectricTest {
    protected Analytics getAnalytics() {
        return getTestApplication().getAnalytics();
    }

    protected Backup getBackup() {
        return getTestApplication().getBackup();
    }

    private TestGlucosioApplication getTestApplication() {
        return (TestGlucosioApplication) RuntimeEnvironment.application;
    }

    protected DatabaseHandler getDBHandler() {
        return getTestApplication().getDBHandler();
    }

    protected HelloPresenter getHelloPresenter() {
        //noinspection ConstantConditions
        return getTestApplication().createHelloPresenter(null);
    }

    protected LocaleHelper getLocaleHelper() {
        //noinspection ConstantConditions
        return getTestApplication().getLocaleHelper();
    }
}
