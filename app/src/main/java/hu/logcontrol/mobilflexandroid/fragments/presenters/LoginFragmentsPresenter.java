package hu.logcontrol.mobilflexandroid.fragments.presenters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import hu.logcontrol.mobilflexandroid.LoginActivity;
import hu.logcontrol.mobilflexandroid.WebViewActivity;
import hu.logcontrol.mobilflexandroid.enums.FragmentTypes;
import hu.logcontrol.mobilflexandroid.enums.MessageIdentifiers;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.fragments.interfaces.ILoginFragmentsPresenter;
import hu.logcontrol.mobilflexandroid.fragments.interfaces.ILoginFragments;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.models.LanguagesSharedPreferences;
import hu.logcontrol.mobilflexandroid.models.LocalEncryptedPreferences;
import hu.logcontrol.mobilflexandroid.models.SettingsObject;
import hu.logcontrol.mobilflexandroid.taskmanager.CustomThreadPoolManager;
import hu.logcontrol.mobilflexandroid.taskmanager.PresenterThreadCallback;

public class LoginFragmentsPresenter implements ILoginFragmentsPresenter, PresenterThreadCallback {

    private ILoginFragments iLoginFragments;
    private Context context;
    private CustomThreadPoolManager mCustomThreadPoolManager;
    private UserPassFragmentHandler userPassFragmentHandler;

    private LanguagesSharedPreferences hungaryWCPrefFile;
    private LanguagesSharedPreferences germanWCPrefFile;
    private LanguagesSharedPreferences englishWCPrefFile;

    private LocalEncryptedPreferences preferences;
    private SettingsObject s;

