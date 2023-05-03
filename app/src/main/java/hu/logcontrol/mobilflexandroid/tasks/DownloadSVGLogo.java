package hu.logcontrol.mobilflexandroid.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.Log;

//import com.larvalabs.svgandroid.SVG;
//import com.larvalabs.svgandroid.SVGParser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class DownloadSVGLogo implements Callable {

    private WeakReference<CustomThreadPoolManager> ctpmw;
    private Context context;
    private MainPreferenceFileService mainPreferenceFileService;
    private int applicationsSize;

    private int defaultThemeId;
    private String applicationTitle;
    private String backgroundColor;
    private String backgroundGradientColor;
    private String logoUrl;

    private final List<ProgramsResultObject> pList = new ArrayList<>();

    private ProgramsResultObject p;

    private Message message;
    private Bitmap drawable;

    public DownloadSVGLogo(Context context, MainPreferenceFileService mainPreferenceFileService, int applicationsSize) {
        this.context = context.getApplicationContext();
        this.mainPreferenceFileService = mainPreferenceFileService;
        this.applicationsSize = applicationsSize;
    }

    public void setCustomThreadPoolManager(CustomThreadPoolManager customThreadPoolManager) {
        this.ctpmw = new WeakReference<>(customThreadPoolManager);
    }

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
                    logoUrl = mainPreferenceFileService.getStringValueFromSettingsPrefFile("logoUrl" + '_' + (i + 1));

                    Log.e("defaultThemeId" + '_' + (i + 1),  " " + String.valueOf(defaultThemeId));
                    Log.e("applicationTitle" + '_' + (i + 1), applicationTitle);
                    Log.e("backgroundColor" + '_' + (i + 1) + '_' + defaultThemeId, backgroundColor);
                    Log.e("backgroundGradientColor" + '_' + (i + 1) + '_' + defaultThemeId, backgroundGradientColor);
                    Log.e("logoUrl" + '_' + (i + 1) + '_' + defaultThemeId, logoUrl);

                    logoUrl = "https://html.com/wp-content/uploads/flamingo.jpg";

                    URL url = new URL(logoUrl);

                    HttpURLConnection connection = null;
                    try{
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();

                        if(connection.getResponseCode() == 200){

//                            InputStream inputStream = connection.getInputStream();
//                            Log.e("inputstream", String.valueOf(inputStream.available()));
//                            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//
//                            SVG svg = SVGParser. getSVGFromInputStream(bufferedInputStream);
//                            drawable = svg.createPictureDrawable();

//                            bitmap = BitmapFactory.decodeStream(bufferedInputStream);
//                            Log.e("a", String.valueOf(bitmap.getHeight()));

                            InputStream inputStream = connection.getInputStream();
                            Log.e("inputstream", String.valueOf(inputStream.available()));
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                            drawable = BitmapFactory.decodeStream(bufferedInputStream);

                            inputStream.close();

                            p = new ProgramsResultObject(applicationTitle, backgroundColor, backgroundGradientColor, drawable, defaultThemeId, i + 1);
                            pList.add(p);
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }

            message = Helper.createMessage(MessageIdentifiers.LOGO_DOWNLOAD_SUCCES, "A logó sikersen letöltődött!");

            if(ctpmw != null && ctpmw.get() != null && message != null && p != null) {
                message.obj = pList;
                ctpmw.get().sendResultToPresenter(message);
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
