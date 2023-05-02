package hu.logcontrol.mobilflexandroid.presenters;

import android.content.Context;
import android.content.Intent;

import hu.logcontrol.mobilflexandroid.LoginActivity;
import hu.logcontrol.mobilflexandroid.ProgramsActivity;
import hu.logcontrol.mobilflexandroid.adapters.LanguagesSpinnerAdapter;
import hu.logcontrol.mobilflexandroid.datamanager.AppDataManager;
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

    public MainActivityPresenter(IMainActivity iMainActivity, Context context) {
        this.iMainActivity = iMainActivity;
        this.context = context.getApplicationContext();
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* ILoginActivityPresenter interfész függvényei */

    @Override
    public void openActivityByEnum(ViewEnums viewEnum, int applicationNumber) {
        if(viewEnum == null) return;
        if(iMainActivity == null) return;
        if(appDataManager == null) return;

        Intent intent = null;

        switch (viewEnum){
            case LOGIN_ACTIVITY:{
                intent = new Intent(context, LoginActivity.class);
                intent.putExtra("applicationNumber", applicationNumber);
                break;
            }
            case PROGRAMS_ACTIVITY:{
                intent = new Intent(context, ProgramsActivity.class);
                intent.putExtra("applicationNumber", applicationNumber);
                break;
            }
//            case SETTINSG_ACTIVITY:{
//                intent = new Intent(context, ProgramsActivity.class);
//                intent.putExtra("applicationsSize", applicationNumber);
//                break;
//            }
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
        appDataManager.saveValueToSettinsPrefFile("CurrentSelectedLanguage", languageID);
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
        appDataManager.createMainWebAPIService();
        appDataManager.initLanguagesPrefFile();
        appDataManager.initSettingsPrefFile();
        appDataManager.initBaseMessagesPrefFile();
    }

    @Override
    public void startProgram() {
        if(appDataManager == null) return;
        if(iMainActivity == null) return;

        String loginWebApiUrl = appDataManager.getStringValueFromSettingsFile("loginWebApiUrl");
        if(loginWebApiUrl != null){
            initCallingWebAPI();
        }
        else{
            appDataManager.saveValueToSettinsPrefFile("loginWebApiUrl", "https://api.mobileflex.hu/");

            loginWebApiUrl = appDataManager.getStringValueFromSettingsFile("loginWebApiUrl");
            if(loginWebApiUrl != null){
                initCallingWebAPI();
            }
        }
    }

    private void initCallingWebAPI(){

        String message = null;
        boolean isNetWokAvailable;

        isNetWokAvailable = Helper.isInternetConnection(context);
        if(isNetWokAvailable){
            message = appDataManager.getMessageFromLanguagesFiles("WC_NETWORK_AVAILABLE", "$");
            if(message != null) iMainActivity.setTextToMessageTV(message);

            message = appDataManager.getMessageFromLanguagesFiles("WC_DATA_RETIREVAL_START", "$");
            if(message != null) iMainActivity.setTextToMessageTV(message);
            appDataManager.callSettingsWebAPI();
        }
        else {
            message = appDataManager.getMessageFromLanguagesFiles("WC_NETWORK_NOT_AVAILABLE", "$");
            if(message != null) iMainActivity.setTextToMessageTV(message);
        }
    }

    @Override
    public void sendBitmapLogoToPresenter(String resultMessage) {
        if(resultMessage == null) return;
        if(iMainActivity == null) return;

        iMainActivity.setTextToMessageTV(resultMessage);

        int resultCode = appDataManager.getIntValueFromSettingsFile("resultCode");
        if(resultCode == -99) {
            openActivityByEnum(ViewEnums.SETTINSG_ACTIVITY, -1);
        }
        else {

            int applicationNumber = appDataManager.getIntValueFromSettingsFile("applicationsNumber");
            if(applicationNumber == 1){
//                openActivityByEnum(ViewEnums.LOGIN_ACTIVITY, 1);
                openActivityByEnum(ViewEnums.PROGRAMS_ACTIVITY, applicationNumber);
            }
            else {
                openActivityByEnum(ViewEnums.PROGRAMS_ACTIVITY, applicationNumber);
            }
        }
    }
}
