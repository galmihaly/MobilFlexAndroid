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
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.interfaces.IWebViewActivity;
import hu.logcontrol.mobilflexandroid.interfaces.IWebViewActivityPresenter;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.models.LocalEncryptedPreferences;
import hu.logcontrol.mobilflexandroid.taskmanager.CustomThreadPoolManager;
import hu.logcontrol.mobilflexandroid.taskmanager.PresenterThreadCallback;

public class WebViewActivityPresenter implements IWebViewActivityPresenter, PresenterThreadCallback {

    private IWebViewActivity webViewActivity;
    private Context context;
    private CustomThreadPoolManager mCustomThreadPoolManager;
    private WebViewActivityHandler mWebViewActivityHandler;

    private LocalEncryptedPreferences preferences;

    public WebViewActivityPresenter(IWebViewActivity webViewActivity, Context context) {
        this.webViewActivity = webViewActivity;
        this.context = context.getApplicationContext();
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* IWebViewActivityPresenter interfész függvényei */

    @Override
    public void initTaskManager() {
        try {
            ApplicationLogger.logging(LogLevel.INFORMATION, "A feladatkezelő létrehozása megkezdődött.");

            mWebViewActivityHandler = new WebViewActivityHandler(Looper.myLooper(), this);
            mCustomThreadPoolManager = CustomThreadPoolManager.getsInstance();
            mCustomThreadPoolManager.setPresenterCallback(this);

            ApplicationLogger.logging(LogLevel.INFORMATION, "A feladatkezelő létrehozása befejeződött.");
        }
        catch (Exception e){
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    @Override
    public void openActivityByEnum(ViewEnums viewEnum) {
        if(viewEnum == null) return;
        if(webViewActivity == null) return;

        Intent intent = null;

        switch (viewEnum){
            case LOGIN_ACTIVITY:{
                intent = new Intent(context, LoginActivity.class);
                break;
            }
        }

        if(intent == null) return;
        if(webViewActivity != null) webViewActivity.openViewByIntent(intent);
    }

    @Override
    public void initSharedPreferenceFile() {
        preferences = LocalEncryptedPreferences.getInstance(
                "values",
                MasterKeys.AES256_GCM_SPEC,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    @Override
    public void getValuesFromSettingsPrefFile() {
        if(preferences == null) return;
        if(webViewActivity == null) return;

        String appBarBackgroundColor = preferences.getStringValueByKey("backgroundColor");
        String appBarBackgroundGradientColor = preferences.getStringValueByKey("backgroundGradientColor");

        if(appBarBackgroundColor != null && appBarBackgroundGradientColor != null) webViewActivity.changeStateAppbarLayout(appBarBackgroundColor, appBarBackgroundGradientColor);
        if(appBarBackgroundColor != null && appBarBackgroundGradientColor != null) webViewActivity.changeMobileBarsColors(appBarBackgroundColor, appBarBackgroundGradientColor);

        String buttonBackgroundColor = preferences.getStringValueByKey("buttonBackgroundColor");
        String buttonBackgroundGradientColor = preferences.getStringValueByKey("buttonBackgroundGradientColor");

        if(buttonBackgroundColor != null && buttonBackgroundGradientColor != null) {
            webViewActivity.changeStateLoginButton( buttonBackgroundColor, buttonBackgroundGradientColor);
        }
    }

    @Override
    public void getURLfromSettings() {
        if(preferences == null) return;

        String loginWebApiUrl = preferences.getStringValueByKey("loginWebApiUrl");
        Log.e("loginWebApiUrl", loginWebApiUrl);
        if(loginWebApiUrl != null) webViewActivity.loadLoginWebAPIUrl(loginWebApiUrl);
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* PresenterThreadCallback interfész függvénye */

    @Override
    public void sendResultToPresenter(Message message) {
        if(mWebViewActivityHandler == null) return;
        mWebViewActivityHandler.sendMessage(message);
    }
    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */

    private static class WebViewActivityHandler extends Handler {


        private WeakReference<IWebViewActivityPresenter> iWebViewActivityPresenterWeakReference;

        public WebViewActivityHandler(Looper looper, IWebViewActivityPresenter iWebViewActivityPresenter) {
            super(looper);
            this.iWebViewActivityPresenterWeakReference = new WeakReference<>(iWebViewActivityPresenter);
        }

        // Ui-ra szánt üzenetet kezelejük itt
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
            }
        }
    }
}
