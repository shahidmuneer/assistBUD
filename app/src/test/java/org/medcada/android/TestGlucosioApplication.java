package org.medcada.android;

import android.support.annotation.NonNull;

import org.medcada.android.activity.A1cCalculatorActivity;
import org.medcada.android.activity.HelloActivity;

import org.medcada.android.backup.Backup;
import org.medcada.android.db.DatabaseHandler;
import org.medcada.android.presenter.A1CCalculatorPresenter;
import org.medcada.android.presenter.HelloPresenter;
import org.medcada.android.tools.LocaleHelper;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;

public class TestGlucosioApplication extends GlucosioApplication {
    @Mock
    private Backup backupMock;



    @Mock
    private DatabaseHandler dbHandlerMock;

    @Mock
    private A1CCalculatorPresenter a1CCalculatorPresenterMock;

    @Mock
    private HelloPresenter helloPresenterMock;

    @Mock
    private LocaleHelper localeHelperMock;

    @Override
    public void onCreate() {
        super.onCreate();

        initMocks(this);
    }

    @NonNull
    @Override
    public Backup getBackup() {
        return backupMock;
    }

  

    @NonNull
    @Override
    public DatabaseHandler getDBHandler() {
        return dbHandlerMock;
    }

    @NonNull
    @Override
    public A1CCalculatorPresenter createA1cCalculatorPresenter(@NonNull A1cCalculatorActivity activity) {
        return a1CCalculatorPresenterMock;
    }

    @Override
    protected void initLanguage() {
        //nothing
    }

    @NonNull
    @Override
    public HelloPresenter createHelloPresenter(@NonNull final HelloActivity activity) {
        return helloPresenterMock;
    }

    @NonNull
    @Override
    public LocaleHelper getLocaleHelper() {
        return localeHelperMock;
    }
}
