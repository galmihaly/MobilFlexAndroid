package hu.logcontrol.mobilflexandroid.presenters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import hu.logcontrol.mobilflexandroid.ProgramsActivity;
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
    public void openActivityByEnum(ViewEnums viewEnum, int applicationId) {
        if(viewEnum == null) return;
        if(iLoginActivity == null) return;
        if(appDataManager == null) return;

        Intent intent = null;

        switch (viewEnum){
            case WEBVIEW_ACTIVITY:{

                int applicationsSize = appDataManager.getIntValueFromSettingsFile("applicationsSize" + '_' + applicationId);
                int isFromLoginPage = appDataManager.getIntValueFromSettingsFile("applicationEnabledLoginFlag" + '_' + applicationId);

                intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("applicationId", applicationId);
                intent.putExtra("isFromLoginPage", isFromLoginPage);
                intent.putExtra("applicationsSize", applicationsSize);
                break;
            }
            case PROGRAMS_ACTIVITY:{

                int applicationNumber = appDataManager.getIntValueFromSettingsFile("applicationsNumber");

                intent = new Intent(context, ProgramsActivity.class);
                intent.putExtra("applicationsSize", applicationNumber);
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
    public GradientDrawable[] getCurrentThemeForButton(int applicationId) {
        if(appDataManager == null) return null;

        int currentSelectedThemeId = appDataManager.getIntValueFromSettingsFile("currentSelectedThemeId" + '_' + applicationId);

        String startedGradientColor = appDataManager.getStringValueFromSettingsFile("backgroundColor" + '_' + applicationId + '_' + currentSelectedThemeId);
        String endedGradientColor = appDataManager.getStringValueFromSettingsFile("backgroundGradientColor" + '_' + applicationId + '_' + currentSelectedThemeId);

        GradientDrawable[] g = new GradientDrawable[2];

        int[] colors = {Color.parseColor(startedGradientColor),Color.parseColor(endedGradientColor)};
        g[0] = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        g[0].setCornerRadius(60);

        g[1] = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        g[1].setCornerRadius(60);
        g[1].setAlpha(80);

        if(g == null) return null;
        return g;
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
    public void initButtonsByLoginModesNumber(WindowSizeTypes[] wsc, int applicationId) {
        if(wsc == null) return;
        if(appDataManager == null) return;

        try {
            if(applicationId != -1){

                int loginModesNumber = appDataManager.getIntValueFromSettingsFile("applicationEnabledLoginFlag" + '_' + applicationId);
                if(loginModesNumber != 0){
                    appDataManager.initLoginButtons(loginModesNumber, wsc);
                }
                else {
                    openActivityByEnum(ViewEnums.WEBVIEW_ACTIVITY, applicationId);
                }
            }
        }
        catch (Exception e){
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    @Override
    public void setControlsValuesBySettings(int applicationId) {
        if(iLoginActivity == null) return;
        if(appDataManager == null) return;

        if(applicationId != -1){

            int currentSelectedThemeId = appDataManager.getIntValueFromSettingsFile("currentSelectedThemeId" + '_' + applicationId);

            String message = "Bejelentkezés";
            String applicationLead = appDataManager.getStringValueFromSettingsFile("applicationLead" + '_' + applicationId);
            String applicationName = appDataManager.getStringValueFromSettingsFile("applicationTitle" + '_' + applicationId);

            String backgroundColor = appDataManager.getStringValueFromSettingsFile("backgroundColor" + '_' + applicationId + '_' + currentSelectedThemeId);
            String backgroundGradientColor = appDataManager.getStringValueFromSettingsFile("backgroundGradientColor" + '_' + applicationId + '_' + currentSelectedThemeId);
            String foreGroundColor = appDataManager.getStringValueFromSettingsFile("foregroundColor" + '_' + applicationId + '_' + currentSelectedThemeId);

            String buttonBackgroundColor = appDataManager.getStringValueFromSettingsFile("buttonBackgroundColor" + '_' + applicationId + '_' + currentSelectedThemeId);
            String buttonBackgroundGradientColor = appDataManager.getStringValueFromSettingsFile("buttonBackgroundGradientColor" + '_' + applicationId + '_' + currentSelectedThemeId);

            if(message != null && foreGroundColor != null) iLoginActivity.changeStateLoginTV(message, foreGroundColor);
            if(backgroundColor != null && backgroundGradientColor != null) iLoginActivity.changeStateMainActivityCL(backgroundColor, backgroundGradientColor);
            if(applicationLead != null && foreGroundColor != null) iLoginActivity.changeStateApplicationLeadTextbox(applicationLead, foreGroundColor);
            if(applicationName != null && foreGroundColor != null) iLoginActivity.changeStateApplicationTitleTextbox(applicationName, foreGroundColor);
            if(backgroundColor != null && backgroundGradientColor != null) iLoginActivity.changeMobileBarsColors(backgroundColor, backgroundGradientColor);
            if(buttonBackgroundColor != null && buttonBackgroundGradientColor != null) iLoginActivity.changeBackButtonColors(buttonBackgroundColor, buttonBackgroundGradientColor);
        }
    }

    @Override
    public void getLogoImageFromExternalStorage(int applicationId) {
        if(iLoginActivity == null) return;
        if(appDataManager == null) return;

        int currentSelectedThemeId = appDataManager.getIntValueFromSettingsFile("currentSelectedThemeId" + '_' + applicationId);

        Bitmap bitmap = null;
        String fileName = appDataManager.getStringValueFromSettingsFile("logoName" + '_' + applicationId + '_' + currentSelectedThemeId);
        String directionName = Environment.getExternalStorageDirectory() + File.separator + "MobileFlexAndroid";

        if(fileName != null){
            File dir = new File(directionName);
            if(dir.exists()){
                File file = new File(directionName + File.separator + fileName);

                if(file.exists()){
                    bitmap = BitmapFactory.decodeFile(directionName + File.separator + fileName);
                }
            }
        }

        if(bitmap != null) iLoginActivity.getLogoFromPresenter(bitmap);
    }
}
