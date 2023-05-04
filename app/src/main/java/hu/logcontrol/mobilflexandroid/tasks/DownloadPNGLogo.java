package hu.logcontrol.mobilflexandroid.tasks;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
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

public class DownloadPNGLogo implements Callable {

    private WeakReference<CustomThreadPoolManager> ctpmw;
    private Context context;
    private MainPreferenceFileService mainPreferenceFileService;
    private int applicationsSize;

    private int defaultThemeId;
    private String applicationTitle;
    private String backgroundColor;
    private String backgroundGradientColor;
    private String logoUrl;
    private Drawable drawable;

    private int responseCode;
    private Bitmap bmp;
    private InputStream in;
    private FileWriter fileWriter;

    private final List<ProgramsResultObject> pList = new ArrayList<>();
    private ProgramsResultObject p;
    private Message message;


    public DownloadPNGLogo(Context context, MainPreferenceFileService mainPreferenceFileService, int applicationsSize) {
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
                    applicationTitle = mainPreferenceFileService.getStringValueFromSettingsPrefFile("applicationTitle" + '_' + (i + 1));
                    backgroundColor = mainPreferenceFileService.getStringValueFromSettingsPrefFile("backgroundColor" + '_' + (i + 1) + '_' + defaultThemeId);
                    backgroundGradientColor = mainPreferenceFileService.getStringValueFromSettingsPrefFile("backgroundGradientColor" + '_' + (i + 1) + '_' + defaultThemeId);
                    logoUrl = mainPreferenceFileService.getStringValueFromSettingsPrefFile("logoUrl" + '_' + (i + 1) + '_' + defaultThemeId);

                    Log.e("defaultThemeId", String.valueOf(defaultThemeId));
                    Log.e("applicationTitle", applicationTitle);
                    Log.e("backgroundColor", backgroundColor);
                    Log.e("backgroundGradientColor", backgroundGradientColor);
                    Log.e("logoUrl", logoUrl);

                    String fileName = getFileNameFromUrl();
                    mainPreferenceFileService.saveValueToSettingsPrefFile("logoName" + '_' + (i + 1) + '_' + defaultThemeId, fileName);

                    if(logoUrl != null && fileName != null){
                        URL url = new URL(logoUrl);
                        HttpURLConnection con = (HttpURLConnection)url.openConnection();
                        con.setDoInput(true);
                        con.connect();
                        responseCode = con.getResponseCode();
                        if(responseCode == HttpURLConnection.HTTP_OK)
                        {
                            in = con.getInputStream();
                            bmp = BitmapFactory.decodeStream(in);
                            drawable = new BitmapDrawable(context.getResources(), bmp);

                            File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "MobileFlexAndroid");
                            if(!dir.exists()){ dir.mkdir(); }

                            File file = new File(dir, fileName);
                            if(!file.exists()) file.createNewFile();

                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);

                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(bytes.toByteArray());

                            fos.close();
                            in.close();
                        }
                    }


                    p = new ProgramsResultObject(applicationTitle, backgroundColor, backgroundGradientColor, drawable, defaultThemeId, i + 1);
                    pList.add(p);
                }
            }

            message = Helper.createMessage(MessageIdentifiers.LOGO_DOWNLOAD_SUCCES, "A logó sikersen letöltődött!");

            if(ctpmw != null && ctpmw.get() != null && message != null && pList != null) {
                message.obj = pList;
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
