package hu.logcontrol.mobilflexandroid.datamanager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.UUID;

import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.enums.MessageIdentifiers;
import hu.logcontrol.mobilflexandroid.enums.RepositoryType;
import hu.logcontrol.mobilflexandroid.interfaces.IAppDataManagerHandler;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivityPresenter;
import hu.logcontrol.mobilflexandroid.interfaces.IMainWebAPIService;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.models.Device;
import hu.logcontrol.mobilflexandroid.models.DeviceRequest;
import hu.logcontrol.mobilflexandroid.taskmanager.CustomThreadPoolManager;
import hu.logcontrol.mobilflexandroid.taskmanager.PresenterThreadCallback;
import hu.logcontrol.mobilflexandroid.tasks.MainWebAPICalling;
import retrofit2.Call;

public class AppDataManager implements PresenterThreadCallback, IAppDataManagerHandler {

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
        mainWebAPIService = MainWebAPIService.getRetrofitInstance("https://api.mobileflex.hu/");
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

        String r = mainPreferenceFileService.getValueFromSettingsPrefFile("CurrentSelectedLanguage");
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

        String result = mainPreferenceFileService.getValueFromSettingsPrefFile(valueString);

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
        mainWebAPIService.sendDeviceRequestObject();
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* IAppDataManager interfész függvénye */
    @Override
    public void sendResultFromWebAPICallingTask(String resultMessage) {
        if(resultMessage == null) return;
        if(iMainActivityPresenter == null) return;

        iMainActivityPresenter.sendResultToPresenter(resultMessage);
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

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* PresenterThreadCallback interfész függvénye */
    @Override
    public void sendResultToPresenter(Message message) {
        if(appDataManagerHandler == null) return;
        appDataManagerHandler.sendMessage(message);
    }
}
