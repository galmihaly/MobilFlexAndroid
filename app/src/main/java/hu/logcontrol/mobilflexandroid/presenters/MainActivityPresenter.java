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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

                List<String> languages = new ArrayList<>();
                languages.add("HU");
                languages.add("EN");
                languages.add("DE");

                List<String> wordCodes = new ArrayList<>();
                wordCodes.add("WC_ApplicationLead");
                wordCodes.add("WC_ApplicationUsernameTitle");
                wordCodes.add("WC_ApplicationPasswordTitle");
                wordCodes.add("WC_ApplicationBarCodeTitle");
                wordCodes.add("WC_ApplicationPinCodeTitle");
                wordCodes.add("WC_ApplicationRFIDTitle");
                wordCodes.add("WC_ApplicationLoginButtonTitle");

                HashMap<String, String> translations = new HashMap<>();
                translations.put(languages.get(0) + "$" + wordCodes.get(0), "Ezen az oldalon lehet bejelentkezni!");
                translations.put(languages.get(0) + "$" + wordCodes.get(1), "Felhasználónév");
                translations.put(languages.get(0) + "$" + wordCodes.get(2), "Jelszó");
                translations.put(languages.get(0) + "$" + wordCodes.get(3), "Vonalkód");
                translations.put(languages.get(0) + "$" + wordCodes.get(4), "PINkód");
                translations.put(languages.get(0) + "$" + wordCodes.get(5), "RFID");
                translations.put(languages.get(0) + "$" + wordCodes.get(6), "Bejelentkezés");

                translations.put(languages.get(1) + "$" + wordCodes.get(0), "You can log in on this page!");
                translations.put(languages.get(1) + "$" + wordCodes.get(1), "Username");
                translations.put(languages.get(1) + "$" + wordCodes.get(2), "Password");
                translations.put(languages.get(1) + "$" + wordCodes.get(3), "Barcode");
                translations.put(languages.get(1) + "$" + wordCodes.get(4), "PINcode");
                translations.put(languages.get(1) + "$" + wordCodes.get(5), "RFID");
                translations.put(languages.get(1) + "$" + wordCodes.get(6), "Login");

                translations.put(languages.get(2) + "$" + wordCodes.get(0), "Auf dieser Seite können Sie sich einloggen!");
                translations.put(languages.get(2) + "$" + wordCodes.get(1), "Nutzername");
                translations.put(languages.get(2) + "$" + wordCodes.get(2), "Passwort");
                translations.put(languages.get(2) + "$" + wordCodes.get(3), "Strichkode");
                translations.put(languages.get(2) + "$" + wordCodes.get(4), "Geheimzahl");
                translations.put(languages.get(2) + "$" + wordCodes.get(5), "RFID");
                translations.put(languages.get(2) + "$" + wordCodes.get(6), "Anmeldung");

                SettingsObject settingsObject = new SettingsObject(
                        "1",
                        "name",
                        UUID.randomUUID(),
                        "MobileFlexAndroid",
                        "MobileFlexAndroid",
                        "Ezen a felületen lehet bejelentkezni!",
                        "1",
                        "1.0",
                        15,
                        "logoImageUrl",
                        "https://index.hu",
                        "mainWebApiUrl",
                        "settingsWebApiUrl",
                        "helpWebApiUrl",
                        languages,
                        wordCodes,
                        translations,
                        "#FF9A3A2A",
                        "#FFF1EDCA",
                        "#FFFFFFFF",
                        "#FFFF00FF",
                        "#FF00FF00",
                        "#FFFFFFFF",
                        "#FFFFFFFF"
                );

                intent.putExtra("settingsObject", settingsObject);

                break;
            }
            case MAIN_ACTIVITY:{
                intent = new Intent(context, LoginActivity.class);

                List<String> languages = new ArrayList<>();
                languages.add("HU");
                languages.add("EN");
                languages.add("DE");

                List<String> wordCodes = new ArrayList<>();
                wordCodes.add("WC_ApplicationLead");
                wordCodes.add("WC_ApplicationUsernameTitle");
                wordCodes.add("WC_ApplicationPasswordTitle");
                wordCodes.add("WC_ApplicationBarCodeTitle");
                wordCodes.add("WC_ApplicationPinCodeTitle");
                wordCodes.add("WC_ApplicationRFIDTitle");
                wordCodes.add("WC_ApplicationLoginButtonTitle");

                HashMap<String, String> translations = new HashMap<>();
                translations.put(languages.get(0) + "$" + wordCodes.get(0), "Ezen az oldalon lehet bejelentkezni!");
                translations.put(languages.get(0) + "$" + wordCodes.get(1), "Felhasználónév");
                translations.put(languages.get(0) + "$" + wordCodes.get(2), "Jelszó");
                translations.put(languages.get(0) + "$" + wordCodes.get(3), "Vonalkód");
                translations.put(languages.get(0) + "$" + wordCodes.get(4), "PINkód");
                translations.put(languages.get(0) + "$" + wordCodes.get(5), "RFID");
                translations.put(languages.get(0) + "$" + wordCodes.get(6), "Bejelentkezés");

                translations.put(languages.get(1) + "$" + wordCodes.get(0), "You can log in on this page!");
                translations.put(languages.get(1) + "$" + wordCodes.get(1), "Username");
                translations.put(languages.get(1) + "$" + wordCodes.get(2), "Password");
                translations.put(languages.get(1) + "$" + wordCodes.get(3), "Barcode");
                translations.put(languages.get(1) + "$" + wordCodes.get(4), "PINcode");
                translations.put(languages.get(1) + "$" + wordCodes.get(5), "RFID");
                translations.put(languages.get(1) + "$" + wordCodes.get(6), "Login");

                translations.put(languages.get(2) + "$" + wordCodes.get(0), "Auf dieser Seite können Sie sich einloggen!");
                translations.put(languages.get(2) + "$" + wordCodes.get(1), "Nutzername");
                translations.put(languages.get(2) + "$" + wordCodes.get(2), "Passwort");
                translations.put(languages.get(2) + "$" + wordCodes.get(3), "Strichkode");
                translations.put(languages.get(2) + "$" + wordCodes.get(4), "Geheimzahl");
                translations.put(languages.get(2) + "$" + wordCodes.get(5), "RFID");
                translations.put(languages.get(2) + "$" + wordCodes.get(6), "Anmeldung");

                SettingsObject settingsObject = new SettingsObject(
                        "1",
                        "name",
                        UUID.randomUUID(),
                        "MobileFlexAndroid",
                        "MobileFlexAndroid",
                        "Ezen a felületen lehet bejelentkezni!",
                        "1",
                        "1.0",
                        15,
                        "logoImageUrl",
                        "https://index.hu/",
                        "mainWebApiUrl",
                        "settingsWebApiUrl",
                        "helpWebApiUrl",
                        languages,
                        wordCodes,
                        translations,
                        "#FF66CA9B",
                        "#FFEAD261",
                        "#FF000000",
                        "#FF3087D8",
                        "#FFC4F6F7",
                        "#FF000000",
                        "#FF000000"
                );

                intent.putExtra("settingsObject", settingsObject);

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
    public void translateTextBySelectedLanguage(String languageID) {
        if(hungaryWCPrefFile == null) return;
        if(englishWCPrefFile == null) return;
        if(germanWCPrefFile == null) return;

        String m;

        switch (languageID){
            case "HU":{
                m = hungaryWCPrefFile.getStringValueByKey("HU$WC_MessageTextView");
                if(m != null) iMainActivity.setTextToMessageTV(m);
                break;
            }
            case "EN":{
                m = englishWCPrefFile.getStringValueByKey("EN$WC_MessageTextView");
                if(m != null) iMainActivity.setTextToMessageTV(m);
                break;
            }
            case "DE":{
                m = germanWCPrefFile.getStringValueByKey("DE$WC_MessageTextView");
                if(m != null) iMainActivity.setTextToMessageTV(m);
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
    public void saveLanguageToSettingsFile(String languageID) {
        if(preferences == null) return;

        String oldLanguageID = preferences.getStringValueByKey("CurrentSelectedLanguage");

        if(oldLanguageID != null){
            if(!oldLanguageID.equals(languageID)){
                preferences.replaceString("CurrentSelectedLanguage", languageID);
            }
        }
        else {
            preferences.putString("CurrentSelectedLanguage", languageID);
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
        if(preferences == null) return -2;
        String preferenceLanguage = preferences.getStringValueByKey("CurrentSelectedLanguage");
        int currentLanguagePosition = -1;

        if(preferenceLanguage != null){
            for (int i = 0; i < languagesImages.length; i++) {
                switch (preferenceLanguage){
                    case "HU":{
                        for (int j = 0; j < languagesImages.length; j++) {
                            if(languagesImages[i] == R.drawable.ic_hu2) currentLanguagePosition = i;
                        }
                        break;
                    }
                    case "EN":{
                        for (int j = 0; j < languagesImages.length; j++) {
                            if(languagesImages[i] == R.drawable.ic_brit) currentLanguagePosition = i;
                        }
                        break;
                    }
                    case "DE":{
                        for (int j = 0; j < languagesImages.length; j++) {
                            if(languagesImages[i] == R.drawable.ic_german) currentLanguagePosition = i;
                        }
                        break;
                    }
                }
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
