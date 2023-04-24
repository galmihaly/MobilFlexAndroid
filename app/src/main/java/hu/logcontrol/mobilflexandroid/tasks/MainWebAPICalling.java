package hu.logcontrol.mobilflexandroid.tasks;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.enums.MessageIdentifiers;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;
import hu.logcontrol.mobilflexandroid.helpers.Helper;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.taskmanager.CustomThreadPoolManager;

public class MainWebAPICalling implements Callable {

    private WeakReference<CustomThreadPoolManager> ctpmw;

    private Context context;

    private Message message;
    private String hardwareID;

    public MainWebAPICalling(Context context) {
        this.context = context;
    }

    public void setCustomThreadPoolManager(CustomThreadPoolManager customThreadPoolManager) {
        this.ctpmw = new WeakReference<>(customThreadPoolManager);
    }

    @Override
    public Object call() {

        try{

            if (Thread.interrupted()) throw new InterruptedException();

            Log.e("Callable", "Callable");

            sendMessageToPresenterHandler(MainWebAPICallingEnums.HARDWARE_ID_FAILED);

        } catch (Exception e){
            sendMessageToPresenterHandler(MainWebAPICallingEnums.THREAD_INTERRUPTED);
        }

        return null;
    }

    private void sendMessageToPresenterHandler(MainWebAPICallingEnums e){

        switch (e){
            case THREAD_INTERRUPTED:{
                message = Helper.createMessage(MessageIdentifiers.HARDWARE_ID_FAILED, "A szál létrehozása során hiba kelettkezett!");
                break;
            }
            case HARDWARE_ID_FAILED:{
                message = Helper.createMessage(MessageIdentifiers.THREAD_INTERRUPTED, "Eszköz azonosítójának lekérdezése során hiba keletkezett!");
                break;
            }
        }

        if(ctpmw != null && ctpmw.get() != null && message != null) {
            ctpmw.get().sendResultToPresenter(message);
        }
    }

    private enum MainWebAPICallingEnums{
        THREAD_INTERRUPTED,
        HARDWARE_ID_FAILED,
    }
}
