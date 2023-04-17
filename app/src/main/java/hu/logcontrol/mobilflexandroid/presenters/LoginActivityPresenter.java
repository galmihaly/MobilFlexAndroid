package hu.logcontrol.mobilflexandroid.presenters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.enums.MessageIdentifiers;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;
import hu.logcontrol.mobilflexandroid.interfaces.ILoginActivity;
import hu.logcontrol.mobilflexandroid.interfaces.ILoginActivityPresenter;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivity;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivityPresenter;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.models.LanguagesSharedPreferences;
import hu.logcontrol.mobilflexandroid.models.LocalEncryptedPreferences;
import hu.logcontrol.mobilflexandroid.models.SettingsObject;
import hu.logcontrol.mobilflexandroid.taskmanager.CustomThreadPoolManager;
import hu.logcontrol.mobilflexandroid.taskmanager.PresenterThreadCallback;
import hu.logcontrol.mobilflexandroid.tasks.CreateLoginButtons;

public class LoginActivityPresenter implements ILoginActivityPresenter, PresenterThreadCallback {

    private ILoginActivity iLoginActivity;
    private Context context;
    private CustomThreadPoolManager mCustomThreadPoolManager;
    private LoginActivityHandler loginActivityHandler;

    private LanguagesSharedPreferences hungaryWCPrefFile;
    private LanguagesSharedPreferences germanWCPrefFile;
    private LanguagesSharedPreferences englishWCPrefFile;

    private LocalEncryptedPreferences preferences;
    private SettingsObject s;

