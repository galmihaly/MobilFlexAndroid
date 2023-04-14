package hu.logcontrol.mobilflexandroid.presenters;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.lang.ref.WeakReference;
import java.util.List;

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
    public void initButtonsByLoginModesNumber(int loginModesNumber, WindowSizeTypes[] wsc, int[] colors) {
        try {
            ApplicationLogger.logging(LogLevel.INFORMATION, "A bejelentkezési módok gombjainak létrehozása megkezdődött.");

            CreateLoginButtons callable = new CreateLoginButtons(context.getApplicationContext(), loginModesNumber, wsc, colors);
            callable.setCustomThreadPoolManager(mCustomThreadPoolManager);
            mCustomThreadPoolManager.addCallableMethod(callable);

            ApplicationLogger.logging(LogLevel.INFORMATION, "A bejelentkezési módok gombjainak létrehozása befejeződött.");
        }
        catch (Exception e){
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    @Override
    public void initTextsByCurrentValue() {
        if(preferences == null) return;

        hungaryWCPrefFile = new LanguagesSharedPreferences(context, "HungaryWCPrefFile");
        englishWCPrefFile = new LanguagesSharedPreferences(context, "EnglishWCPrefFile");
        germanWCPrefFile = new LanguagesSharedPreferences(context, "GermanWCPrefFile");

        int currentLanguageID = preferences.getIntValueByKey("CurrentSelectedLanguage");

        String message;
        String color;

        switch (currentLanguageID){
            case R.drawable.ic_hu2:{
                if(hungaryWCPrefFile != null){

                    message = hungaryWCPrefFile.getStringValueByKey("HU$WC_MessageTextView");
                    color = hungaryWCPrefFile.getStringValueByKey("HU$WC_MessageColor");
                    if(message != null) iLoginActivity.changeStateLoginTV(message, color);
                }
                break;
            }
            case R.drawable.ic_brit:{
                if(englishWCPrefFile != null){

                    message = hungaryWCPrefFile.getStringValueByKey("EN$WC_MessageTextView");
                    color = hungaryWCPrefFile.getStringValueByKey("EN$WC_MessageColor");
                    if(message != null) iLoginActivity.changeStateLoginTV(message, color);
                }
                break;
            }
            case R.drawable.ic_german:{
                if(germanWCPrefFile != null){

                    message = hungaryWCPrefFile.getStringValueByKey("DE$WC_MessageTextView");
                    color = hungaryWCPrefFile.getStringValueByKey("DE$WC_MessageColor");
                    if(message != null) iLoginActivity.changeStateLoginTV(message, color);
                }
                break;
            }
        }
    }

    @Override
    public void sendMessageToView(String message) {
        if(message == null) return;
        if(iLoginActivity != null) iLoginActivity.getMessageFromPresenter(message);
    }

    @Override
    public void sendCreatedButtonsToView(List<ImageButton> createdButtons) {
        if(createdButtons == null) return;
        Log.e("c", "beléptem ide");
        if(iLoginActivity != null) iLoginActivity.sendCreatedButtonsToView(createdButtons);
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
                    Log.e("a", "beléptem ide");
                    if(msg.obj instanceof List){
                        List<ImageButton> createdButtons = (List<ImageButton>) msg.obj;
                        Log.e("msg.obj", "beléptem ide");
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
