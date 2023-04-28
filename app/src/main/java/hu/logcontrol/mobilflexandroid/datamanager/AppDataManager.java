package hu.logcontrol.mobilflexandroid.datamanager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;

import java.lang.ref.WeakReference;
import java.util.List;

import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.enums.MessageIdentifiers;
import hu.logcontrol.mobilflexandroid.enums.RepositoryType;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;
import hu.logcontrol.mobilflexandroid.fragments.interfaces.ILoginFragments;
import hu.logcontrol.mobilflexandroid.fragments.interfaces.ILoginFragmentsPresenter;
import hu.logcontrol.mobilflexandroid.helpers.AppDataManagerHelper;
import hu.logcontrol.mobilflexandroid.interfaces.IAppDataManagerHandler;
import hu.logcontrol.mobilflexandroid.interfaces.ILoginActivityPresenter;
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
import hu.logcontrol.mobilflexandroid.tasks.CreateLoginButtons;

public class AppDataManager implements PresenterThreadCallback, IAppDataManagerHandler, IMainWebAPIService {

    private Context context;
    private IMainActivityPresenter iMainActivityPresenter;
    private ILoginActivityPresenter iLoginActivityPresenter;
    private ILoginFragmentsPresenter iLoginFragmentsPresenter;

    private CustomThreadPoolManager mCustomThreadPoolManager;
    private AppDataManagerHandler appDataManagerHandler;
    private static MainPreferenceFileService mainPreferenceFileService;
    private static MainWebAPIService mainWebAPIService;

    private static int[] languagesImages;

    public AppDataManager(Context context, IMainActivityPresenter iMainActivityPresenter) {
        this.context = context.getApplicationContext();
        this.iMainActivityPresenter = iMainActivityPresenter;
    }

    public AppDataManager(Context context, ILoginActivityPresenter iLoginActivityPresenter) {
        this.context = context.getApplicationContext();
        this.iLoginActivityPresenter = iLoginActivityPresenter;
    }

    public AppDataManager(Context context, ILoginFragmentsPresenter iLoginFragmentsPresenter) {
        this.context = context.getApplicationContext();
        this.iLoginFragmentsPresenter = iLoginFragmentsPresenter;
    }

    public void createPreferenceFileService(){
        mainPreferenceFileService = new MainPreferenceFileService(context);
    }

    public void initSettingsPrefFile(){
        if(mainPreferenceFileService == null) return;
        mainPreferenceFileService.initSettingsPreferenceFile("MobileFlexAndroidSettings");
    }

    public void initLanguagesPrefFile(){
        if(mainPreferenceFileService == null) return;

        mainPreferenceFileService.initPublicSharedPreferenceFiles(
                "HungaryWCPrefFile",
                "EnglishWCPrefFile",
                "GermanWCPrefFile"
        );
    }

