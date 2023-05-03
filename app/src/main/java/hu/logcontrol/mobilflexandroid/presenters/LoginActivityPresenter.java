package hu.logcontrol.mobilflexandroid.presenters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageButton;

import java.util.List;

import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.WebViewActivity;
import hu.logcontrol.mobilflexandroid.datamanager.AppDataManager;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;
import hu.logcontrol.mobilflexandroid.interfaces.ILoginActivity;
import hu.logcontrol.mobilflexandroid.interfaces.ILoginActivityPresenter;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;

public class LoginActivityPresenter implements ILoginActivityPresenter {

    private ILoginActivity iLoginActivity;
    private Context context;

    private AppDataManager appDataManager;

    public LoginActivityPresenter(ILoginActivity iLoginActivity, Context context) {
        this.iLoginActivity = iLoginActivity;
        this.context = context.getApplicationContext();
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* ILoginActivityPresenter interfész függvényei */

    @Override
    public void openActivityByEnum(ViewEnums viewEnum, int applicationId, int defaultTheme) {
        if(viewEnum == null) return;
        if(iLoginActivity == null) return;

        Intent intent = null;

        switch (viewEnum){
            case WEBVIEW_ACTIVITY:{
                intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("applicationId", applicationId);
                intent.putExtra("defaultTheme", defaultTheme);
                break;
            }
        }

        if(intent == null) return;
        if(iLoginActivity != null) iLoginActivity.openViewByIntent(intent);

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
    public void getMessageFromAppDataManager(String message) {
        if(message == null) return;
        if(iLoginActivity == null) return;
        iLoginActivity.getMessageFromPresenter(message);
    }

    @Override
    public void getCreatedButtonsFromAppDataManager(List<ImageButton> createdButtons) {
        if(createdButtons == null) return;
        if(iLoginActivity == null) return;
        iLoginActivity.sendCreatedButtonsToView(createdButtons);
    }

    @Override
    public void initButtonsByLoginModesNumber(WindowSizeTypes[] wsc, int applicationId, int defaultThemeId) {
        if(wsc == null) return;
        if(appDataManager == null) return;

        if(applicationId != -1 && defaultThemeId != -1){
            String color1 = appDataManager.getStringValueFromSettingsFile("backgroundColor" + '_' + applicationId + '_' + defaultThemeId);
            String color2 = appDataManager.getStringValueFromSettingsFile("backgroundGradientColor" + '_' + applicationId + '_' + defaultThemeId);

            try {
                if(color1 != null && color2 != null){
                    int[] colors = new int[] {
                            Color.parseColor(appDataManager.getStringValueFromSettingsFile("backgroundColor" + '_' + applicationId + '_' + defaultThemeId)),
                            Color.parseColor(appDataManager.getStringValueFromSettingsFile("backgroundGradientColor" + '_' + applicationId + '_' + defaultThemeId))
                    };

                    int loginModesNumber = appDataManager.getIntValueFromSettingsFile("applicationEnabledLoginFlag" + '_' + applicationId);
                    if(loginModesNumber != 0){
                        appDataManager.initLoginButtons(loginModesNumber, wsc, colors);
                    }
                    else {
                        openActivityByEnum(ViewEnums.WEBVIEW_ACTIVITY, applicationId, defaultThemeId);
                    }
                }
            }
            catch (Exception e){
                ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
            }
        }
    }

    @Override
    public void setControlsValuesBySettings(int applicationId, int defaultThemeId) {
        if(iLoginActivity == null) return;
        if(appDataManager == null) return;

        if(applicationId != -1 && defaultThemeId != -1){

            String message = "Bejelentkezés";
            String applicationLead = appDataManager.getStringValueFromSettingsFile("applicationLead" + '_' + applicationId);
            String applicationName = appDataManager.getStringValueFromSettingsFile("applicationTitle" + '_' + applicationId);

            String backgroundColor = appDataManager.getStringValueFromSettingsFile("backgroundColor" + '_' + applicationId + '_' + defaultThemeId);
            String backgroundGradientColor = appDataManager.getStringValueFromSettingsFile("backgroundGradientColor" + '_' + applicationId + '_' + defaultThemeId);
            String foreGroundColor = appDataManager.getStringValueFromSettingsFile("foregroundColor" + '_' + applicationId + '_' + defaultThemeId);

            // TODO ezt majd le kell tölteni egy URL-ről
//            iLoginActivity.changeStateLoginLogo(R.drawable.ic_mobileflex);

            if(message != null && foreGroundColor != null) iLoginActivity.changeStateLoginTV(message, foreGroundColor);
            if(backgroundColor != null && backgroundGradientColor != null) iLoginActivity.changeStateMainActivityCL(backgroundColor, backgroundGradientColor);
            if(applicationLead != null && foreGroundColor != null) iLoginActivity.changeStateApplicationLeadTextbox(applicationLead, foreGroundColor);
            if(applicationName != null && foreGroundColor != null) iLoginActivity.changeStateApplicationTitleTextbox(applicationName, foreGroundColor);
            if(backgroundColor != null) iLoginActivity.changeMobileBarsColors(backgroundColor, backgroundGradientColor);
        }
    }
}
