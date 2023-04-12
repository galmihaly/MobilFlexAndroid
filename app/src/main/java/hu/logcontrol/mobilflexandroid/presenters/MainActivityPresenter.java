package hu.logcontrol.mobilflexandroid.presenters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

import hu.logcontrol.mobilflexandroid.MainActivity;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivity;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivityPresenter;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.taskmanager.CustomThreadPoolManager;
import hu.logcontrol.mobilflexandroid.taskmanager.PresenterThreadCallback;

public class MainActivityPresenter implements IMainActivityPresenter, PresenterThreadCallback {

    private IMainActivity iMainActivity;
    private Context context;
    private CustomThreadPoolManager mCustomThreadPoolManager;
    private MainActivityHandler mainActivityHandler;

    public MainActivityPresenter(IMainActivity iMainActivity, Context context) {
        this.iMainActivity = iMainActivity;
        this.context = context.getApplicationContext();
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* ILoginActivityPresenter interfész függvényei */
    @Override
    public void initTaskManager() {
        try {
            ApplicationLogger.logging(LogLevel.INFORMATION, "A feladatkezelő létrehozása megkezdődött.");

            mainActivityHandler = new MainActivityHandler(Looper.myLooper(), this);
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
//        if(loginActivity == null) return;
//
//        Intent intent = null;
//
//        switch (viewEnum){
//            case SETTINGS_ACTIVITY:{
//                intent = new Intent(context, SettingsActivity.class);
//                break;
//            }
//            case WEBVIEW_ACTIVITY:{
//                intent = new Intent(context, WebViewActivity.class);
//                break;
//            }
//        }
//
//        if(intent == null) return;
//        if(loginActivity != null) loginActivity.openViewByIntent(intent);
    }
    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* PresenterThreadCallback interfész függvénye */
    @Override
    public void sendResultToPresenter(Message message) {
        if(mainActivityHandler == null) return;
        mainActivityHandler.sendMessage(message);
    }
    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */

    private static class MainActivityHandler extends Handler {


        private WeakReference<IMainActivityPresenter> iMainActivityPresenterWeakReference;

        public MainActivityHandler(Looper looper, IMainActivityPresenter iMainActivityPresenterWeakReference) {
            super(looper);
            this.iMainActivityPresenterWeakReference = new WeakReference<>(iMainActivityPresenterWeakReference);
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
