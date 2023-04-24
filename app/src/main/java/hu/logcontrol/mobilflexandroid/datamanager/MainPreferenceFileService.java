package hu.logcontrol.mobilflexandroid.datamanager;

import android.content.Context;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.enums.RepositoryType;
import hu.logcontrol.mobilflexandroid.models.LanguagesSharedPreferences;
import hu.logcontrol.mobilflexandroid.models.LocalEncryptedPreferences;
import hu.logcontrol.mobilflexandroid.models.SettingsObject;

public class MainPreferenceFileService {

    private LanguagesSharedPreferences hungaryWCPrefFile;
    private LanguagesSharedPreferences germanWCPrefFile;
    private LanguagesSharedPreferences englishWCPrefFile;

    private static final int[] languagesFlags = new int[] {
            R.drawable.ic_hu2,
            R.drawable.ic_brit,
            R.drawable.ic_german
    };

    private LocalEncryptedPreferences preferences;

    private Context context;

    public MainPreferenceFileService(Context context) {
        this.context = context.getApplicationContext();
    }

    public void initPublicSharedPreferenceFiles(String hungaryFileName, String englsihFileName, String germanFileName) {
        hungaryWCPrefFile = new LanguagesSharedPreferences(context, hungaryFileName);
        englishWCPrefFile = new LanguagesSharedPreferences(context, englsihFileName);
        germanWCPrefFile = new LanguagesSharedPreferences(context, germanFileName);

        hungaryWCPrefFile.putString("HU$WC_MessageTextView", "Üzenet");
        englishWCPrefFile.putString("EN$WC_MessageTextView", "Message");
        germanWCPrefFile.putString("DE$WC_MessageTextView", "Nachricht");
    }

    public void initSettingsPreferenceFile(String encryptedFileName) {
        preferences = LocalEncryptedPreferences.getInstance(
                encryptedFileName,
                MasterKeys.AES256_GCM_SPEC,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public SettingsObject initSettinsObject(){
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

        return settingsObject;
    }

    public void saveValueToEncryptedPrefFile(String prefStringKey, String languageID) {
        if(preferences == null) return;
        if(prefStringKey == null) return;
        if(languageID == null) return;
        preferences.replaceString(prefStringKey, languageID);
    }

    public String getValueFromLanguageFile(RepositoryType type, String prefStringKey){
        if(type == null) return null;
        if(prefStringKey == null) return null;

        String result = null;

        switch (type){
            case HUNGARY_PREFERENCES_FILE:{ result = hungaryWCPrefFile.getStringValueByKey(prefStringKey); break; }
            case ENGLISH_PREFERENCES_FILE:{ result = englishWCPrefFile.getStringValueByKey(prefStringKey); break; }
            case GERMAN_PREFERENCES_FILE:{ result = germanWCPrefFile.getStringValueByKey(prefStringKey); break; }
        }

        if(result == null) return null;
        return result;
    }

    public String getValueFromEncryptedPreferenceFile(String prefStringKey){
        if(prefStringKey == null) return null;
        if(preferences == null) return null;

        String result = preferences.getStringValueByKey(prefStringKey);

        if(result == null) return null;
        return result;
    }

    public int[] getLanguagesFlags(){ return languagesFlags; }
}