    public void initBaseMessagesPrefFile(){
        if(mainPreferenceFileService == null) return;

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_NETWORK_AVAILABLE", "WIFI hálózat elérhető!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_NETWORK_AVAILABLE", "WIFI network is available!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_NETWORK_AVAILABLE", "WIFI-Netz ist verfügbar!");

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_NETWORK_NOT_AVAILABLE", "WIFI hálózat nem elérhető!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_NETWORK_NOT_AVAILABLE", "WIFI network is not available!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_NETWORK_NOT_AVAILABLE", "WIFI-Netz ist nicht verfügbar!");

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_DATA_RETIREVAL_START", "Az adatok lekérdezése a Web API-ról megkezdődött!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_DATA_RETIREVAL_START", "Data retrieval has started from the Web API!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_DATA_RETIREVAL_START", "Der Datenabruf von der Web-API hat begonnen!");

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_DATA_RETIREVAL_END", "Az adatok lekérdezése a Web API-ról befejeződött!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_DATA_RETIREVAL_END", "Data retrieval from the Web API is complete!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_DATA_RETIREVAL_END", "Der Datenabruf von der Web-API ist abgeschlossen!");

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_BASE_WEBAPI_NOT_AVAILABLE", "Az alap WEB API nem elérhető!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_BASE_WEBAPI_NOT_AVAILABLE", "The basic Web API is available!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_BASE_WEBAPI_NOT_AVAILABLE", "Die grundlegende Web-API ist verfügbar!");
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

    public String getStringValueFromSettingsFile(String key) {
        if(key == null) return null;
        if(mainPreferenceFileService == null) return null;

        String result = mainPreferenceFileService.getStringValueFromSettingsPrefFile(key);

        if(result == null) return null;
        return result;
    }

    public int getIntValueFromSettingsFile(String key) {
        if(key == null) return -1;
        if(mainPreferenceFileService == null) return -1;

        int result = mainPreferenceFileService.getIntValueFromSettingsPrefFile(key);

        return result;
    }

    public void callSettingsWebAPI(){
        if(mainWebAPIService == null) return;
        mainWebAPIService.sendDeviceDetails("7a0e0865-08b2-488a-8a20-c327ce28e59d", "TESZT", "1");
    }

    public void initLoginButtons(int loginModesNumber, WindowSizeTypes[] wsc, int[] colors){
        try {
            CreateLoginButtons callable = new CreateLoginButtons(context.getApplicationContext(), loginModesNumber, wsc, colors);
            callable.setCustomThreadPoolManager(mCustomThreadPoolManager);
            mCustomThreadPoolManager.addCallableMethod(callable);
        }
        catch (Exception e){
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* IAppDataManager interfész függvénye */
    @Override
    public void sendResultFromWebAPICallingTask(String resultMessage) {
        if(resultMessage == null) return;
        if(iMainActivityPresenter == null) return;

        iMainActivityPresenter.sendWebAPIResultToPresenter(resultMessage);
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* IMainWebAPIService interfész függvénye */
    @Override
    public void onSucces(ResultObject resultObject) {
        if(resultObject == null) return;
        if(iMainActivityPresenter == null ) return;
        if(mainPreferenceFileService == null) return;

        AppDataManagerHelper.saveIntValueToPrefFile(mainPreferenceFileService, "resultCode", resultObject.getResultCode());

        Device device = resultObject.getDevice();
        if(device != null){

            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "deviceId", device.getDeviceId());
            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "deviceName", device.getDeviceName());
            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "active", device.getActive());
            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "lastDeviceLoginDate", device.getLastDeviceLoginDate());
            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "comments", device.getComments());

            List<Application> applications = device.getApplicationList();

            if(applications != null) {

                AppDataManagerHelper.saveIntValueToPrefFile(mainPreferenceFileService, "applicationsNumber", device.getApplicationList().size());

                for (int i = 0; i < applications.size(); i++) {

                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "comments" + (i + 1), applications.get(i).getId().toString());
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "applicationName" + (i + 1), applications.get(i).getApplicationName());
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "applicationTitle" + (i + 1), applications.get(i).getApplicationTitle());
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "applicationLead" + (i + 1), applications.get(i).getApplicationLead());
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "applicationDescription" + (i + 1), applications.get(i).getApplicationDescription());
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "applicationVersion" + (i + 1), applications.get(i).getApplicationVersion());
                    AppDataManagerHelper.saveIntValueToPrefFile(mainPreferenceFileService, "applicationEnabledLoginFlag" + (i + 1), applications.get(i).getApplicationEnabledLoginFlag());
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "loginUrl" + (i + 1), applications.get(i).getLoginUrl());
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "mainUrl" + (i + 1), applications.get(i).getMainUrl());
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "settingsUrl" + (i + 1), applications.get(i).getSettingsUrl());
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "helpUrl" + (i + 1), applications.get(i).getHelpUrl());
                    AppDataManagerHelper.saveIntValueToPrefFile(mainPreferenceFileService, "defaultThemeId" + (i + 1), applications.get(i).getDefaultThemeId());
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "logoUrl" + (i + 1), applications.get(i).getLogoUrl());

                    List<ApplicationTheme> applicationThemes = applications.get(i).getApplicationThemeList();
                    if(applicationThemes != null){

                        AppDataManagerHelper.saveIntValueToPrefFile(mainPreferenceFileService, "applicationThemesNumber", applications.get(i).getApplicationThemeList().size());

                        for (int l = 0; l < applicationThemes.size(); l++) {

                            AppDataManagerHelper.saveIntValueToPrefFile(mainPreferenceFileService, "id" + (l + 1), applicationThemes.get(l).getId());
                            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "applicationId" + (l + 1), applicationThemes.get(l).getApplicationId().toString());
                            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "logoUrl" + (l + 1), applications.get(l).getLogoUrl());
                            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "name" + (l + 1), applicationThemes.get(l).getName());
                            AppDataManagerHelper.saveColorToSettingsPrefFile(mainPreferenceFileService, "backgroundColor" + (l + 1), applicationThemes.get(l).getBackgroundColor());
                            AppDataManagerHelper.saveColorToSettingsPrefFile(mainPreferenceFileService, "backgroundGradientColor" + (l + 1), applicationThemes.get(l).getBackgroundGradientColor());
                            AppDataManagerHelper.saveColorToSettingsPrefFile(mainPreferenceFileService, "foregroundColor" + (l + 1), applicationThemes.get(l).getForegroundColor());
                            AppDataManagerHelper.saveColorToSettingsPrefFile(mainPreferenceFileService, "buttonBackgroundColor" + (l + 1), applicationThemes.get(l).getButtonBackgroundColor());
                            AppDataManagerHelper.saveColorToSettingsPrefFile(mainPreferenceFileService, "buttonBackgroundGradientColor" + (l + 1), applicationThemes.get(l).getButtonBackgroundGradientColor());
                            AppDataManagerHelper.saveColorToSettingsPrefFile(mainPreferenceFileService, "buttonForegroundColor" + (l + 1), applicationThemes.get(l).getButtonForegroundColor());
                            AppDataManagerHelper.saveColorToSettingsPrefFile(mainPreferenceFileService, "controlColor" + (l + 1), applicationThemes.get(l).getControlColor());
                        }
                    }
                }
            }
        }

        String message = getMessageFromLanguagesFiles( "WC_DATA_RETIREVAL_END", "$");
        if(message != null ) iMainActivityPresenter.sendWebAPIResultToPresenter(message);

        // TODO a resultCode-kat át kell majd beszélni Imivel
    }

    @Override
    public void onFailure() {
        if(iMainActivityPresenter == null) return;

        String message = getMessageFromLanguagesFiles( "WC_BASE_WEBAPI_NOT_AVAILABLE", "$");
        if(message != null ) iMainActivityPresenter.sendWebAPIResultToPresenter(message);

    }

    public String getMessageFromLanguagesFiles(String languageKey, String separator){
        if(languageKey == null) return null;

        String currentLanguage = getStringValueFromSettingsFile("CurrentSelectedLanguage");
        String result = null;
        switch (currentLanguage){
            case "HU":{
                result = getMessageText(RepositoryType.HUNGARY_PREFERENCES_FILE, "HU" + separator + languageKey);
                break;
            }
            case "EN":{
                result = getMessageText(RepositoryType.ENGLISH_PREFERENCES_FILE, "EN" + separator + languageKey);
                break;
            }
            case "DE":{
                result = getMessageText(RepositoryType.GERMAN_PREFERENCES_FILE, "DE" + separator + languageKey);
                break;
            }
        }

        if(result == null) return null;
        return result;
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* PresenterThreadCallback interfész függvénye */
    @Override
    public void sendMessageToHandler(Message message) {
        if(appDataManagerHandler == null) return;
        appDataManagerHandler.sendMessage(message);
    }

    @Override
    public void sendMessageToLoginView(String message) {
        if(message == null) return;
        if(iLoginActivityPresenter != null) iLoginActivityPresenter.getMessageFromAppDataManager(message);
    }

    @Override
    public void sendCreatedButtonsToLoginView(List<ImageButton> createdButtons) {
        if(createdButtons == null) return;
        if(iLoginActivityPresenter == null) return;
        iLoginActivityPresenter.getCreatedButtonsFromAppDataManager(createdButtons);
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
                case MessageIdentifiers.EXCEPTION:{
                    iAppDataManagerHandler.get().sendMessageToLoginView(getWeakReferenceNotification(msg));
                    break;
                }
                case MessageIdentifiers.BUTTONS_IS_CREATED:{
                    Log.e("handleMessage", "beléptem ide");
                    if(msg.obj instanceof List){
                        List<ImageButton> createdButtons = (List<ImageButton>) msg.obj;
                        iAppDataManagerHandler.get().sendCreatedButtonsToLoginView(createdButtons);
                    }
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
