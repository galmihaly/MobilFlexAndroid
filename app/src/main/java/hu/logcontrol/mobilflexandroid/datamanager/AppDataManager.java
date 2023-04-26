package hu.logcontrol.mobilflexandroid.datamanager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.enums.MessageIdentifiers;
import hu.logcontrol.mobilflexandroid.enums.RepositoryType;
import hu.logcontrol.mobilflexandroid.interfaces.IAppDataManagerHandler;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivityPresenter;
import hu.logcontrol.mobilflexandroid.interfaces.IMainWebAPIService;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.models.Application;
import hu.logcontrol.mobilflexandroid.models.ApplicationTheme;
import hu.logcontrol.mobilflexandroid.models.Device;
import hu.logcontrol.mobilflexandroid.models.ResultObject;
import hu.logcontrol.mobilflexandroid.taskmanager.CustomThreadPoolManager;
import hu.logcontrol.mobilflexandroid.taskmanager.PresenterThreadCallback;
import hu.logcontrol.mobilflexandroid.tasks.MainWebAPICalling;

public class AppDataManager implements PresenterThreadCallback, IAppDataManagerHandler, IMainWebAPIService {

    private Context context;
    private IMainActivityPresenter iMainActivityPresenter;

    private CustomThreadPoolManager mCustomThreadPoolManager;
    private AppDataManagerHandler appDataManagerHandler;
    private static MainPreferenceFileService mainPreferenceFileService;
    private static MainWebAPIService mainWebAPIService;

    private static int[] languagesImages;

    public AppDataManager(Context context, IMainActivityPresenter iMainActivityPresenter) {
        this.context = context.getApplicationContext();
        this.iMainActivityPresenter = iMainActivityPresenter;
    }

    public void createPreferenceFileService(){
        mainPreferenceFileService = new MainPreferenceFileService(context);

        mainPreferenceFileService.initPublicSharedPreferenceFiles(
                "HungaryWCPrefFile",
                "EnglishWCPrefFile",
                "GermanWCPrefFile"
        );

        mainPreferenceFileService.initSettingsPreferenceFile("MobileFlexAndroidSettings");
    }

    public void createMainWebAPIService(){
        mainWebAPIService = MainWebAPIService.getRetrofitInstance("https://api.mobileflex.hu/", this);
    }

