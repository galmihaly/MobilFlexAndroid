package hu.logcontrol.mobilflexandroid.fragments.presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import hu.logcontrol.mobilflexandroid.WebViewActivity;
import hu.logcontrol.mobilflexandroid.datamanager.AppDataManager;
import hu.logcontrol.mobilflexandroid.enums.FragmentTypes;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.fragments.interfaces.ILoginFragmentsPresenter;
import hu.logcontrol.mobilflexandroid.fragments.interfaces.ILoginFragments;

public class LoginFragmentsPresenter implements ILoginFragmentsPresenter {

    private ILoginFragments iLoginFragments;
    private Context context;

    private AppDataManager appDataManager;

    public LoginFragmentsPresenter(ILoginFragments iLoginFragments, Context context) {
        this.iLoginFragments = iLoginFragments;
        this.context = context.getApplicationContext();
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* IUserPassFrPresenter interfész függvényei */

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
            appDataManager.createSettingsWebAPIService(baseUrl);
        }
    }

    @Override
    public void setControlsTextBySettings(FragmentTypes fragmentType) {
        if(fragmentType == null) return;
        if(iLoginFragments == null) return;
        if(appDataManager == null) return;

        String currentLanguage = appDataManager.getStringValueFromSettingsFile("CurrentSelectedLanguage");
        String usernameTVLabel = null;
        String passwordTVLabel = null;
        String rfidTextlabel = null;
        String barcodeTextLabel = null;
        String pincodeTextLabel = null;

//        switch (currentLanguage){
//            case "HU":{
//                if(fragmentType.equals(FragmentTypes.USERPASSFRAGMENT)) {
//                    usernameTVLabel = hungaryWCPrefFile.getStringValueByKey("HU$WC_ApplicationUsernameTitle");
//                    passwordTVLabel = hungaryWCPrefFile.getStringValueByKey("HU$WC_ApplicationPasswordTitle");
//                }
//                else if(fragmentType.equals(FragmentTypes.RFIDFRAGMENT)) {
//                    rfidTextlabel = hungaryWCPrefFile.getStringValueByKey("HU$WC_ApplicationRFIDTitle");
//                }
//                else if(fragmentType.equals(FragmentTypes.BARCODEFRAGMENT)) {
//                    barcodeTextLabel = hungaryWCPrefFile.getStringValueByKey("HU$WC_ApplicationBarCodeTitle");
//                }
//                else if(fragmentType.equals(FragmentTypes.PINCODEFRAGMENT)) {
//                    pincodeTextLabel = hungaryWCPrefFile.getStringValueByKey("HU$WC_ApplicationPinCodeTitle");
//                }
//                break;
//            }
//            case "EN":{
//                if(fragmentType.equals(FragmentTypes.USERPASSFRAGMENT)) {
//                    usernameTVLabel = englishWCPrefFile.getStringValueByKey("EN$WC_ApplicationUsernameTitle");
//                    passwordTVLabel = englishWCPrefFile.getStringValueByKey("EN$WC_ApplicationPasswordTitle");
//                }
//                else if(fragmentType.equals(FragmentTypes.RFIDFRAGMENT)) {
//                    rfidTextlabel = englishWCPrefFile.getStringValueByKey("EN$WC_ApplicationRFIDTitle");
//                }
//                else if(fragmentType.equals(FragmentTypes.BARCODEFRAGMENT)) {
//                    barcodeTextLabel = englishWCPrefFile.getStringValueByKey("EN$WC_ApplicationBarCodeTitle");
//                }
//                else if(fragmentType.equals(FragmentTypes.PINCODEFRAGMENT)) {
//                    pincodeTextLabel = englishWCPrefFile.getStringValueByKey("EN$WC_ApplicationPinCodeTitle");
//                }
//                break;
//            }
//            case "DE":{
//                if(fragmentType.equals(FragmentTypes.USERPASSFRAGMENT)) {
//                    usernameTVLabel = germanWCPrefFile.getStringValueByKey("DE$WC_ApplicationUsernameTitle");
//                    passwordTVLabel = germanWCPrefFile.getStringValueByKey("DE$WC_ApplicationPasswordTitle");
//                }
//                else if(fragmentType.equals(FragmentTypes.RFIDFRAGMENT)) {
//                    rfidTextlabel = germanWCPrefFile.getStringValueByKey("DE$WC_ApplicationRFIDTitle");
//                }
//                else if(fragmentType.equals(FragmentTypes.BARCODEFRAGMENT)) {
//                    barcodeTextLabel = germanWCPrefFile.getStringValueByKey("DE$WC_ApplicationBarCodeTitle");
//                }
//                else if(fragmentType.equals(FragmentTypes.PINCODEFRAGMENT)) {
//                    pincodeTextLabel = germanWCPrefFile.getStringValueByKey("DE$WC_ApplicationPinCodeTitle");
//                }
//                break;
//            }
//        }
//
//        if(usernameTVLabel != null && passwordTVLabel != null) { iLoginFragments.changeTextInputElemenets(usernameTVLabel, passwordTVLabel); }
//        if(rfidTextlabel != null) { iLoginFragments.changeTextInputElemenets(rfidTextlabel, null); }
//        if(barcodeTextLabel != null) { iLoginFragments.changeTextInputElemenets(barcodeTextLabel, null); }
//        if(pincodeTextLabel != null) { iLoginFragments.changeTextInputElemenets(pincodeTextLabel, null); }

    }

    @Override
    public void openActivityByEnum(ViewEnums viewEnum, int applicationId, int defaultThemeId) {
        if(viewEnum == null) return;
        if(iLoginFragments == null) return;

        Intent intent = null;

        switch (viewEnum){
            case WEBVIEW_ACTIVITY:{
                intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("applicationId", applicationId);
                intent.putExtra("defaultThemeId", defaultThemeId);
                break;
            }
        }

        if(intent == null) return;
        if(iLoginFragments != null) iLoginFragments.openViewByIntent(intent);

    }

    @Override
    public void setControlsValuesBySettings(int defaultThemeId, int applicationId) {
        if(iLoginFragments == null) return;
        if(appDataManager == null) return;

        if(defaultThemeId != -1 && applicationId != -1){

            String controlColor = appDataManager.getStringValueFromSettingsFile("controlColor" + '_' + applicationId + '_' + defaultThemeId);
            String textColor = appDataManager.getStringValueFromSettingsFile("foregroundColor" + '_' + applicationId + '_' + defaultThemeId);

            String buttonBackgroundColor = appDataManager.getStringValueFromSettingsFile("buttonBackgroundColor" + '_' + applicationId + '_' + defaultThemeId);
            String buttonBackgroundGradientColor = appDataManager.getStringValueFromSettingsFile("buttonBackgroundGradientColor" + '_' + applicationId + '_' + defaultThemeId);
            String buttonForeGroundColor = appDataManager.getStringValueFromSettingsFile("buttonForegroundColor" + '_' + applicationId + '_' + defaultThemeId);

            String currentLanguage = appDataManager.getStringValueFromSettingsFile("CurrentSelectedLanguage");
            String buttonLabel = null;

//        switch (currentLanguage){
//            case "HU":{ buttonLabel = hungaryWCPrefFile.getStringValueByKey("HU$WC_ApplicationLoginButtonTitle"); break; }
//            case "EN":{ buttonLabel = englishWCPrefFile.getStringValueByKey("EN$WC_ApplicationLoginButtonTitle"); break; }
//            case "DE":{ buttonLabel = germanWCPrefFile.getStringValueByKey("DE$WC_ApplicationLoginButtonTitle"); break; }
//        }

            buttonLabel = "Bejelentkezés";

            if(controlColor != null && textColor != null) {
                iLoginFragments.changeStateUserPassElements(controlColor, textColor);
            }

            if(buttonBackgroundColor != null && buttonBackgroundGradientColor != null && buttonForeGroundColor != null && buttonLabel != null) {
                iLoginFragments.changeStateLoginButton(
                        buttonBackgroundColor,
                        buttonBackgroundGradientColor,
                        buttonForeGroundColor,
                        buttonLabel
                );
            }
        }
    }

    @Override
    public void startLogin(String identifier, String authenticationToken, int loginModeEnum) {
        if(appDataManager == null) return;
        if(identifier == null) return;

        String data = "";
        boolean createSession = true;

        appDataManager.callSettingsWebAPI(loginModeEnum, identifier, authenticationToken, data, createSession);
    }
}
