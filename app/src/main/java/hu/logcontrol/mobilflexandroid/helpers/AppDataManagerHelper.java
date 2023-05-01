package hu.logcontrol.mobilflexandroid.helpers;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Method;

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

    public static String getDeviceSerialNumber(){
        String serialNumber = null;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);

            serialNumber = (String) get.invoke(c, "gsm.sn1");
            if (serialNumber.equals("")) serialNumber = (String) get.invoke(c, "ril.serialnumber");
            if (serialNumber.equals("")) serialNumber = (String) get.invoke(c, "ro.serialno");
            if (serialNumber.equals("")) serialNumber = (String) get.invoke(c, "sys.serialnumber");
            if (serialNumber.equals("")) serialNumber = null;

            Log.e("ser", serialNumber);
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = null;
        }

        if(serialNumber == null) return null;
        return serialNumber;
    }

    public static String getDeviceName(Context context) {
        String deviceName = Settings.Global.getString(context.getApplicationContext().getContentResolver(), "device_name");

        if(deviceName == null) return null;
        return deviceName;
    }
}