    public LoginActivityPresenter(ILoginActivity iLoginActivity, Context context) {
        this.iLoginActivity = iLoginActivity;
        this.context = context.getApplicationContext();
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* ILoginActivityPresenter interfész függvényei */
    @Override
    public void initTaskManager() {
        try {
            ApplicationLogger.logging(LogLevel.INFORMATION, "A feladatkezelő létrehozása megkezdődött.");

            loginActivityHandler = new LoginActivityHandler(Looper.myLooper(), this);
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
    public void initButtonsByLoginModesNumber(WindowSizeTypes[] wsc) {
        try {
            if(preferences != null){

                ApplicationLogger.logging(LogLevel.INFORMATION, "A bejelentkezési módok gombjainak létrehozása megkezdődött.");

                int[] colors = new int[] {
                        Color.parseColor(preferences.getStringValueByKey("backgroundColor")),
                        Color.parseColor(preferences.getStringValueByKey("backgroundGradientColor"))
                };

                int loginModesNumber = preferences.getIntValueByKey("applicationEnabledLoginFlag");

                CreateLoginButtons callable = new CreateLoginButtons(context.getApplicationContext(), loginModesNumber, wsc, colors);
                callable.setCustomThreadPoolManager(mCustomThreadPoolManager);
                mCustomThreadPoolManager.addCallableMethod(callable);

                ApplicationLogger.logging(LogLevel.INFORMATION, "A bejelentkezési módok gombjainak létrehozása befejeződött.");

            }
        }
        catch (Exception e){
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

//    @Override
//    public void initTextsByCurrentValue() {
//        if(preferences == null) return;
//
//        hungaryWCPrefFile = new LanguagesSharedPreferences(context, "HungaryWCPrefFile");
//        englishWCPrefFile = new LanguagesSharedPreferences(context, "EnglishWCPrefFile");
//        germanWCPrefFile = new LanguagesSharedPreferences(context, "GermanWCPrefFile");
//
//        int currentLanguageID = preferences.getIntValueByKey("CurrentSelectedLanguage");
//
//        String message;
//        String color;
//
//        switch (currentLanguageID){
//            case R.drawable.ic_hu2:{
//                if(hungaryWCPrefFile != null){
//
//                    message = hungaryWCPrefFile.getStringValueByKey("HU$WC_MessageTextView");
//                    color = hungaryWCPrefFile.getStringValueByKey("HU$WC_MessageColor");
//                    if(message != null) iLoginActivity.changeStateLoginTV(message, color);
//                }
//                break;
//            }
//            case R.drawable.ic_brit:{
//                if(englishWCPrefFile != null){
//
//                    message = hungaryWCPrefFile.getStringValueByKey("EN$WC_MessageTextView");
//                    color = hungaryWCPrefFile.getStringValueByKey("EN$WC_MessageColor");
//                    if(message != null) iLoginActivity.changeStateLoginTV(message, color);
//                }
//                break;
//            }
//            case R.drawable.ic_german:{
//                if(germanWCPrefFile != null){
//
//                    message = hungaryWCPrefFile.getStringValueByKey("DE$WC_MessageTextView");
//                    color = hungaryWCPrefFile.getStringValueByKey("DE$WC_MessageColor");
//                    if(message != null) iLoginActivity.changeStateLoginTV(message, color);
//                }
//                break;
//            }
//        }
//    }

    @Override
    public void sendMessageToView(String message) {
        if(message == null) return;
        if(iLoginActivity != null) iLoginActivity.getMessageFromPresenter(message);
    }

    @Override
    public void sendCreatedButtonsToView(List<ImageButton> createdButtons) {
        if(createdButtons == null) return;
        if(iLoginActivity != null) iLoginActivity.sendCreatedButtonsToView(createdButtons);
    }

    @Override
    public void saveSettingsToPreferences(Intent intent) {
        if(intent == null) return;

        SettingsObject s = (SettingsObject) intent.getSerializableExtra("settingsObject");

        preferences.replaceString("deviceIdentifier", s.getDeviceIdentifier());
        preferences.replaceString("deviceName", s.getDeviceName());
        preferences.replaceString("applicationIdentifier", s.getApplicationIdentifier().toString());
        preferences.replaceString("applicationName", s.getApplicationName());
        preferences.replaceString("applicationTitle", s.getApplicationTitle());
        preferences.replaceString("applicationLead", s.getApplicationLead());
        preferences.replaceString("applicationDescription", s.getApplicationDescription());
        preferences.replaceString("applicationVersion", s.getApplicationVersion());
        preferences.replaceInt("applicationEnabledLoginFlag", s.getApplicationEnabledLoginFlag());
        preferences.replaceString("logoImageUrl", s.getLogoImageUrl());
        preferences.replaceString("loginWebApiUrl", s.getLoginWebApiUrl());
        preferences.replaceString("mainWebApiUrl", s.getMainWebApiUrl());
        preferences.replaceString("settingsWebApiUrl", s.getSettingsWebApiUrl());
        preferences.replaceString("helpWebApiUrl", s.getHelpWebApiUrl());
        preferences.replaceString("backgroundColor", s.getBackgroundColor());
        preferences.replaceString("backgroundGradientColor", s.getBackgroundGradientColor());
        preferences.replaceString("foregroundColor", s.getForegroundColor());
        preferences.replaceString("buttonBackgroundColor", s.getButtonBackgroundColor());
        preferences.replaceString("buttonBackgroundGradientColor", s.getButtonBackgroundGradientColor());
        preferences.replaceString("buttonForegroundColor", s.getForegroundColor());
        preferences.replaceString("controlColor", s.getControlColor());

        Log.e("deviceIdentifier", preferences.getStringValueByKey("deviceIdentifier"));
        Log.e("deviceName", preferences.getStringValueByKey("deviceName"));
        Log.e("applicationIdentifier", preferences.getStringValueByKey("applicationIdentifier"));
        Log.e("applicationName", preferences.getStringValueByKey("applicationName"));
        Log.e("applicationTitle", preferences.getStringValueByKey("applicationTitle"));
        Log.e("applicationLead", preferences.getStringValueByKey("applicationLead"));
        Log.e("applicationDescription", preferences.getStringValueByKey("applicationDescription"));
        Log.e("applicationVersion", preferences.getStringValueByKey("applicationVersion"));
        Log.e("applicationEnabledLoginFlag", preferences.getStringValueByKey("applicationEnabledLoginFlag"));
        Log.e("logoImageUrl", preferences.getStringValueByKey("logoImageUrl"));
        Log.e("loginWebApiUrl", preferences.getStringValueByKey("loginWebApiUrl"));
        Log.e("mainWebApiUrl", preferences.getStringValueByKey("mainWebApiUrl"));
        Log.e("settingsWebApiUrl", preferences.getStringValueByKey("settingsWebApiUrl"));
        Log.e("helpWebApiUrl", preferences.getStringValueByKey("helpWebApiUrl"));

//        List<String> languages = s.getLanguages();
//        List<String> wordCodes = s.getWordCodes();
//
//        HashMap<String, String> translations = s.getTranslations();

        Log.e("backgroundColor", preferences.getStringValueByKey("backgroundColor"));
        Log.e("backgroundGradientColor", preferences.getStringValueByKey("backgroundGradientColor"));
        Log.e("foregroundColor", preferences.getStringValueByKey("foregroundColor"));
        Log.e("buttonBackgroundColor", preferences.getStringValueByKey("buttonBackgroundColor"));
        Log.e("buttonBackgroundGradientColor", preferences.getStringValueByKey("buttonBackgroundGradientColor"));
        Log.e("buttonForegroundColor", preferences.getStringValueByKey("buttonForegroundColor"));
        Log.e("controlColor", preferences.getStringValueByKey("controlColor"));

//        for (int i = 0; i < languages.size(); i++) {
//            Log.e("languages", languages.get(i));
//        }
//
//        for (int i = 0; i < wordCodes.size(); i++) {
//            Log.e("wordCodes", wordCodes.get(i));
//        }
//
//        for (Map.Entry<String, String> entry : translations.entrySet()){
//            Log.e("translations", entry.getKey() + "   " + entry.getValue());
//        }
    }

    @Override
    public void setControlsValuesBySettings() {
        if(iLoginActivity == null) return;
        if(preferences == null) return;

        String message = preferences.getStringValueByKey("applicationTitle");
        String foreGroundColor = preferences.getStringValueByKey("foregroundColor");

        if(message != null && foreGroundColor != null) iLoginActivity.changeStateLoginTV(message, foreGroundColor);

        iLoginActivity.changeStateLoginLogo(R.drawable.ic_baseline_album);

        String backgroundColor = preferences.getStringValueByKey("backgroundColor");
        String backgroundGradientColor = preferences.getStringValueByKey("backgroundGradientColor");

        if(backgroundColor != null && backgroundGradientColor != null) iLoginActivity.changeStateMainActivityCL(backgroundColor, backgroundGradientColor);
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* PresenterThreadCallback interfész függvénye */
    @Override
    public void sendResultToPresenter(Message message) {
        if(loginActivityHandler == null) return;
        loginActivityHandler.sendMessage(message);
    }

    private static class LoginActivityHandler extends Handler {

        private WeakReference<ILoginActivityPresenter> iLoginActivityPresenterWeakReference;

        public LoginActivityHandler(Looper looper, ILoginActivityPresenter iLoginActivityPresenterWeakReference) {
            super(looper);
            this.iLoginActivityPresenterWeakReference = new WeakReference<>(iLoginActivityPresenterWeakReference);
        }

        // Ui-ra szánt üzenetet kezelejük itt
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MessageIdentifiers.EXCEPTION:{
                    iLoginActivityPresenterWeakReference.get().sendMessageToView(getWeakReferenceNotification(msg));
                    break;
                }
                case MessageIdentifiers.BUTTONS_IS_CREATED:{
                    if(msg.obj instanceof List){
                        List<ImageButton> createdButtons = (List<ImageButton>) msg.obj;
                        iLoginActivityPresenterWeakReference.get().sendCreatedButtonsToView(createdButtons);
                    }
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
