package hu.logcontrol.mobilflexandroid.presenters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

import hu.logcontrol.mobilflexandroid.LoginActivity;
import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.adapters.LanguagesSpinnerAdapter;
import hu.logcontrol.mobilflexandroid.datamanager.AppDataManager;
import hu.logcontrol.mobilflexandroid.enums.RepositoryType;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivity;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivityPresenter;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.models.LanguagesSharedPreferences;
import hu.logcontrol.mobilflexandroid.models.LocalEncryptedPreferences;
import hu.logcontrol.mobilflexandroid.models.SettingsObject;
import hu.logcontrol.mobilflexandroid.taskmanager.CustomThreadPoolManager;
import hu.logcontrol.mobilflexandroid.taskmanager.PresenterThreadCallback;

public class MainActivityPresenter implements IMainActivityPresenter, PresenterThreadCallback {

    private IMainActivity iMainActivity;
    private Context context;
    private CustomThreadPoolManager mCustomThreadPoolManager;
    private MainActivityHandler mainActivityHandler;

    private int[] languagesImages;

    private LanguagesSharedPreferences hungaryWCPrefFile;
    private LanguagesSharedPreferences germanWCPrefFile;
    private LanguagesSharedPreferences englishWCPrefFile;

    private LocalEncryptedPreferences preferences;

    private AppDataManager appDataManager;

    public MainActivityPresenter(IMainActivity iMainActivity, Context context) {
        this.iMainActivity = iMainActivity;
        this.context = context.getApplicationContext();
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* ILoginActivityPresenter interfész függvényei */
    @Override
    public void initTaskManager() {
        try {
            ApplicationLogger.logging(LogLevel.INFORMATION, "A feladatkezelő létrehozása megkezdődött.");

            mainActivityHandler = new MainActivityHandler(Looper.myLooper(), this);
            mCustomThreadPoolManager = CustomThreadPoolManager.getsInstance();
            mCustomThreadPoolManager.setPresenterCallback(this);

            ApplicationLogger.logging(LogLevel.INFORMATION, "A feladatkezelő létrehozása befejeződött.");
        }
        catch (Exception e){
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    @Override
    public void openActivityByEnum(ViewEnums viewEnum) {
        if(viewEnum == null) return;
        if(iMainActivity == null) return;
        if(appDataManager == null) return;

        Intent intent = null;

        switch (viewEnum){
            case LOGIN_ACTIVITY:{
                intent = new Intent(context, LoginActivity.class);

                SettingsObject settingsObject = appDataManager.getBaseSettingsObjectFromPrefFile();
                if(settingsObject != null) intent.putExtra("settingsObject", settingsObject);

                break;
            }
        }

        if(intent == null) return;
        if(iMainActivity != null) iMainActivity.openViewByIntent(intent);
    }

    @Override
    public LanguagesSpinnerAdapter getSpinnerAdapter() {
        if(appDataManager == null) return null;
        languagesImages = appDataManager.getLanguagesFlags();

        LanguagesSpinnerAdapter adapter = null;
        if(languagesImages != null) adapter = new LanguagesSpinnerAdapter(context, languagesImages);

        if(adapter == null) return null;
        return adapter;
    }

    @Override
    public void translateTextBySelectedLanguage(String languageID) {
        if(appDataManager == null) return;

        String m;

        switch (languageID){
            case "HU":{
                m = appDataManager.getMessageText(RepositoryType.HUNGARY_PREFERENCES_FILE, "HU$WC_MessageTextView");
                if(m != null) iMainActivity.setTextToMessageTV(m);
                break;
            }
            case "EN":{
                m = appDataManager.getMessageText(RepositoryType.ENGLISH_PREFERENCES_FILE, "EN$WC_MessageTextView");
                if(m != null) iMainActivity.setTextToMessageTV(m);
                break;
            }
            case "DE":{
                m = appDataManager.getMessageText(RepositoryType.GERMAN_PREFERENCES_FILE, "DE$WC_MessageTextView");
                if(m != null) iMainActivity.setTextToMessageTV(m);
                break;
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void saveLanguageToSettingsFile(String languageID) {
        if(appDataManager == null) return;
        appDataManager.saveLanguageIDToPrefFile("CurrentSelectedLanguage", languageID);
    }

    @Override
    public int getCurrentLanguageFromSettingsFile() {
        if(appDataManager == null) return -1;

        int preferenceLanguage = appDataManager.getLanguageFromPrefFile();

        if(preferenceLanguage == -1) return -1;
        return preferenceLanguage;
    }

    @Override
    public void initAppDataManager() {
        appDataManager = new AppDataManager(context);
        appDataManager.createPreferenceFileService();
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* PresenterThreadCallback interfész függvénye */
    @Override
    public void sendResultToPresenter(Message message) {
        if(mainActivityHandler == null) return;
        mainActivityHandler.sendMessage(message);
    }

    private static class MainActivityHandler extends Handler {

        private WeakReference<IMainActivityPresenter> iMainActivityPresenterWeakReference;

        public MainActivityHandler(Looper looper, IMainActivityPresenter iMainActivityPresenterWeakReference) {
            super(looper);
            this.iMainActivityPresenterWeakReference = new WeakReference<>(iMainActivityPresenterWeakReference);
        }

        // Ui-ra szánt üzenetet kezelejük itt
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
            }
        }
    }
}
