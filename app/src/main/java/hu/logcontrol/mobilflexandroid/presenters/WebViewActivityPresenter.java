package hu.logcontrol.mobilflexandroid.presenters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.lang.ref.WeakReference;

import hu.logcontrol.mobilflexandroid.LoginActivity;
import hu.logcontrol.mobilflexandroid.datamanager.AppDataManager;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.interfaces.IWebViewActivity;
import hu.logcontrol.mobilflexandroid.interfaces.IWebViewActivityPresenter;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.models.LocalEncryptedPreferences;
import hu.logcontrol.mobilflexandroid.taskmanager.CustomThreadPoolManager;
import hu.logcontrol.mobilflexandroid.taskmanager.PresenterThreadCallback;

public class WebViewActivityPresenter implements IWebViewActivityPresenter {

    private IWebViewActivity webViewActivity;
    private Context context;

    private AppDataManager appDataManager;

    public WebViewActivityPresenter(IWebViewActivity webViewActivity, Context context) {
        this.webViewActivity = webViewActivity;
        this.context = context.getApplicationContext();
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* IWebViewActivityPresenter interfész függvényei */


    @Override
    public void openActivityByEnum(ViewEnums viewEnum, Intent backActivityIntent) {
        if(viewEnum == null) return;
        if(backActivityIntent == null) return;
        if(webViewActivity == null) return;

        String defaultThemeId = String.valueOf(backActivityIntent.getIntExtra("defaultThemeId", -1));
        String applicationId = String.valueOf(backActivityIntent.getIntExtra("applicationId", -1));

        Intent intent = null;

        switch (viewEnum){
            case LOGIN_ACTIVITY:{
                intent = new Intent(context, LoginActivity.class);
                intent.putExtra("defaultThemeId", defaultThemeId);
                intent.putExtra("applicationId", applicationId);
                break;
            }
        }

        if(intent == null) return;
        if(webViewActivity != null) webViewActivity.openViewByIntent(intent);
    }

    @Override
    public void initAppDataManager() {
        appDataManager = new AppDataManager(context, this);
        appDataManager.createPreferenceFileService();
        appDataManager.createMainWebAPIService();
        appDataManager.initLanguagesPrefFile();
        appDataManager.initSettingsPrefFile();
        appDataManager.initBaseMessagesPrefFile();
        appDataManager.initTaskManager();
    }

    @Override
    public void getValuesFromSettingsPrefFile(Intent intent) {
        if(appDataManager == null) return;
        if(webViewActivity == null) return;
        if(intent == null) return;

        String defaultThemeId = String.valueOf(intent.getIntExtra("defaultThemeId", -1));
        String applicationId = String.valueOf(intent.getIntExtra("applicationId", -1));

        String appBarBackgroundColor = appDataManager.getStringValueFromSettingsFile("backgroundColor" + '_' + applicationId + '_' + defaultThemeId);
        String appBarBackgroundGradientColor = appDataManager.getStringValueFromSettingsFile("backgroundGradientColor" + '_' + applicationId + '_' + defaultThemeId);

        if(appBarBackgroundColor != null && appBarBackgroundGradientColor != null) webViewActivity.changeStateAppbarLayout(appBarBackgroundColor, appBarBackgroundGradientColor);
        if(appBarBackgroundColor != null && appBarBackgroundGradientColor != null) webViewActivity.changeMobileBarsColors(appBarBackgroundColor, appBarBackgroundGradientColor);

        String buttonBackgroundColor = appDataManager.getStringValueFromSettingsFile("buttonBackgroundColor" + '_' + applicationId + '_' + defaultThemeId);
        String buttonBackgroundGradientColor = appDataManager.getStringValueFromSettingsFile("buttonBackgroundGradientColor" + '_' + applicationId + '_' + defaultThemeId);

        if(buttonBackgroundColor != null && buttonBackgroundGradientColor != null) {
            webViewActivity.changeStateLoginButton( buttonBackgroundColor, buttonBackgroundGradientColor);
        }
    }

    @Override
    public void getURLfromSettings(Intent intent) {
        if(appDataManager == null) return;
        if(intent == null) return;

        String applicationId = String.valueOf(intent.getIntExtra("applicationId", -1));
        Log.e("applicationId_applicationId", applicationId);

        String loginWebApiUrl = appDataManager.getStringValueFromSettingsFile("mainUrl" + '_' + applicationId);
        Log.e("loginWebApiUrl", loginWebApiUrl);
        if(loginWebApiUrl != null) webViewActivity.loadLoginWebAPIUrl(loginWebApiUrl);
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
}
