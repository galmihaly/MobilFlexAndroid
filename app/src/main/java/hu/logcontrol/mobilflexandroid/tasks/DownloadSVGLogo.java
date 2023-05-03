package hu.logcontrol.mobilflexandroid.tasks;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import java.net.HttpURLConnection;

import hu.logcontrol.mobilflexandroid.datamanager.MainPreferenceFileService;
import hu.logcontrol.mobilflexandroid.enums.MessageIdentifiers;
import hu.logcontrol.mobilflexandroid.helpers.Helper;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.models.ProgramsResultObject;
import hu.logcontrol.mobilflexandroid.taskmanager.CustomThreadPoolManager;

public class DownloadSVGLogo implements Callable {

    private WeakReference<CustomThreadPoolManager> ctpmw;
    private Context context;
    private MainPreferenceFileService mainPreferenceFileService;
    private int applicationsSize;

    private int defaultThemeId;
    private String logoUrl;

    private final List<String> fileList = new ArrayList<>();
    private ProgramsResultObject p;
    private Message message;


    public DownloadSVGLogo(Context context, MainPreferenceFileService mainPreferenceFileService, int applicationsSize) {
        this.context = context.getApplicationContext();
        this.mainPreferenceFileService = mainPreferenceFileService;
        this.applicationsSize = applicationsSize;
    }

    public void setCustomThreadPoolManager(CustomThreadPoolManager customThreadPoolManager) {
        this.ctpmw = new WeakReference<>(customThreadPoolManager);
    }

    // TODO ez itt még nem teljesen működik, majd meg kell csinálni
    // sokszor jön létre a fájl a Environment.getExternalStorageDirectory().getPath() útvonalon

    @SuppressLint("CheckResult")
    @Override
    public Object call() throws Exception {
        try {
            if (Thread.interrupted()) throw new InterruptedException();

            if(mainPreferenceFileService != null){
                for (int i = 0; i < applicationsSize; i++) {

                    defaultThemeId = mainPreferenceFileService.getIntValueFromSettingsPrefFile("defaultThemeId" + '_' + (i + 1));
                    logoUrl = mainPreferenceFileService.getStringValueFromSettingsPrefFile("logoUrl" + '_' + (i + 1) + '_' + defaultThemeId);

                    String filname = getFileNameFromUrl();

                    if(filname != null){

                        boolean isConnected = isConnectedToServer(logoUrl);
                        if(isConnected){
                            File dir = new File(Environment.getExternalStorageState() + File.separator + "WasteProgram", filname);

                            if(!dir.exists()){
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(logoUrl));

                                Log.e("path", dir.getAbsolutePath());
                                Log.e("filename", dir.getName());
                                Log.e("getPath", dir.getPath());
                                Log.e("getAbsolutePath", dir.getAbsoluteFile().getAbsolutePath());

                                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                                request.setDestinationInExternalPublicDir(File.separator + "WasteProgram", filname);

                                DownloadManager manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
                                manager.enqueue(request);
                            }
                        }

                        fileList.add(filname);
                    }
                }
            }

            message = Helper.createMessage(MessageIdentifiers.LOGO_DOWNLOAD_SUCCES, "A logó sikersen letöltődött!");

            if(ctpmw != null && ctpmw.get() != null && message != null && fileList != null) {
                message.obj = fileList;
                ctpmw.get().sendResultToPresenter(message);
            }

        }
        catch (Exception e){
            savingGlobalMessageHandling(-1, e.getMessage());
        }

        return null;
    }

    private String getFileNameFromUrl() {
        if(logoUrl == null) return null;

        String[] s = logoUrl.split("/");
        return s[s.length - 1];
    }

    public boolean isConnectedToServer(String stringUrl) {
        if(logoUrl == null) return false;
        if(stringUrl == null) return false;

        boolean isConnected = false;

        try{
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if(connection.getResponseCode() == 200){
                isConnected = true;
            }
            else if(connection.getResponseCode() == 404){
                isConnected = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            isConnected = false;
        }

        return isConnected;
    }

    private void savingGlobalMessageHandling(int messageID, String exceptionMessage) {
        switch (messageID){
            case -1:{
                ApplicationLogger.logging(LogLevel.FATAL, exceptionMessage);
                sendMessageToAdapterHandler(DownloadSVGLogoEnums.EXCEPTION);
                break;
            }
        }
    }

    private void sendMessageToAdapterHandler(DownloadSVGLogoEnums downloadSVGLogoEnum){

        switch (downloadSVGLogoEnum){
            case EXCEPTION:{
                message = Helper.createMessage(MessageIdentifiers.EXCEPTION, "Hiba történt!");
                break;
            }
        }

        if(ctpmw != null && ctpmw.get() != null && message != null) {
            ctpmw.get().sendResultToPresenter(message);
        }
    }

    private enum DownloadSVGLogoEnums {
        EXCEPTION,
        LOGO_DOWNLOAD_SUCCES
    }
}
