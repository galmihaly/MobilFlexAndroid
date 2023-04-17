package hu.logcontrol.mobilflexandroid.fragments.presenters;

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
import hu.logcontrol.mobilflexandroid.fragments.interfaces.IUserPassFrPresenter;
import hu.logcontrol.mobilflexandroid.fragments.interfaces.IUserPassFragment;
import hu.logcontrol.mobilflexandroid.interfaces.ILoginActivity;
import hu.logcontrol.mobilflexandroid.interfaces.ILoginActivityPresenter;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.models.LanguagesSharedPreferences;
import hu.logcontrol.mobilflexandroid.models.LocalEncryptedPreferences;
import hu.logcontrol.mobilflexandroid.models.SettingsObject;
import hu.logcontrol.mobilflexandroid.presenters.LoginActivityPresenter;
import hu.logcontrol.mobilflexandroid.taskmanager.CustomThreadPoolManager;
import hu.logcontrol.mobilflexandroid.taskmanager.PresenterThreadCallback;

public class UserPassPresenter implements IUserPassFrPresenter, PresenterThreadCallback {

    private IUserPassFragment iUserPassFragment;
    private Context context;
    private CustomThreadPoolManager mCustomThreadPoolManager;
    private UserPassFragmentHandler userPassFragmentHandler;

    private LanguagesSharedPreferences hungaryWCPrefFile;
    private LanguagesSharedPreferences germanWCPrefFile;
    private LanguagesSharedPreferences englishWCPrefFile;

    private LocalEncryptedPreferences preferences;
    private SettingsObject s;

    public UserPassPresenter(IUserPassFragment iUserPassFragment, Context context) {
        this.iUserPassFragment = iUserPassFragment;
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
    public void setControlsValuesBySettings() {
        if(iUserPassFragment == null) return;
        if(preferences == null) return;

        Log.e("setControlsValuesBySettings", "beléptem ide");

        String controlColor = preferences.getStringValueByKey("controlColor");
        String textColor = preferences.getStringValueByKey("foregroundColor");

        Log.e("controlColor", controlColor);
        Log.e("textColor", textColor);

        if(controlColor != null && textColor != null) iUserPassFragment.changeStateTextInputEditText(controlColor, textColor);
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* PresenterThreadCallback interfész függvénye */
    @Override
    public void sendResultToPresenter(Message message) {
        if(userPassFragmentHandler == null) return;
        userPassFragmentHandler.sendMessage(message);
    }

    private static class UserPassFragmentHandler extends Handler {

        private WeakReference<IUserPassFrPresenter> iUserPassFrPresenterWeakReference;

        public UserPassFragmentHandler(Looper looper, IUserPassFrPresenter iUserPassFrPresenterWeakReference) {
            super(looper);
            this.iUserPassFrPresenterWeakReference = new WeakReference<>(iUserPassFrPresenterWeakReference);
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