    public LoginFragmentsPresenter(ILoginFragments iLoginFragments, Context context) {
        this.iLoginFragments = iLoginFragments;
        this.context = context.getApplicationContext();
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* IUserPassFrPresenter interfész függvényei */
    @Override
    public void initTaskManager() {
        try {
            ApplicationLogger.logging(LogLevel.INFORMATION, "A feladatkezelő létrehozása megkezdődött.");

            userPassFragmentHandler = new UserPassFragmentHandler(Looper.myLooper(), this);
            mCustomThreadPoolManager = CustomThreadPoolManager.getsInstance();
            mCustomThreadPoolManager.setPresenterCallback(this);

            ApplicationLogger.logging(LogLevel.INFORMATION, "A feladatkezelő létrehozása befejeződött.");
        }
        catch (Exception e){
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    @Override
    public void initSettingsPreferenceFile() {
        preferences = LocalEncryptedPreferences.getInstance(
                "settings",
                MasterKeys.AES256_GCM_SPEC,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    @Override
    public void initLanguageSharedPreferenceFiles() {
        hungaryWCPrefFile = new LanguagesSharedPreferences(context, "HungaryWCPrefFile");
        englishWCPrefFile = new LanguagesSharedPreferences(context, "EnglishWCPrefFile");
        germanWCPrefFile = new LanguagesSharedPreferences(context, "GermanWCPrefFile");
    }

    @Override
    public void setControlsTextBySettings(FragmentTypes fragmentType) {
        if(fragmentType == null) return;
        if(preferences == null) return;
        if(iLoginFragments == null) return;
        if(hungaryWCPrefFile == null) return;
        if(englishWCPrefFile == null) return;
        if(germanWCPrefFile == null) return;

        String currentLanguage = preferences.getStringValueByKey("CurrentSelectedLanguage");
        String usernameTVLabel = null;
        String passwordTVLabel = null;
        String rfidTextlabel = null;
        String barcodeTextLabel = null;
        String pincodeTextLabel = null;

        switch (currentLanguage){
            case "HU":{
                if(fragmentType.equals(FragmentTypes.USERPASSFRAGMENT)) {
                    usernameTVLabel = hungaryWCPrefFile.getStringValueByKey("HU$WC_ApplicationUsernameTitle");
                    passwordTVLabel = hungaryWCPrefFile.getStringValueByKey("HU$WC_ApplicationPasswordTitle");
                }
                else if(fragmentType.equals(FragmentTypes.RFIDFRAGMENT)) {
                    rfidTextlabel = hungaryWCPrefFile.getStringValueByKey("HU$WC_ApplicationRFIDTitle");
                }
                else if(fragmentType.equals(FragmentTypes.BARCODEFRAGMENT)) {
                    barcodeTextLabel = hungaryWCPrefFile.getStringValueByKey("HU$WC_ApplicationBarCodeTitle");
                }
                else if(fragmentType.equals(FragmentTypes.PINCODEFRAGMENT)) {
                    pincodeTextLabel = hungaryWCPrefFile.getStringValueByKey("HU$WC_ApplicationPinCodeTitle");
                }
                break;
            }
            case "EN":{
                if(fragmentType.equals(FragmentTypes.USERPASSFRAGMENT)) {
                    usernameTVLabel = englishWCPrefFile.getStringValueByKey("EN$WC_ApplicationUsernameTitle");
                    passwordTVLabel = englishWCPrefFile.getStringValueByKey("EN$WC_ApplicationPasswordTitle");
                }
                else if(fragmentType.equals(FragmentTypes.RFIDFRAGMENT)) {
                    rfidTextlabel = englishWCPrefFile.getStringValueByKey("EN$WC_ApplicationRFIDTitle");
                }
                else if(fragmentType.equals(FragmentTypes.BARCODEFRAGMENT)) {
                    barcodeTextLabel = englishWCPrefFile.getStringValueByKey("EN$WC_ApplicationBarCodeTitle");
                }
                else if(fragmentType.equals(FragmentTypes.PINCODEFRAGMENT)) {
                    pincodeTextLabel = englishWCPrefFile.getStringValueByKey("EN$WC_ApplicationPinCodeTitle");
                }
                break;
            }
            case "DE":{
                if(fragmentType.equals(FragmentTypes.USERPASSFRAGMENT)) {
                    usernameTVLabel = germanWCPrefFile.getStringValueByKey("DE$WC_ApplicationUsernameTitle");
                    passwordTVLabel = germanWCPrefFile.getStringValueByKey("DE$WC_ApplicationPasswordTitle");
                }
                else if(fragmentType.equals(FragmentTypes.RFIDFRAGMENT)) {
                    rfidTextlabel = germanWCPrefFile.getStringValueByKey("DE$WC_ApplicationRFIDTitle");
                }
                else if(fragmentType.equals(FragmentTypes.BARCODEFRAGMENT)) {
                    barcodeTextLabel = germanWCPrefFile.getStringValueByKey("DE$WC_ApplicationBarCodeTitle");
                }
                else if(fragmentType.equals(FragmentTypes.PINCODEFRAGMENT)) {
                    pincodeTextLabel = germanWCPrefFile.getStringValueByKey("DE$WC_ApplicationPinCodeTitle");
                }
                break;
            }
        }

        if(usernameTVLabel != null && passwordTVLabel != null) { iLoginFragments.changeTextInputElemenets(usernameTVLabel, passwordTVLabel); }
        if(rfidTextlabel != null) { iLoginFragments.changeTextInputElemenets(rfidTextlabel, null); }
        if(barcodeTextLabel != null) { iLoginFragments.changeTextInputElemenets(barcodeTextLabel, null); }
        if(pincodeTextLabel != null) { iLoginFragments.changeTextInputElemenets(pincodeTextLabel, null); }

    }

    @Override
    public void openActivityByEnum(ViewEnums viewEnum) {
        if(viewEnum == null) return;
        if(iLoginFragments == null) return;

        Intent intent = null;

        switch (viewEnum){
            case WEBVIEW_ACTIVITY:{
                intent = new Intent(context, WebViewActivity.class);
                break;
            }
        }

        if(intent == null) return;
        if(iLoginFragments != null) iLoginFragments.openViewByIntent(intent);

    }

    @Override
    public void setControlsValuesBySettings() {
        if(iLoginFragments == null) return;
        if(preferences == null) return;
        if(hungaryWCPrefFile == null) return;
        if(englishWCPrefFile == null) return;
        if(germanWCPrefFile == null) return;

        String controlColor = preferences.getStringValueByKey("controlColor");
        String textColor = preferences.getStringValueByKey("foregroundColor");

        String buttonBackgroundColor = preferences.getStringValueByKey("buttonBackgroundColor");
        String buttonBackgroundGradientColor = preferences.getStringValueByKey("buttonBackgroundGradientColor");
        String buttonForeGroundColor = preferences.getStringValueByKey("buttonForegroundColor");

        String currentLanguage = preferences.getStringValueByKey("CurrentSelectedLanguage");
        String buttonLabel = null;

        switch (currentLanguage){
            case "HU":{ buttonLabel = hungaryWCPrefFile.getStringValueByKey("HU$WC_ApplicationLoginButtonTitle"); break; }
            case "EN":{ buttonLabel = englishWCPrefFile.getStringValueByKey("EN$WC_ApplicationLoginButtonTitle"); break; }
            case "DE":{ buttonLabel = germanWCPrefFile.getStringValueByKey("DE$WC_ApplicationLoginButtonTitle"); break; }
        }

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

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* PresenterThreadCallback interfész függvénye */
    @Override
    public void sendResultToPresenter(Message message) {
        if(userPassFragmentHandler == null) return;
        userPassFragmentHandler.sendMessage(message);
    }

    private static class UserPassFragmentHandler extends Handler {

        private WeakReference<ILoginFragmentsPresenter> iUserPassFrPresenterWeakReference;

        public UserPassFragmentHandler(Looper looper, ILoginFragmentsPresenter iLoginFragmentsPresenterWeakReference) {
            super(looper);
            this.iUserPassFrPresenterWeakReference = new WeakReference<>(iLoginFragmentsPresenterWeakReference);
        }

        // Ui-ra szánt üzenetet kezelejük itt
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MessageIdentifiers.EXCEPTION:{
//                    iLoginActivityPresenterWeakReference.get().sendMessageToView(getWeakReferenceNotification(msg));
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
