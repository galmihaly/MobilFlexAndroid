package hu.logcontrol.mobilflexandroid.helpers;

import hu.logcontrol.mobilflexandroid.datamanager.AppDataManager;
import hu.logcontrol.mobilflexandroid.enums.RepositoryType;

public class MainActivityPresenterHelper {

    public static String getMessageFromLanguagesFiles(AppDataManager appDataManager, String languageKey, String separator) {
        if(appDataManager == null) return null;
        if(languageKey == null) return null;

        String currentLanguage = appDataManager.getStringValueFromSettingsFile("CurrentSelectedLanguage");
        String result = null;
        switch (currentLanguage){
            case "HU":{
                result = appDataManager.getMessageText(RepositoryType.HUNGARY_PREFERENCES_FILE, "HU" + separator + languageKey);
                break;
            }
            case "EN":{
                result = appDataManager.getMessageText(RepositoryType.ENGLISH_PREFERENCES_FILE, "EN" + separator + languageKey);
                break;
            }
            case "DE":{
                result = appDataManager.getMessageText(RepositoryType.GERMAN_PREFERENCES_FILE, "DE" + separator + languageKey);
                break;
            }
        }

        if(result == null) return null;
        return result;
    }
}
