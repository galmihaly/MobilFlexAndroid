package hu.logcontrol.mobilflexandroid.presenters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import hu.logcontrol.mobilflexandroid.LoginActivity;
import hu.logcontrol.mobilflexandroid.ProgramsActivity;
import hu.logcontrol.mobilflexandroid.WebViewActivity;
import hu.logcontrol.mobilflexandroid.adapters.LanguagesSpinnerAdapter;
import hu.logcontrol.mobilflexandroid.datamanager.AppDataManager;
import hu.logcontrol.mobilflexandroid.enums.DeviceResponse;
import hu.logcontrol.mobilflexandroid.enums.RepositoryType;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.helpers.Helper;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivity;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivityPresenter;

public class MainActivityPresenter implements IMainActivityPresenter {

    private IMainActivity iMainActivity;
    private Context context;

    private int[] languagesImages;
    private AppDataManager appDataManager;
    private Handler handler;

    public MainActivityPresenter(IMainActivity iMainActivity, Context context, Looper handler) {
        this.iMainActivity = iMainActivity;
        this.context = context.getApplicationContext();
        this.handler = new Handler(handler);
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* ILoginActivityPresenter interfész függvényei */

    @Override
    public void openActivityByEnum(ViewEnums viewEnum, int applicationNumber, int applicationId, int isFromLoginPage) {
        if(viewEnum == null) return;
        if(iMainActivity == null) return;
        if(appDataManager == null) return;

        Intent intent = null;

        switch (viewEnum){
            case LOGIN_ACTIVITY:{

                intent = new Intent(context, LoginActivity.class);
                intent.putExtra("applicationId", applicationId);
                intent.putExtra("isFromLoginPage", isFromLoginPage);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            }
            case PROGRAMS_ACTIVITY:{
                intent = new Intent(context, ProgramsActivity.class);
                intent.putExtra("applicationsSize", applicationNumber);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            }
            case WEBVIEW_ACTIVITY:{
                intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("applicationsSize", applicationNumber);
                intent.putExtra("applicationId", applicationId);
                intent.putExtra("isFromLoginPage", isFromLoginPage);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            }
        }

        if(intent == null) return;
        if(iMainActivity != null) iMainActivity.openViewByIntent(intent);
    }

    @Override
    public LanguagesSpinnerAdapter getSpinnerAdapter() {
        if(appDataManager == null) return null;
        languagesImages = appDataManager.getLanguagesFlags();

        LanguagesSpinnerAdapter adapter = null;
        if(languagesImages != null) adapter = new LanguagesSpinnerAdapter(context, languagesImages);

        if(adapter == null) return null;
        return adapter;
    }

    @Override
    public void translateTextBySelectedLanguage(String languageID) {
        if(appDataManager == null) return;
        if(iMainActivity == null) return;

        String m;

        switch (languageID){
            case "HU":{
                m = appDataManager.getMessageText(RepositoryType.HUNGARY_PREFERENCES_FILE, "HU$WC_MessageTextView");
                if(m != null) iMainActivity.setTextToMessageTV(m);
                break;
            }
            case "EN":{
                m = appDataManager.getMessageText(RepositoryType.ENGLISH_PREFERENCES_FILE, "EN$WC_MessageTextView");
                if(m != null) iMainActivity.setTextToMessageTV(m);
                break;
            }
            case "DE":{
                m = appDataManager.getMessageText(RepositoryType.GERMAN_PREFERENCES_FILE, "DE$WC_MessageTextView");
                if(m != null) iMainActivity.setTextToMessageTV(m);
                break;
            }
        }
    }

    @Override
    public void saveLanguageToSettingsFile(String languageID) {
        if(appDataManager == null) return;
        appDataManager.saveStringValueToSettinsPrefFile("CurrentSelectedLanguage", languageID);
    }

    @Override
    public int getCurrentLanguageFromSettingsFile() {
        if(appDataManager == null) return -1;

        int preferenceLanguage = appDataManager.getLanguageIDFromPrefFile();

        if(preferenceLanguage == -1) return -1;
        return preferenceLanguage;
    }

    @Override
    public void initAppDataManager() {
        appDataManager = new AppDataManager(context, this);
        appDataManager.createPreferenceFileService();
        appDataManager.initLanguagesPrefFile();
        appDataManager.initSettingsPrefFile();
        appDataManager.initBaseMessagesPrefFile();
    }

    @Override
    public void startProgram(int delay) {
        if(appDataManager == null) return;
        if(iMainActivity == null) return;
        if(handler == null) return;

        handler.postDelayed(() -> {
            String loginWebApiUrl = appDataManager.getStringValueFromSettingsFile("loginWebApiUrl");
            if(loginWebApiUrl != null && !loginWebApiUrl.equals("")){

                boolean isCreated = appDataManager.createMainWebAPIService(loginWebApiUrl);
                if(isCreated) initCallingWebAPI();
            }
            else {
                boolean isSaved = saveBaseUrl();
                if(isSaved) {

                    loginWebApiUrl = appDataManager.getStringValueFromSettingsFile("loginWebApiUrl");
                    boolean isCreated = appDataManager.createMainWebAPIService(loginWebApiUrl);
                    if(isCreated) initCallingWebAPI();
                }
            }
        }, delay);
    }

    private void initCallingWebAPI(){

        String message = null;

        boolean isNetWokAvailable = Helper.isInternetConnection(context);
        if(isNetWokAvailable){
            appDataManager.callMainWebAPI();

            message = appDataManager.getMessageFromLanguagesFiles("WC_DATA_RETIREVAL_START", "$");
            sendMessageToView(message);
        }
        else {
            message = appDataManager.getMessageFromLanguagesFiles("WC_DATA_RETIREVAL_START", "$");
            sendMessageToView(message);

            int applicationNumber = appDataManager.getIntValueFromSettingsFile("applicationsNumber");
            if(applicationNumber == 1){

                int applicationId = 1;
                int currentSelectedThemeId = appDataManager.getIntValueFromSettingsFile("currentSelectedThemeId" + '_' + applicationId);
                int applicationEnabledLoginFlag = appDataManager.getIntValueFromSettingsFile("applicationEnabledLoginFlag" + '_' + applicationId);

                if(applicationEnabledLoginFlag == 0){
                    openActivityByEnum(ViewEnums.WEBVIEW_ACTIVITY, applicationNumber, applicationId, 0);
                }
                else {
                    openActivityByEnum(ViewEnums.LOGIN_ACTIVITY, applicationNumber, applicationId, 1);
                }
            }
            else {
                iMainActivity.setProgressRingVisibility(View.INVISIBLE);
                openActivityByEnum(ViewEnums.PROGRAMS_ACTIVITY, applicationNumber, -1, 2);
            }
        }
    }

    @Override
    public void sendMessageToView(String message){
        if(message == null) return;
        if(iMainActivity == null) return;

        iMainActivity.setTextToMessageTV(message);
    }

    @Override
    public void processResultMessage(int resultCode) {
        if(iMainActivity == null) return;
        if(appDataManager == null) return;

        String message = null;

        if(resultCode == DeviceResponse.OK) {

            int applicationNumber = appDataManager.getIntValueFromSettingsFile("applicationsNumber");
            if(applicationNumber == 1){

                int applicationId = 1;
                int currentSelectedThemeId = appDataManager.getIntValueFromSettingsFile("currentSelectedThemeId" + '_' + applicationId);
                int applicationEnabledLoginFlag = appDataManager.getIntValueFromSettingsFile("applicationEnabledLoginFlag" + '_' + applicationId);

                if(applicationEnabledLoginFlag == 0){
                    openActivityByEnum(ViewEnums.WEBVIEW_ACTIVITY, applicationNumber, applicationId, 0);
                }
                else if(applicationEnabledLoginFlag > 0) {
                    openActivityByEnum(ViewEnums.LOGIN_ACTIVITY, applicationNumber, applicationId, 1);
                }
            }
            else if(applicationNumber > 1){
                iMainActivity.setProgressRingVisibility(View.INVISIBLE);
                openActivityByEnum(ViewEnums.PROGRAMS_ACTIVITY, applicationNumber, -1, 2);
            }
            else {
                message = appDataManager.getMessageFromLanguagesFiles("WC_BASE_WEBAPI_NO_HAS_APPLICATION", "$");
                sendMessageToView(message);
            }
        }
        else if(resultCode == DeviceResponse.DeviceDoesNotExists) {
            message = appDataManager.getMessageFromLanguagesFiles("WC_BASE_WEBAPI_NOT_EXIST", "$");
            sendMessageToView(message);
        }
        else if(resultCode == DeviceResponse.DeviceInactive) {
            message = appDataManager.getMessageFromLanguagesFiles("WC_BASE_WEBAPI_INACTIVE_STATE", "$");
            sendMessageToView(message);
        }
        else if(resultCode == DeviceResponse.DeviceAccessDenied) {
            message = appDataManager.getMessageFromLanguagesFiles("WC_BASE_WEBAPI_NOT_ALLOWED", "$");
            sendMessageToView(message);
        }
    }

    public boolean saveBaseUrl() {
        boolean isSaved = false;

        if(iMainActivity != null && appDataManager != null){
            Uri uri = Uri.parse("https://api.mobileflex.hu/device");

            String loginWebApiUrl = appDataManager.getStringValueFromSettingsFile("loginWebApiUrl");
            String serverName = appDataManager.getStringValueFromSettingsFile("ServerName");

            if(loginWebApiUrl == null && serverName == null){
                appDataManager.saveStringValueToSettinsPrefFile("loginWebApiUrl", "https://" + uri.getHost() +"/");
                appDataManager.saveStringValueToSettinsPrefFile("ServerName", uri.getPathSegments().get(0));

                loginWebApiUrl = appDataManager.getStringValueFromSettingsFile("loginWebApiUrl");
                serverName = appDataManager.getStringValueFromSettingsFile("ServerName");

                if(loginWebApiUrl != null && serverName != null){
                    isSaved = true;
                }
            }
        }

        return isSaved;
    }
}
