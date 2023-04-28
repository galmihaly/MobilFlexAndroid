package hu.logcontrol.mobilflexandroid.presenters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageButton;

import java.util.List;

import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.datamanager.AppDataManager;
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
    public void initButtonsByLoginModesNumber(WindowSizeTypes[] wsc) {
        if(wsc == null) return;
        if(appDataManager == null) return;

        String color1 = appDataManager.getStringValueFromSettingsFile("backgroundColor1");
        String color2 = appDataManager.getStringValueFromSettingsFile("backgroundGradientColor1");

        try {
            if(color1 != null && color2 != null){
                int[] colors = new int[] {
                        Color.parseColor(appDataManager.getStringValueFromSettingsFile("backgroundColor1")),
                        Color.parseColor(appDataManager.getStringValueFromSettingsFile("backgroundGradientColor1"))
                };

                int loginModesNumber = appDataManager.getIntValueFromSettingsFile("applicationEnabledLoginFlag1");
                appDataManager.initLoginButtons(loginModesNumber, wsc, colors);
            }
        }
        catch (Exception e){
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    @Override
    public void setControlsValuesBySettings() {
        if(iLoginActivity == null) return;
        if(appDataManager == null) return;

        String message = "Bejelentkezés";
        String applicationLead = appDataManager.getStringValueFromSettingsFile("applicationLead1");
        String backgroundColor = appDataManager.getStringValueFromSettingsFile("backgroundColor1");
        String backgroundGradientColor = appDataManager.getStringValueFromSettingsFile("backgroundGradientColor1");
        String foreGroundColor = appDataManager.getStringValueFromSettingsFile("foregroundColor1");
        String applicationName = appDataManager.getStringValueFromSettingsFile("applicationTitle1");

        // TODO ezt majd le kell tölteni egy URL-ről
        iLoginActivity.changeStateLoginLogo(R.drawable.ic_baseline_album);

        if(message != null && foreGroundColor != null) iLoginActivity.changeStateLoginTV(message, foreGroundColor);
        if(backgroundColor != null && backgroundGradientColor != null) iLoginActivity.changeStateMainActivityCL(backgroundColor, backgroundGradientColor);
        if(applicationLead != null && foreGroundColor != null) iLoginActivity.changeStateApplicationLeadTextbox(applicationLead, foreGroundColor);
        if(applicationName != null && foreGroundColor != null) iLoginActivity.changeStateApplicationTitleTextbox(applicationName, foreGroundColor);
        if(backgroundColor != null) iLoginActivity.changeMobileBarsColors(backgroundColor, backgroundGradientColor);
    }
}
