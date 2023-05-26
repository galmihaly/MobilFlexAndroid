package hu.logcontrol.mobilflexandroid.presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hu.logcontrol.mobilflexandroid.LoginActivity;
import hu.logcontrol.mobilflexandroid.MainActivity;
import hu.logcontrol.mobilflexandroid.ProgramsActivity;
import hu.logcontrol.mobilflexandroid.adapters.ThemesSpinnerAdapter;
import hu.logcontrol.mobilflexandroid.datamanager.AppDataManager;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.helpers.Helper;
import hu.logcontrol.mobilflexandroid.interfaces.IWebViewActivity;
import hu.logcontrol.mobilflexandroid.interfaces.IWebViewActivityPresenter;

public class WebViewActivityPresenter implements IWebViewActivityPresenter {

    private IWebViewActivity iWebViewActivity;
    private Context context;

    private AppDataManager appDataManager;

    public WebViewActivityPresenter(IWebViewActivity iWebViewActivity, Context context) {
        this.iWebViewActivity = iWebViewActivity;
        this.context = context.getApplicationContext();
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* IWebViewActivityPresenter interfész függvényei */


    @Override
    public void openActivityByEnum(ViewEnums viewEnum, int applicationId, int applicationsSize) {
        if(viewEnum == null) return;
        if(iWebViewActivity == null) return;

        Intent intent = null;

        switch (viewEnum){
            case LOGIN_ACTIVITY:{
                intent = new Intent(context, LoginActivity.class);
                intent.putExtra("applicationId", applicationId);
                break;
            }
            case PROGRAMS_ACTIVITY:{
                intent = new Intent(context, ProgramsActivity.class);
                intent.putExtra("applicationsSize", applicationsSize);
                break;
            }
            case MAIN_ACTIVITY:{
                intent = new Intent(context, MainActivity.class);
                break;
            }
        }

        if(intent == null) return;
        if(iWebViewActivity != null) iWebViewActivity.openViewByIntent(intent);
    }

    @Override
    public void initAppDataManager() {
        appDataManager = new AppDataManager(context, this);
        appDataManager.createPreferenceFileService();
        appDataManager.initLanguagesPrefFile();
        appDataManager.initSettingsPrefFile();
        appDataManager.initBaseMessagesPrefFile();
        appDataManager.initTaskManager();
    }

    @Override
    public void initWebAPIServices() {
        if(appDataManager == null) return;
        String baseUrl = appDataManager.getStringValueFromSettingsFile("loginWebApiUrl");
        if(baseUrl != null){
            appDataManager.createMainWebAPIService(baseUrl);
        }
    }

    @Override
    public void getValuesFromSettingsPrefFile(int applicationId) {
        if(appDataManager == null) return;
        if(iWebViewActivity == null) return;

        if(applicationId != -1){

            int currentSelectedThemeId = appDataManager.getIntValueFromSettingsFile("currentSelectedThemeId" + '_' + applicationId);

            String appBarBackgroundColor = appDataManager.getStringValueFromSettingsFile("backgroundColor" + '_' + applicationId + '_' + currentSelectedThemeId);
            String appBarBackgroundGradientColor = appDataManager.getStringValueFromSettingsFile("backgroundGradientColor" + '_' + applicationId + '_' + currentSelectedThemeId);

            if(appBarBackgroundColor != null && appBarBackgroundGradientColor != null) iWebViewActivity.changeStateAppbarLayout(appBarBackgroundColor, appBarBackgroundGradientColor);
            if(appBarBackgroundColor != null && appBarBackgroundGradientColor != null) iWebViewActivity.changeMobileBarsColors(appBarBackgroundColor, appBarBackgroundGradientColor);

            String buttonBackgroundColor = appDataManager.getStringValueFromSettingsFile("buttonBackgroundColor" + '_' + applicationId + '_' + currentSelectedThemeId);
            String buttonBackgroundGradientColor = appDataManager.getStringValueFromSettingsFile("buttonBackgroundGradientColor" + '_' + applicationId + '_' + currentSelectedThemeId);

            if(buttonBackgroundColor != null && buttonBackgroundGradientColor != null) {
                iWebViewActivity.changeStateLoginButton( buttonBackgroundColor, buttonBackgroundGradientColor);
            }
        }
    }

    @Override
    public void getThemesSpinnerValues(int applicationId) {
        if(appDataManager == null) return;
        if(iWebViewActivity == null) return;

        HashMap<Integer, Integer> vaulePairs = new HashMap<>();

        String t = appDataManager.getStringValueFromSettingsFile("themesIds" + '_' + applicationId);
        List<Integer> themesIds = Helper.splitStringToIntList(t, "_");

        int currentSelectedThemeId = appDataManager.getIntValueFromSettingsFile("currentSelectedThemeId" + '_' + applicationId);

        if(themesIds != null){
            if(themesIds.size() > 1){
                List<String> themes = new ArrayList<>();

                for (int i = 0; i < themesIds.size(); i++) {
                    themes.add(appDataManager.getStringValueFromSettingsFile("name" + '_' + applicationId + '_' + themesIds.get(i)));
                    vaulePairs.put(i, themesIds.get(i));
                }

                ThemesSpinnerAdapter adapter = null;
                if(themesIds != null) adapter = new ThemesSpinnerAdapter(context, themes);

                int currentThemeId = -1;

                for (int i = 0; i < themesIds.size(); i++) {
                    if(themesIds.get(i) == currentSelectedThemeId) currentThemeId = i;
                }

                iWebViewActivity.initThemesSpinner(adapter, currentThemeId, vaulePairs);
            }
            else {
                iWebViewActivity.hideThemesSpinner();
            }
        }
    }

    @Override
    public void saveValueToCurrentThemeId(int applicationId, int themesId) {
        if(appDataManager == null) return;

        appDataManager.saveIntValueToSettinsPrefFile("currentSelectedThemeId" + '_' + applicationId, themesId);
        getValuesFromSettingsPrefFile(applicationId);
    }

    @Override
    public void getURLfromSettings(int applicationId) {
        if(appDataManager == null) return;

        if(applicationId != -1){
            String loginWebApiUrl = appDataManager.getStringValueFromSettingsFile("mainUrl" + '_' + applicationId);
            Log.e("loginWebApiUrl", loginWebApiUrl);
            if(loginWebApiUrl != null) {
                if(loginWebApiUrl.contains("https://")) {
                    iWebViewActivity.loadLoginWebAPIUrl(loginWebApiUrl);
                }
                else {
                    iWebViewActivity.loadLoginWebAPIUrl("https://" + loginWebApiUrl);
                }
            }
        }
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
}
