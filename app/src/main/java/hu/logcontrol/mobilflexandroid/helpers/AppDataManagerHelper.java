package hu.logcontrol.mobilflexandroid.helpers;

import android.util.Log;

import hu.logcontrol.mobilflexandroid.datamanager.MainPreferenceFileService;
import hu.logcontrol.mobilflexandroid.enums.SaveValueType;

public class AppDataManagerHelper {

    public static void saveStringValueToPrefFile(MainPreferenceFileService mainPreferenceFileService, String key, String value) {
        if(mainPreferenceFileService == null) return;
        if(key == null) return;

        if(value != null){
            mainPreferenceFileService.saveValueToSettingsPrefFile(key, value);
        }
        else {
            mainPreferenceFileService.saveValueToSettingsPrefFile(key, "");
        }

        Log.e(key, mainPreferenceFileService.getStringValueFromSettingsPrefFile(key));
    }

    public static void saveIntValueToPrefFile(MainPreferenceFileService mainPreferenceFileService, String key, int value) {
        if(mainPreferenceFileService == null) return;
        if(key == null) return;

        if(value >= 0){
            mainPreferenceFileService.saveValueToSettingsPrefFile(key, value);
        }
        else {
            mainPreferenceFileService.saveValueToSettingsPrefFile(key, -1);
        }

        Log.e(key, String.valueOf(mainPreferenceFileService.getIntValueFromSettingsPrefFile(key)));
    }

    public static void saveColorToSettingsPrefFile(MainPreferenceFileService mainPreferenceFileService, String key, String colorValue) {
        if(mainPreferenceFileService == null) return;
        if(key == null) return;
        if(colorValue == null) return;

        if(colorValue.contains("#")){
            mainPreferenceFileService.saveValueToSettingsPrefFile(key, colorValue);
        }
        else {
            mainPreferenceFileService.saveValueToSettingsPrefFile(key, "#" + colorValue);
        }

        Log.e(key, mainPreferenceFileService.getStringValueFromSettingsPrefFile(key));
    }
}
