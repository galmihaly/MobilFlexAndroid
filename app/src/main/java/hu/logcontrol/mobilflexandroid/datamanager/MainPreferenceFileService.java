package hu.logcontrol.mobilflexandroid.datamanager;

import android.content.Context;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.models.LanguagesSharedPreferences;
import hu.logcontrol.mobilflexandroid.models.LocalEncryptedPreferences;

public class MainPreferenceFileService {

    private LanguagesSharedPreferences hungaryWCPrefFile;
    private LanguagesSharedPreferences germanWCPrefFile;
    private LanguagesSharedPreferences englishWCPrefFile;
    private LocalEncryptedPreferences preferences;

    private Context context;
    public MainPreferenceFileService(Context context) {
        this.context = context.getApplicationContext();
    }

    public void initPublicSharedPreferenceFile(String hungaryFileName, String englsihFileName, String germanFileName) {
        this.hungaryWCPrefFile = new LanguagesSharedPreferences(context, hungaryFileName);
        this.englishWCPrefFile = new LanguagesSharedPreferences(context, englsihFileName);
        this.germanWCPrefFile = new LanguagesSharedPreferences(context, germanFileName);
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

    public String getStringValueFromSettingsPrefFile(String key){
        if(key == null) return null;
        if(preferences == null) return null;

        String value = preferences.getStringValueByKey(key);

        if(value == null) return null;
        return value;
    }

    public int getIntValueFromSettingsPrefFile(String key){
        if(key == null) return 0;
        if(preferences == null) return 0;

        int value = preferences.getIntValueByKey(key);
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

    public void saveValueToSettingsPrefFile(String key, int value){
        if(key == null) return;
        if(preferences == null) return;
        preferences.replaceInt(key, value);
    }

    public int[] getLanguagesFlags(){
        return new int[] {
                R.drawable.ic_hu2,
                R.drawable.ic_brit,
                R.drawable.ic_german
        };
    }
}
