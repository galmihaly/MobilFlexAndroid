package hu.logcontrol.mobilflexandroid.datamanager;

import android.content.Context;
import android.util.Log;

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
    private LocalEncryptedPreferences preferences;

    private String serviceHungaryFileName;
    private String serviceEnglsihFileName;
    private String serviceGermanFileName;

    private static final int[] languagesFlags = new int[] {
            R.drawable.ic_hu2,
            R.drawable.ic_brit,
            R.drawable.ic_german
    };

    private Context context;

    public MainPreferenceFileService(Context context) {
        this.context = context.getApplicationContext();
    }

    public void initPublicSharedPreferenceFiles(String hungaryFileName, String englsihFileName, String germanFileName) {
        this.hungaryWCPrefFile = new LanguagesSharedPreferences(context, hungaryFileName);
        this.englishWCPrefFile = new LanguagesSharedPreferences(context, englsihFileName);
        this.germanWCPrefFile = new LanguagesSharedPreferences(context, germanFileName);

        this.serviceHungaryFileName = hungaryFileName;
        this.serviceEnglsihFileName = englsihFileName;
        this.serviceGermanFileName = germanFileName;

        hungaryWCPrefFile.putString("HU$WC_MessageTextView", "Üzenet");
        englishWCPrefFile.putString("EN$WC_MessageTextView", "Message");
        germanWCPrefFile.putString("DE$WC_MessageTextView", "Nachricht");
    }

    public void initSettingsPreferenceFile(String encryptedFileName) {
        this.preferences = LocalEncryptedPreferences.getInstance(
                encryptedFileName,
                MasterKeys.AES256_GCM_SPEC,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

//    public void saveValueToEncryptedPrefFile(String prefStringKey, String languageID) {
//        if(preferences == null) return;
//        if(prefStringKey == null) return;
//        if(languageID == null) return;
//
//        preferences.replaceString(prefStringKey, languageID);
//    }

//    public String getValueFromLanguageFile(String fileName, String prefStringKey){
//        if(prefStringKey == null) return null;
//
//        if(hungaryWCPrefFile == null) return null;
//        if(englishWCPrefFile == null) return null;
//        if(germanWCPrefFile == null) return null;
//
//        if(serviceHungaryFileName == null) return null;
//        if(serviceEnglsihFileName == null) return null;
//        if(serviceGermanFileName == null) return null;
//
//        String result = null;
//
//        if(fileName.equals(serviceHungaryFileName)) result = hungaryWCPrefFile.getStringValueByKey(prefStringKey);
//        if(fileName.equals(serviceEnglsihFileName)) result =englishWCPrefFile.getStringValueByKey(prefStringKey);
//        if(fileName.equals(serviceGermanFileName)) result = germanWCPrefFile.getStringValueByKey(prefStringKey);
//
//        if(result == null) return null;
//        return result;
//    }

    public String getValueFromHungaryPrefFile(String key){
        if(key == null) return null;
        if(hungaryWCPrefFile == null) return null;

        String value = hungaryWCPrefFile.getStringValueByKey(key);

        if(value == null) return null;
        return value;
    }

    public String getValueFromEnglishPrefFile(String key){
        if(key == null) return null;
        if(englishWCPrefFile == null) return null;

        String value = englishWCPrefFile.getStringValueByKey(key);

        if(value == null) return null;
        return value;
    }

    public String getValueFromGermanPrefFile(String key){
        if(key == null) return null;
        if(germanWCPrefFile == null) return null;

        String value = germanWCPrefFile.getStringValueByKey(key);

        if(value == null) return null;
        return value;
    }

    public String getValueFromSettingsPrefFile(String key){
        if(key == null) return null;
        if(preferences == null) return null;

        String value = preferences.getStringValueByKey(key);

        if(value == null) return null;
        return value;
    }

    public void saveValueToHungaryPrefFile(String key, String value){
        if(key == null) return;
        if(value == null) return;
        if(hungaryWCPrefFile == null) return;
        hungaryWCPrefFile.replaceString(key, value);
     }

    public void saveValueToEnglishPrefFile(String key, String value){
        if(key == null) return;
        if(value == null) return;
        if(englishWCPrefFile == null) return;
        englishWCPrefFile.replaceString(key, value);
    }

    public void saveValueToGermanPrefFile(String key, String value){
        if(key == null) return;
        if(value == null) return;
        if(germanWCPrefFile == null) return;
        germanWCPrefFile.replaceString(key, value);
    }

    public void saveValueToSettingsPrefFile(String key, String value){
        if(key == null) return;
        if(value == null) return;
        if(preferences == null) return;
        preferences.replaceString(key, value);
    }

    public int[] getLanguagesFlags(){ return languagesFlags; }
}
