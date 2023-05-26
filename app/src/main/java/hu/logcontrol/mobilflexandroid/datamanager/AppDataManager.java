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
import hu.logcontrol.mobilflexandroid.enums.DeviceResponse;
import hu.logcontrol.mobilflexandroid.enums.LoginResponse;
import hu.logcontrol.mobilflexandroid.enums.MessageIdentifiers;
import hu.logcontrol.mobilflexandroid.enums.RepositoryType;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;
import hu.logcontrol.mobilflexandroid.fragments.interfaces.ILoginFragmentsPresenter;
import hu.logcontrol.mobilflexandroid.helpers.AppDataManagerHelper;
import hu.logcontrol.mobilflexandroid.interfaces.IAppDataManagerHandler;
import hu.logcontrol.mobilflexandroid.interfaces.ILoginActivityPresenter;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivityPresenter;
import hu.logcontrol.mobilflexandroid.interfaces.IMainWebAPIService;
import hu.logcontrol.mobilflexandroid.interfaces.IProgramsPresenter;
import hu.logcontrol.mobilflexandroid.interfaces.ISettingsWebAPIService;
import hu.logcontrol.mobilflexandroid.interfaces.IWebViewActivityPresenter;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.models.Application;
import hu.logcontrol.mobilflexandroid.models.ApplicationTheme;
import hu.logcontrol.mobilflexandroid.models.Device;
import hu.logcontrol.mobilflexandroid.models.ProgramsResultObject;
import hu.logcontrol.mobilflexandroid.models.MainWebAPIResponseObject;
import hu.logcontrol.mobilflexandroid.models.SettingsWebAPIResponseObject;
import hu.logcontrol.mobilflexandroid.taskmanager.CustomThreadPoolManager;
import hu.logcontrol.mobilflexandroid.taskmanager.PresenterThreadCallback;
import hu.logcontrol.mobilflexandroid.tasks.CreateLoginButtons;
import hu.logcontrol.mobilflexandroid.tasks.DownloadPNGLogo;

public class AppDataManager implements PresenterThreadCallback, IAppDataManagerHandler, IMainWebAPIService, ISettingsWebAPIService {

    private Context context;
    private IMainActivityPresenter iMainActivityPresenter;
    private ILoginActivityPresenter iLoginActivityPresenter;
    private ILoginFragmentsPresenter iLoginFragmentsPresenter;
    private IWebViewActivityPresenter iWebViewActivityPresenter;
    private IProgramsPresenter iProgramsPresenter;

    private CustomThreadPoolManager mCustomThreadPoolManager;
    private AppDataManagerHandler appDataManagerHandler;
    private static MainPreferenceFileService mainPreferenceFileService;
    private static MainWebAPIService mainWebAPIService;
    private static LoginWebAPIService loginWebAPIService;
    private StringBuilder themes;

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

    public AppDataManager(Context context, IProgramsPresenter iProgramsPresenter) {
        this.context = context.getApplicationContext();
        this.iProgramsPresenter = iProgramsPresenter;
    }

    public AppDataManager(Context context, IWebViewActivityPresenter iWebViewActivityPresenter) {
        this.context = context.getApplicationContext();
        this.iWebViewActivityPresenter = iWebViewActivityPresenter;
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

        mainPreferenceFileService.initPublicSharedPreferenceFile(
                "HungaryWCPrefFile",
                "EnglishWCPrefFile",
                "GermanWCPrefFile"
        );
    }

