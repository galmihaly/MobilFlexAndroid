package hu.logcontrol.mobilflexandroid.tasks;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import hu.logcontrol.mobilflexandroid.datamanager.MainPreferenceFileService;
import hu.logcontrol.mobilflexandroid.enums.MessageIdentifiers;
import hu.logcontrol.mobilflexandroid.helpers.Helper;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.models.ProgramsResultObject;
import hu.logcontrol.mobilflexandroid.taskmanager.CustomThreadPoolManager;

public class ProcessSVGLogo implements Callable {

    private WeakReference<CustomThreadPoolManager> ctpmw;
    private Context context;
    private MainPreferenceFileService mainPreferenceFileService;
    private List<String> fileNames;
    private int applicationsSize;

    private int defaultThemeId;
    private String applicationTitle;
    private String backgroundColor;
    private String backgroundGradientColor;
    private String logoUrl;
    private Drawable drawable;

    private final List<ProgramsResultObject> pList = new ArrayList<>();
    private ProgramsResultObject p;
    private Message message;


    public ProcessSVGLogo(Context context, MainPreferenceFileService mainPreferenceFileService, List<String> fileNames, int applicationsSize) {
        this.context = context.getApplicationContext();
        this.mainPreferenceFileService = mainPreferenceFileService;
        this.fileNames = fileNames;
        this.applicationsSize = applicationsSize;
    }

    public void setCustomThreadPoolManager(CustomThreadPoolManager customThreadPoolManager) {
        this.ctpmw = new WeakReference<>(customThreadPoolManager);
    }

    @SuppressLint("CheckResult")
    @Override
    public Object call() throws Exception {
        try {
            if (Thread.interrupted()) throw new InterruptedException();

            for (int j = 0; j < fileNames.size(); j++) {
                Log.e("fileName" + j, fileNames.get(j));
            }

            if(mainPreferenceFileService != null){
                for (int i = 0; i < applicationsSize; i++) {

                    defaultThemeId = mainPreferenceFileService.getIntValueFromSettingsPrefFile("defaultThemeId" + '_' + (i + 1));
                    applicationTitle = mainPreferenceFileService.getStringValueFromSettingsPrefFile("applicationTitle" + '_' + (i + 1));
                    backgroundColor = mainPreferenceFileService.getStringValueFromSettingsPrefFile("backgroundColor" + '_' + (i + 1) + '_' + defaultThemeId);
                    backgroundGradientColor = mainPreferenceFileService.getStringValueFromSettingsPrefFile("backgroundGradientColor" + '_' + (i + 1) + '_' + defaultThemeId);
                    logoUrl = mainPreferenceFileService.getStringValueFromSettingsPrefFile("logoUrl" + '_' + (i + 1) + '_' + defaultThemeId);

                    p = new ProgramsResultObject(applicationTitle, backgroundColor, backgroundGradientColor, drawable, defaultThemeId, i + 1);
                    pList.add(p);

                }

                message = Helper.createMessage(MessageIdentifiers.SUCCES, "A ProgramResultObject lista sikeresen létrejött!");

                if(ctpmw != null && ctpmw.get() != null && message != null && p != null) {
                    message.obj = pList;
                    ctpmw.get().sendResultToPresenter(message);
                }
            }

        }
        catch (Exception e){
            savingGlobalMessageHandling(-1, e.getMessage());
        }

        return null;
    }

    private void savingGlobalMessageHandling(int messageID, String exceptionMessage) {
        switch (messageID){
            case -1:{
                ApplicationLogger.logging(LogLevel.FATAL, exceptionMessage);
                sendMessageToAdapterHandler(ProcessSVGLogoEnums.EXCEPTION);
                break;
            }
        }
    }

    private void sendMessageToAdapterHandler(ProcessSVGLogoEnums processSVGLogoEnum){

        switch (processSVGLogoEnum){
            case EXCEPTION:{
                message = Helper.createMessage(MessageIdentifiers.EXCEPTION, "Hiba történt!");
                break;
            }
        }

        if(ctpmw != null && ctpmw.get() != null && message != null) {
            ctpmw.get().sendResultToPresenter(message);
        }
    }

    private enum ProcessSVGLogoEnums {
        EXCEPTION,
        SUCCES
    }
}