    public void initTaskManager() {
        try {
            ApplicationLogger.logging(LogLevel.INFORMATION, "A feladatkezelő létrehozása megkezdődött.");

            appDataManagerHandler = new AppDataManagerHandler(Looper.myLooper(), this);
            mCustomThreadPoolManager = CustomThreadPoolManager.getsInstance();
            mCustomThreadPoolManager.setPresenterCallback(this);

            ApplicationLogger.logging(LogLevel.INFORMATION, "A feladatkezelő létrehozása befejeződött.");
        }
        catch (Exception e){
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    public void saveValueToSettinsPrefFile(String key, String value){
        if(key == null) return;
        if(value == null) return;
        if(mainPreferenceFileService == null) return;

        mainPreferenceFileService.saveValueToSettingsPrefFile(key, value);
    }

    public String getMessageText(RepositoryType hungaryPreferencesFile, String translateCode) {
        if(hungaryPreferencesFile == null) return null;
        if(translateCode == null) return null;
        if(mainPreferenceFileService == null) return null;

        String result = null;

        switch (hungaryPreferencesFile){
            case HUNGARY_PREFERENCES_FILE:{ result = mainPreferenceFileService.getValueFromHungaryPrefFile(translateCode); break; }
            case ENGLISH_PREFERENCES_FILE:{ result = mainPreferenceFileService.getValueFromEnglishPrefFile(translateCode); break; }
            case GERMAN_PREFERENCES_FILE:{ result = mainPreferenceFileService.getValueFromGermanPrefFile(translateCode); break; }
        }

        if(result == null) return null;
        return result;
    }

    public int[] getLanguagesFlags() {
        if(mainPreferenceFileService == null) return null;

        languagesImages = mainPreferenceFileService.getLanguagesFlags();
        if(languagesImages == null) return null;
        return languagesImages;
    }

    public int getLanguageIDFromPrefFile() {
        if(mainPreferenceFileService == null) return -1;
        if(languagesImages == null) return -2;

        String r = mainPreferenceFileService.getStringValueFromSettingsPrefFile("CurrentSelectedLanguage");
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

    public String getValueFromSettingsFile(String valueString) {
        if(valueString == null) return null;
        if(mainPreferenceFileService == null) return null;

        // valide ???

        String result = mainPreferenceFileService.getStringValueFromSettingsPrefFile(valueString);

        if(result == null) return null;
        return result;
    }

    public void loadWebAPICallingTask() {
        if(mCustomThreadPoolManager == null) return;

        try {
            MainWebAPICalling callable = new MainWebAPICalling(context);
            callable.setCustomThreadPoolManager(mCustomThreadPoolManager);
            mCustomThreadPoolManager.addCallableMethod(callable);
        }
        catch (Exception e){
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    public void getDevices(){
        if(mainWebAPIService == null) return;
        mainWebAPIService.sendDeviceDetails("7a0e0865-08b2-488a-8a20-c327ce28e59d", "TESZT", "1");
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* IAppDataManager interfész függvénye */
    @Override
    public void sendResultFromWebAPICallingTask(String resultMessage) {
        if(resultMessage == null) return;
        if(iMainActivityPresenter == null) return;

        iMainActivityPresenter.sendResultToPresenter(resultMessage);
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* IMainWebAPIService interfész függvénye */
    @Override
    public void onSucces(ResultObject resultObject) {
        if(resultObject == null) return;
        if(mainPreferenceFileService == null) return;

        if(resultObject.getResultCode() != null) mainPreferenceFileService.saveValueToSettingsPrefFile("resultCode", resultObject.getResultCode());

        Device device = resultObject.getDevice();
        if(device != null){
            String value = device.getDeviceId();
            if(value != null) {
                mainPreferenceFileService.saveValueToSettingsPrefFile("resultCode", value);
                Log.e("resultCode", mainPreferenceFileService.getStringValueFromSettingsPrefFile("resultCode"));
            }
            else {
                mainPreferenceFileService.saveValueToSettingsPrefFile("resultCode", "");
            }

            value = device.getDeviceName();
            if(value != null) {
                mainPreferenceFileService.saveValueToSettingsPrefFile("deviceName", device.getDeviceName());
                Log.e("deviceName", mainPreferenceFileService.getStringValueFromSettingsPrefFile("deviceName"));
            }
            else {
                mainPreferenceFileService.saveValueToSettingsPrefFile("deviceName", "");
            }


            value = device.getActive();
            if(value != null) {
                mainPreferenceFileService.saveValueToSettingsPrefFile("active", device.getActive());
                Log.e("active", mainPreferenceFileService.getStringValueFromSettingsPrefFile("active"));
            }
            else {
                mainPreferenceFileService.saveValueToSettingsPrefFile("active", "");
            }

            value = device.getLastDeviceLoginDate();
            if(value != null) {
                mainPreferenceFileService.saveValueToSettingsPrefFile("lastDeviceLoginDate", device.getLastDeviceLoginDate());
                Log.e("lastDeviceLoginDate", mainPreferenceFileService.getStringValueFromSettingsPrefFile("lastDeviceLoginDate"));
            }
            else {
                mainPreferenceFileService.saveValueToSettingsPrefFile("lastDeviceLoginDate", "");
            }

            value = device.getComments();
            if(value != null) {
                mainPreferenceFileService.saveValueToSettingsPrefFile("comments", device.getComments());
                Log.e("comments", mainPreferenceFileService.getStringValueFromSettingsPrefFile("comments"));
            }
            else {
                mainPreferenceFileService.saveValueToSettingsPrefFile("comments", "");
            }

            List<Application> applications = device.getApplicationList();
            if(applications != null) {
                for (int i = 0; i < applications.size(); i++) {

                    value = applications.get(i).getId().toString();
                    if(value != null) {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("id" + (i + 1), value);
                        Log.e("id" + (i + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("id" + (i + 1)));
                    }
                    else {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("id" + (i + 1), "");
                    }

                    value = applications.get(i).getApplicationName();
                    if(value != null) {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("applicationName" + (i + 1), value);
                        Log.e("applicationName" + (i + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("applicationName" + (i + 1)));
                    }
                    else {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("applicationName" + (i + 1), "");
                    }

                    value = applications.get(i).getApplicationTitle();
                    if(value != null) {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("applicationTitle" + (i + 1), value);
                        Log.e("applicationTitle" + (i + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("applicationTitle" + (i + 1)));
                    }
                    else {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("applicationTitle" + (i + 1), "");
                    }

                    value = applications.get(i).getApplicationLead();
                    if(value != null) {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("applicationLead" + (i + 1), value);
                        Log.e("applicationLead" + (i + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("applicationLead" + (i + 1)));
                    }
                    else {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("applicationLead" + (i + 1), "");
                    }

                    value = applications.get(i).getApplicationDescription();
                    if(value != null) {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("applicationDescription" + (i + 1), value);
                        Log.e("applicationDescription" + (i + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("applicationDescription" + (i + 1)));
                    }
                    else {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("applicationDescription" + (i + 1), "");
                    }

                    value = applications.get(i).getApplicationVersion();
                    if(value != null) {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("applicationVersion" + (i + 1), value);
                        Log.e("applicationVersion" + (i + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("applicationVersion" + (i + 1)));
                    }
                    else {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("applicationVersion" + (i + 1), "");
                    }

                    int value_2 = applications.get(i).getApplicationEnabledLoginFlag();
                    if(value != null) {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("applicationEnabledLoginFlag" + (i + 1), value_2);
                        Log.e("applicationEnabledLoginFlag" + (i + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("applicationEnabledLoginFlag" + (i + 1)));
                    }
                    else {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("applicationEnabledLoginFlag" + (i + 1), "");
                    }

                    value = applications.get(i).getLoginUrl();
                    if(value != null) {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("loginUrl" + (i + 1), value);
                        Log.e("loginUrl" + (i + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("loginUrl" + (i + 1)));
                    }
                    else {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("loginUrl" + (i + 1), "");
                    }

                    value = applications.get(i).getMainUrl();
                    if(value != null) {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("mainUrl" + (i + 1), value);
                        Log.e("mainUrl" + (i + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("mainUrl" + (i + 1)));
                    }
                    else {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("mainUrl" + (i + 1), "");
                    }

                    value = applications.get(i).getSettingsUrl();
                    if(value != null) {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("settingsUrl" + (i + 1), value);
                        Log.e("settingsUrl" + (i + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("settingsUrl" + (i + 1)));
                    }
                    else {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("settingsUrl" + (i + 1), "");
                    }

                    value = applications.get(i).getHelpUrl();
                    if(value != null) {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("helpUrl" + (i + 1), value);
                        Log.e("helpUrl" + (i + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("helpUrl" + (i + 1)));
                    }
                    else {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("helpUrl" + (i + 1), "");
                    }

                    value_2 = applications.get(i).getDefaultThemeId();
                    if(value != null) {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("defaultThemeId" + (i + 1), value_2);
                        Log.e("defaultThemeId" + (i + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("defaultThemeId" + (i + 1)));
                    }
                    else {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("defaultThemeId" + (i + 1), "");
                    }

                    value = applications.get(i).getLogoUrl();
                    if(value != null) {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("logoUrl" + (i + 1), value);
                        Log.e("logoUrl" + (i + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("logoUrl" + (i + 1)));
                    }
                    else {
                        mainPreferenceFileService.saveValueToSettingsPrefFile("logoUrl" + (i + 1), "");
                    }

                    List<ApplicationTheme> applicationThemes = applications.get(i).getApplicationThemeList();
                    if(applicationThemes != null){
                        for (int l = 0; l < applicationThemes.size(); l++) {

                            value_2 = applicationThemes.get(l).getId();
                            if(value != null) {
                                mainPreferenceFileService.saveValueToSettingsPrefFile("id" + (l + 1), value_2);
                                Log.e("id" + (l + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("id" + (l + 1)));
                            }
                            else {
                                mainPreferenceFileService.saveValueToSettingsPrefFile("logoUrl" + (i + 1), "");
                            }

                            value = applicationThemes.get(l).getApplicationId().toString();
                            if(value != null) {
                                mainPreferenceFileService.saveValueToSettingsPrefFile("applicationId" + (l + 1), value);
                                Log.e("applicationId" + (l + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("applicationId" + (l + 1)));
                            }
                            else {
                                mainPreferenceFileService.saveValueToSettingsPrefFile("applicationId" + (l + 1), "");
                            }

                            value = applications.get(l).getLogoUrl();
                            if(value != null) {
                                mainPreferenceFileService.saveValueToSettingsPrefFile("logoUrl" + (l + 1), value);
                                Log.e("logoUrl" + (l + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("logoUrl" + (l + 1)));
                            }
                            else {
                                mainPreferenceFileService.saveValueToSettingsPrefFile("logoUrl" + (l + 1), "");
                            }

                            value = applicationThemes.get(l).getName();
                            if(value != null) {
                                mainPreferenceFileService.saveValueToSettingsPrefFile("name" + (l + 1), value);
                                Log.e("name" + (l + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("name" + (l + 1)));
                            }
                            else {
                                mainPreferenceFileService.saveValueToSettingsPrefFile("name" + (l + 1), "");
                            }

                            saveColorToSettingsPrefFile("backgroundColor" + (l + 1), applicationThemes.get(l).getBackgroundColor());
                            saveColorToSettingsPrefFile("backgroundGradientColor" + (l + 1), applicationThemes.get(l).getBackgroundGradientColor());
                            saveColorToSettingsPrefFile("foregroundColor" + (l + 1), applicationThemes.get(l).getForegroundColor());
                            saveColorToSettingsPrefFile("buttonBackgroundColor" + (l + 1), applicationThemes.get(l).getButtonBackgroundColor());
                            saveColorToSettingsPrefFile("buttonBackgroundGradientColor" + (l + 1), applicationThemes.get(l).getButtonBackgroundGradientColor());
                            saveColorToSettingsPrefFile("buttonForegroundColor" + (l + 1), applicationThemes.get(l).getButtonForegroundColor());
                            saveColorToSettingsPrefFile("controlColor" + (l + 1), applicationThemes.get(l).getControlColor());

                            Log.e("backgroundColor" + (l + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("backgroundColor" + (l + 1)));
                            Log.e("backgroundGradientColor" + (l + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("backgroundGradientColor" + (l + 1)));
                            Log.e("foregroundColor" + (l + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("foregroundColor" + (l + 1)));
                            Log.e("buttonBackgroundColor" + (l + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("buttonBackgroundColor" + (l + 1)));
                            Log.e("buttonBackgroundGradientColor" + (l + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("buttonBackgroundGradientColor" + (l + 1)));
                            Log.e("buttonForegroundColor" + (l + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("buttonForegroundColor" + (l + 1)));
                            Log.e("controlColor" + (l + 1), mainPreferenceFileService.getStringValueFromSettingsPrefFile("controlColor" + (l + 1)));

                        }
                    }
                }
            }
        }

        // TODO a resultCode-kat át kell majd beszélni Imivel
    }

    private void saveColorToSettingsPrefFile(String number , String value){
        if(value == null) return;
        if(value.contains("#")){
            mainPreferenceFileService.saveValueToSettingsPrefFile(number, value);
        }
        else {
            mainPreferenceFileService.saveValueToSettingsPrefFile(number, "#" + value);
        }
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* PresenterThreadCallback interfész függvénye */
    @Override
    public void sendResultToPresenter(Message message) {
        if(appDataManagerHandler == null) return;
        appDataManagerHandler.sendMessage(message);
    }

    private static class AppDataManagerHandler extends Handler {

        private WeakReference<IAppDataManagerHandler> iAppDataManagerHandler;

        public AppDataManagerHandler(Looper looper, IAppDataManagerHandler iAppDataManagerHandler) {
            super(looper);
            this.iAppDataManagerHandler = new WeakReference<>(iAppDataManagerHandler);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MessageIdentifiers.HARDWARE_ID_FAILED:
                case MessageIdentifiers.THREAD_INTERRUPTED: {
                    iAppDataManagerHandler.get().sendResultFromWebAPICallingTask(getWeakReferenceNotification(msg));
                    break;
                }
            }
        }

        private String getWeakReferenceNotification(Message message){
            Bundle bundle = message.getData();
            return bundle.getString(MessageIdentifiers.MESSAGE_BODY);
        }
    }
}