    public void initBaseMessagesPrefFile(){
        if(mainPreferenceFileService == null) return;

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_NEW_WEBAPI", "Az új WebAPI cím sikeresen elmentődött!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_NEW_WEBAPI", "The new WebAPI address has been successfully saved!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_NEW_WEBAPI", "Die neue WebAPI-Adresse wurde erfolgreich gespeichert!");

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_NETWORK_NOT_AVAILABLE", "WIFI hálózat nem elérhető!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_NETWORK_NOT_AVAILABLE", "WIFI network is not available!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_NETWORK_NOT_AVAILABLE", "WIFI-Netz ist nicht verfügbar!");

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_DATA_RETIREVAL_START", "Az adatok lekérdezése a Web API-ról megkezdődött!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_DATA_RETIREVAL_START", "Data retrieval has started from the Web API!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_DATA_RETIREVAL_START", "Der Datenabruf von der Web-API hat begonnen!");

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_DATA_SAVE_SUCCES_END", "Az adatok mentése sikeresen megtörtént!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_DATA_SAVE_SUCCES_END", "The data has been successfully saved!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_DATA_SAVE_SUCCES_END", "Die Daten wurden erfolgreich gespeichert!");

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_DATA_SAVE_FAILED_END", "Az adatok mentése során hiba keletkezett!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_DATA_SAVE_FAILED_END", "An error occurred while saving the data!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_DATA_SAVE_FAILED_END", "Beim Speichern der Daten ist ein Fehler aufgetreten!");

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_BASE_WEBAPI_NOT_AVAILABLE", "Az alap WEB API nem elérhető!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_BASE_WEBAPI_NOT_AVAILABLE", "The basic Web API is available!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_BASE_WEBAPI_NOT_AVAILABLE", "Die grundlegende Web-API ist verfügbar!");

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_BASE_WEBAPI_NOT_EXIST", "Az eszköz nem létezik az adatbázisban!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_BASE_WEBAPI_NOT_EXIST", "The device does not exist in the database!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_BASE_WEBAPI_NOT_EXIST", "Das Gerät existiert nicht in der Datenbank!");

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_BASE_WEBAPI_INACTIVE_STATE", "Az eszköz jelenleg inaktív státuszban van!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_BASE_WEBAPI_INACTIVE_STATE", "The device is currently in inactive status!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_BASE_WEBAPI_INACTIVE_STATE", "Das Werkzeug ist derzeit inaktiv!");

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_BASE_WEBAPI_NOT_ALLOWED", "Az eszköz számára nincs engedélyezve a belépés!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_BASE_WEBAPI_NOT_ALLOWED", "The device is not allowed access!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_BASE_WEBAPI_NOT_ALLOWED", "Das Gerät ist nicht zugangsberechtigt!");

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_LOGIN_SUCCES", "Sikeres bejelentkezés!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_LOGIN_SUCCES", "Successful login!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_LOGIN_SUCCES", "Erfolgreiche Anmeldung!");

        mainPreferenceFileService.saveValueToHungaryPrefFile("HU$WC_BASE_WEBAPI_NO_HAS_APPLICATION", "Nincs engedélyezett alkalmazás az eszköz számára!");
        mainPreferenceFileService.saveValueToEnglishPrefFile("EN$WC_BASE_WEBAPI_NO_HAS_APPLICATION", "No applications are allowed for the device!");
        mainPreferenceFileService.saveValueToGermanPrefFile("DE$WC_BASE_WEBAPI_NO_HAS_APPLICATION", "Es sind keine Anwendungen für dieses Gerät erlaubt!");
    }

    public boolean createMainWebAPIService(String baseUrl){
        if(baseUrl == null) return false;

        boolean isCreated = false;

        mainWebAPIService = MainWebAPIService.getRetrofitInstance(baseUrl, this);
        if(mainWebAPIService != null) isCreated = true;

        return isCreated;
    }

