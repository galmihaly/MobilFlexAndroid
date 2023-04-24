package hu.logcontrol.mobilflexandroid.datamanager;

import android.content.Context;
import android.util.Log;

import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.enums.RepositoryType;
import hu.logcontrol.mobilflexandroid.models.SettingsObject;

public class AppDataManager {

    private Context context;
    private static MainPreferenceFileService mainPreferenceFileService;

    private static int[] languagesImages;

    public AppDataManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public void createPreferenceFileService(){
        mainPreferenceFileService = new MainPreferenceFileService(context);

        mainPreferenceFileService.initPublicSharedPreferenceFiles(
                "HungaryWCPrefFile",
                "EnglishWCPrefFile",
                "GermanWCPrefFile"
        );

        mainPreferenceFileService.initSettingsPreferenceFile("settings");
    }

    public SettingsObject getBaseSettingsObjectFromPrefFile(){
        if(mainPreferenceFileService == null) return null;
        return mainPreferenceFileService.initSettinsObject();
    }

    public void saveLanguageIDToPrefFile(String prefStringKey, String languageID){
        if(mainPreferenceFileService == null) return;
        mainPreferenceFileService.saveValueToEncryptedPrefFile(prefStringKey, languageID);
    }

    public String getMessageText(RepositoryType hungaryPreferencesFile, String translateCode) {
        if(hungaryPreferencesFile == null) return null;
        if(translateCode == null) return null;
        if(mainPreferenceFileService == null) return null;

        String result = mainPreferenceFileService.getValueFromLanguageFile(hungaryPreferencesFile, translateCode);

        if(result == null) return null;
        return result;
    }

    public int[] getLanguagesFlags() {
        languagesImages = mainPreferenceFileService.getLanguagesFlags();
        return languagesImages;
    }

    public int getLanguageFromPrefFile() {
        if(mainPreferenceFileService == null) return -1;
        if(languagesImages == null) return -2;

        String r = mainPreferenceFileService.getValueFromEncryptedPreferenceFile("CurrentSelectedLanguage");

        Log.e("preferenceLanguage", r);
        int currentLanguagePosition = -1;

        if(r != null){
            for (int i = 0; i < languagesImages.length; i++) {
                switch (r){
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

        return currentLanguagePosition;
    }
}
