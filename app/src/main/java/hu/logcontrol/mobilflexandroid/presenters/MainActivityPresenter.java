package hu.logcontrol.mobilflexandroid.presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import hu.logcontrol.mobilflexandroid.LoginActivity;
import hu.logcontrol.mobilflexandroid.adapters.LanguagesSpinnerAdapter;
import hu.logcontrol.mobilflexandroid.datamanager.AppDataManager;
import hu.logcontrol.mobilflexandroid.enums.RepositoryType;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivity;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivityPresenter;
import hu.logcontrol.mobilflexandroid.models.SettingsObject;

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
    public void openActivityByEnum(ViewEnums viewEnum) {
        if(viewEnum == null) return;
        if(iMainActivity == null) return;
        if(appDataManager == null) return;

        Intent intent = null;

        switch (viewEnum){
            case LOGIN_ACTIVITY:{
                intent = new Intent(context, LoginActivity.class);

                SettingsObject settingsObject = appDataManager.getBaseSettingsObjectFromPrefFile();
                if(settingsObject != null) intent.putExtra("settingsObject", settingsObject);

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
        appDataManager.saveLanguageIDToPrefFile("CurrentSelectedLanguage", languageID);
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
        appDataManager.initTaskManager();
    }

    @Override
    public void startProgram() {
        if(appDataManager == null) return;

        String loginWebApiUrl = appDataManager.getValueFromSettingsFile("loginWebApiUrl");

        if(loginWebApiUrl != null){
            appDataManager.loadWebAPICallingTask();
        }
    }

    @Override
    public void sendResultToPresenter(String resultMessage) {
        if(resultMessage == null) return;
        Log.e("sendResultFromWebAPICallingTask", resultMessage);
    }
}