    public void createLoginWebAPIService(String baseUrl){
        loginWebAPIService = LoginWebAPIService.getInstance(baseUrl, this);
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

    public void saveStringValueToSettinsPrefFile(String key, String value){
        if(key == null) return;
        if(value == null) return;
        if(mainPreferenceFileService == null) return;
        mainPreferenceFileService.saveValueToSettingsPrefFile(key, value);
    }

    public void saveIntValueToSettinsPrefFile(String key, int value){
        if(key == null) return;
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

    public void callMainWebAPI(){
        if(mainWebAPIService == null) return;
        if(mainPreferenceFileService == null) return;

        String serialNumber = AppDataManagerHelper.getDeviceSerialNumber();
        String deviceName = AppDataManagerHelper.getDeviceName(context);
        String serverName = mainPreferenceFileService.getStringValueFromSettingsPrefFile("ServerName");

        if(serialNumber != null && deviceName != null && serverName != null){
            mainWebAPIService.sendDeviceDetails("7a0e0865-08b2-488a-8a20-c327ce28e59d", serialNumber, deviceName, serverName);
        }
    }

    public void callSettingsWebAPI(int loginModeEnum, String identifier, String authenticationToken, String data, boolean createSession, int applicationId, int isFromLoginPage, String serverName){
        if(loginWebAPIService == null) return;
        if(identifier == null) return;
        if(authenticationToken == null) return;
        if(serverName == null) return;
        if(data == null) return;

        loginWebAPIService.sendLoginDetails(loginModeEnum, identifier, authenticationToken, data, createSession, applicationId, isFromLoginPage, serverName);
    }

    public void initLoginButtons(int loginModesNumber, WindowSizeTypes[] wsc){
        if(wsc == null) return;
        if(mCustomThreadPoolManager == null) return;

        try {
            CreateLoginButtons callable = new CreateLoginButtons(context.getApplicationContext(), loginModesNumber, wsc);
            callable.setCustomThreadPoolManager(mCustomThreadPoolManager);
            mCustomThreadPoolManager.addCallableMethod(callable);
        }
        catch (Exception e){
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    public void downloadLogoFromUrl(int applicationsSize) {
        if(mCustomThreadPoolManager == null) return;
        if(mainPreferenceFileService == null) return;

        try {
            DownloadPNGLogo callable = new DownloadPNGLogo(context.getApplicationContext(), mainPreferenceFileService, applicationsSize);
            callable.setCustomThreadPoolManager(mCustomThreadPoolManager);
            mCustomThreadPoolManager.addCallableMethod(callable);
        }
        catch (Exception e){
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    public String getMessageFromLanguagesFiles(String languageKey, String separator){
        if(languageKey == null) return null;

        String currentLanguage = getStringValueFromSettingsFile("CurrentSelectedLanguage");

        String result = null;
        if(currentLanguage != null){
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
        }

        if(result == null) return null;
        return result;
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* IAppDataManager interfész függvénye */

    @Override
    public void sendDatasToPresenter(List<ProgramsResultObject> programsResultObjectList) {
        if(programsResultObjectList == null) return;
        if(iProgramsPresenter == null) return;
        iProgramsPresenter.getDatasFromPresenter(programsResultObjectList);
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* IMainWebAPIService interfész függvénye */
    @Override
    public void onSuccesMainWebAPI(MainWebAPIResponseObject apiObj) {
        if(apiObj == null) return;
        if(iMainActivityPresenter == null ) return;
        if(mainPreferenceFileService == null) return;

        try {
            AppDataManagerHelper.saveIntValueToPrefFile(mainPreferenceFileService, "resultCode", apiObj.getResultCode());

            int resultCode = mainPreferenceFileService.getIntValueFromSettingsPrefFile("resultCode");
            Device device = apiObj.getDevice();
            if(device != null){

                if(resultCode == DeviceResponse.WEBAPI_ADDRESS_CHANGE) {
                    mainPreferenceFileService.saveValueToSettingsPrefFile("loginWebApiUrl", device.getDeviceId());

                    String loginWebApiUrl = mainPreferenceFileService.getStringValueFromSettingsPrefFile("loginWebApiUrl");
                    if(loginWebApiUrl != null && loginWebApiUrl.equals(device.getDeviceId())){
                        String message = getMessageFromLanguagesFiles( "WC_NEW_WEBAPI", "$");
                        if(message != null) iMainActivityPresenter.sendMessageToView(message);

                        iMainActivityPresenter.startProgram(4000);
                    }
                }
                else if(resultCode == DeviceResponse.OK){
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "deviceId", device.getDeviceId());
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "deviceName", device.getDeviceName());
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "active", device.getActive());
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "lastDeviceLoginDate", device.getLastDeviceLoginDate());
                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "comments", device.getComments());

                    List<Application> apps = device.getApplicationList();

                    if(apps != null) {

                        AppDataManagerHelper.saveIntValueToPrefFile(mainPreferenceFileService, "applicationsNumber", device.getApplicationList().size());

                        for (int i = 0; i < apps.size(); i++) {

                            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "applicationId" + '_' + (i + 1), apps.get(i).getId().toString());
                            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "applicationName" + '_' + (i + 1), apps.get(i).getApplicationName());
                            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "applicationTitle" + '_' + (i + 1), apps.get(i).getApplicationTitle());
                            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "applicationLead" + '_' + (i + 1), apps.get(i).getApplicationLead());
                            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "applicationDescription" + '_' + (i + 1), apps.get(i).getApplicationDescription());
                            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "applicationVersion" + '_' + (i + 1), apps.get(i).getApplicationVersion());
                            AppDataManagerHelper.saveIntValueToPrefFile(mainPreferenceFileService, "applicationEnabledLoginFlag" + '_' + (i + 1), apps.get(i).getApplicationEnabledLoginFlag());
                            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "loginUrl" + '_' + (i + 1), apps.get(i).getLoginUrl());
                            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "mainUrl" + '_' + (i + 1), apps.get(i).getMainUrl());
                            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "settingsUrl" + '_' + (i + 1), apps.get(i).getSettingsUrl());
                            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "helpUrl" + '_' + (i + 1), apps.get(i).getHelpUrl());
                            AppDataManagerHelper.saveIntValueToPrefFile(mainPreferenceFileService, "defaultThemeId" + '_' + (i + 1), apps.get(i).getDefaultThemeId());
                            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "logoUrl" + '_' + (i + 1), apps.get(i).getLogoUrl());

                            int currentSelectedThemeId = mainPreferenceFileService.getIntValueFromSettingsPrefFile("currentSelectedThemeId" + '_' + (i + 1));
                            if(currentSelectedThemeId == Integer.MAX_VALUE){
                                AppDataManagerHelper.saveIntValueToPrefFile(mainPreferenceFileService, "currentSelectedThemeId" + '_' + (i + 1), apps.get(i).getDefaultThemeId());
                            }

                            List<ApplicationTheme> appsThemes = apps.get(i).getApplicationThemeList();
                            if(appsThemes != null){

                                AppDataManagerHelper.saveIntValueToPrefFile(mainPreferenceFileService, "applicationThemesNumber", apps.get(i).getApplicationThemeList().size());

                                themes = new StringBuilder();

                                for (int l = 0; l < appsThemes.size(); l++) {

                                    int defaultThemeId = appsThemes.get(l).getId();

                                    if(l == 0) themes.append(defaultThemeId);
                                    else themes.append("_" + defaultThemeId);

                                    AppDataManagerHelper.saveIntValueToPrefFile(mainPreferenceFileService, "id" + '_' + (i + 1) + '_' + defaultThemeId, appsThemes.get(l).getId());
                                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "applicationId" + '_' + (i + 1) + '_' + defaultThemeId, appsThemes.get(l).getApplicationId().toString());
                                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "logoUrl" + '_' + (i + 1) + '_' + defaultThemeId, appsThemes.get(l).getLogoUrl());
                                    AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "name" + '_' + (i + 1) + '_' + defaultThemeId, appsThemes.get(l).getName());
                                    AppDataManagerHelper.saveColorToSettingsPrefFile(mainPreferenceFileService, "backgroundColor" + '_' + (i + 1) + '_' + defaultThemeId, appsThemes.get(l).getBackgroundColor());
                                    AppDataManagerHelper.saveColorToSettingsPrefFile(mainPreferenceFileService, "backgroundGradientColor" + '_' + (i + 1) + '_' + defaultThemeId, appsThemes.get(l).getBackgroundGradientColor());
                                    AppDataManagerHelper.saveColorToSettingsPrefFile(mainPreferenceFileService, "foregroundColor" + '_' + (i + 1) + '_' + defaultThemeId, appsThemes.get(l).getForegroundColor());
                                    AppDataManagerHelper.saveColorToSettingsPrefFile(mainPreferenceFileService, "buttonBackgroundColor" + '_' + (i + 1) + '_' + defaultThemeId, appsThemes.get(l).getButtonBackgroundColor());
                                    AppDataManagerHelper.saveColorToSettingsPrefFile(mainPreferenceFileService, "buttonBackgroundGradientColor" + '_' + (i + 1) + '_' + defaultThemeId, appsThemes.get(l).getButtonBackgroundGradientColor());
                                    AppDataManagerHelper.saveColorToSettingsPrefFile(mainPreferenceFileService, "buttonForegroundColor" + '_' + (i + 1) + '_' + defaultThemeId, appsThemes.get(l).getButtonForegroundColor());
                                    AppDataManagerHelper.saveColorToSettingsPrefFile(mainPreferenceFileService, "controlColor" + '_' + (i + 1) + '_' + defaultThemeId, appsThemes.get(l).getControlColor());
                                }

                                AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService, "themesIds" + '_' + (i + 1), themes.toString());
                            }
                        }

                        String message = getMessageFromLanguagesFiles( "WC_DATA_SAVE_SUCCES_END", "$");
                        if(message != null) iMainActivityPresenter.sendMessageToView(message);

                        iMainActivityPresenter.processResultMessage(resultCode);
                    }
                    else {
                        iMainActivityPresenter.processResultMessage(resultCode);
                    }
                }
            }
        }
        catch (Exception e){
            String message = e.getMessage();
            Log.e("AppDataManager_SAVE_FAILED", message);

            message = getMessageFromLanguagesFiles( "WC_DATA_SAVE_FAILED_END", "$");
            if(message != null ) iMainActivityPresenter.sendMessageToView(message);
        }
    }

    @Override
    public void onFailureMainWebAPI(String errorMessage) {
        if(iMainActivityPresenter == null) return;

        Log.e("onFailureMainWebAPI", errorMessage);

        String message = getMessageFromLanguagesFiles( "WC_BASE_WEBAPI_NOT_AVAILABLE", "$");
        if(message != null ) iMainActivityPresenter.sendMessageToView(message);
    }

    @Override
    public void onSuccesSettingsWebAPI(SettingsWebAPIResponseObject s, int applicationId, int isFromLoginPage) {
        if(iLoginFragmentsPresenter == null) return;
        if(mainPreferenceFileService == null) return;
        if(s == null) return;

        int responseCode = s.getLoginResponseCode();

        if(responseCode == LoginResponse.OK){
            AppDataManagerHelper.saveIntValueToPrefFile(mainPreferenceFileService,"loginResponseCode" + applicationId, s.getLoginResponseCode());
            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService,"userId" + applicationId, s.getUserId());
            AppDataManagerHelper.saveStringValueToPrefFile(mainPreferenceFileService,"sessionId" + applicationId, s.getSessionId());

            iLoginFragmentsPresenter.processResultMessage(responseCode, applicationId, isFromLoginPage);
        }
        else {
            iLoginFragmentsPresenter.processResultMessage(responseCode, applicationId, isFromLoginPage);
        }
    }

    @Override
    public void onFailureSettingsWebAPI() {
        if(iLoginActivityPresenter == null) return;
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* PresenterThreadCallback interfész függvénye */
    @Override
    public void sendMessageToHandler(Message message) {
        if(appDataManagerHandler == null) return;
        appDataManagerHandler.sendMessage(message);
    }

    @Override
    public void sendMessageToPresenter(String message) {
        if(message == null) return;
        if(iLoginActivityPresenter != null) iLoginActivityPresenter.getMessageFromAppDataManager(message);
    }

    @Override
    public void sendCreatedButtonsToPresenter(List<ImageButton> createdButtons) {
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
                    iAppDataManagerHandler.get().sendMessageToPresenter(getWeakReferenceNotification(msg));
                    break;
                }
                case MessageIdentifiers.BUTTONS_IS_CREATED:{
                    if(msg.obj instanceof List){
                        List<ImageButton> createdButtons = (List<ImageButton>) msg.obj;
                        iAppDataManagerHandler.get().sendCreatedButtonsToPresenter(createdButtons);
                    }
                    break;
                }
                case MessageIdentifiers.LOGO_DOWNLOAD_SUCCES:{
                    if(msg.obj instanceof List){
                        List<ProgramsResultObject> fileNames = (List<ProgramsResultObject>) msg.obj;
                        iAppDataManagerHandler.get().sendDatasToPresenter(fileNames);
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
