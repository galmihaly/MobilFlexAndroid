package hu.logcontrol.mobilflexandroid.presenters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.lang.ref.WeakReference;

import hu.logcontrol.mobilflexandroid.LoginActivity;
import hu.logcontrol.mobilflexandroid.MainActivity;
import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.adapters.LanguagesSpinnerAdapter;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivity;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivityPresenter;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.models.LanguagesSharedPreferences;
import hu.logcontrol.mobilflexandroid.models.LocalEncryptedPreferences;
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

        Intent intent = null;

        switch (viewEnum){
            case LOGIN_ACTIVITY:{
                intent = new Intent(context, LoginActivity.class);
                break;
            }
        }

        if(intent == null) return;
        if(iMainActivity != null) iMainActivity.openViewByIntent(intent);
    }

    @Override
    public LanguagesSpinnerAdapter getSpinnerAdapter() {
        languagesImages = new int[] {
                R.drawable.ic_hu2,
                R.drawable.ic_brit,
                R.drawable.ic_german
        };

        LanguagesSpinnerAdapter adapter = new LanguagesSpinnerAdapter(context, languagesImages);

        if(adapter == null) return null;
        return adapter;
    }

    @Override
    public void translateTextBySelectedLanguage(int languageID) {

        String m;

        switch (languageID){
            case R.drawable.ic_hu2:{
                if(hungaryWCPrefFile != null){

                    m = hungaryWCPrefFile.getStringValueByKey("HU$WC_MessageTextView");
                    if(m != null) iMainActivity.setTextToMessageTV(m);
                }
                break;
            }
            case R.drawable.ic_brit:{
                if(englishWCPrefFile != null){

                    m = englishWCPrefFile.getStringValueByKey("EN$WC_MessageTextView");
                    if(m != null) iMainActivity.setTextToMessageTV(m);
                }
                break;
            }
            case R.drawable.ic_german:{
                if(germanWCPrefFile != null){

                    m = germanWCPrefFile.getStringValueByKey("DE$WC_MessageTextView");
                    if(m != null) iMainActivity.setTextToMessageTV(m);
                }
                break;
            }
        }
    }

    @Override
    public void initPublicSharedPreferenceFiles() {
        hungaryWCPrefFile = new LanguagesSharedPreferences(context, "HungaryWCPrefFile");
        englishWCPrefFile = new LanguagesSharedPreferences(context, "EnglishWCPrefFile");
        germanWCPrefFile = new LanguagesSharedPreferences(context, "GermanWCPrefFile");

        hungaryWCPrefFile.putString("HU$WC_MessageTextView", "Üzenet");
        englishWCPrefFile.putString("EN$WC_MessageTextView", "Message");
        germanWCPrefFile.putString("DE$WC_MessageTextView", "Nachricht");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void saveLanguageToSettingsFile(int languageID) {
        int oldLanguageID = -1;

        if(preferences != null){
            oldLanguageID = preferences.getInt("CurrentSelectedLanguage");

            if(oldLanguageID != -1){
                if(oldLanguageID != languageID){
                    preferences.replaceInt("CurrentSelectedLanguage", languageID);
                }
            }
        }
    }

    @Override
    public void initSettingsPreferenceFile() {
        preferences = LocalEncryptedPreferences.getInstance(
                "settings",
                MasterKeys.AES256_GCM_SPEC,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    @Override
    public int getCurrentLanguageFromSettingsFile() {
        if(preferences == null) return -1;
        int preferenceLanguage = preferences.getInt("CurrentSelectedLanguage");
        int currentLanguagePosition = -1;

        for (int i = 0; i < languagesImages.length; i++) {
            if(preferenceLanguage == languagesImages[i]) {
                currentLanguagePosition = i;
                break;
            }
        }

        if(currentLanguagePosition == -1) return -1;
        return currentLanguagePosition;
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
