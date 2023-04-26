package hu.logcontrol.mobilflexandroid.tasks;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

            URL url = new URL("https://api.mobileflex.hu/device"); //Enter URL here
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.connect();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", "7a0e0865-08b2-488a-8a20-c327ce28e59d");
            jsonObject.put("deviceId", "TESZT");
            jsonObject.put("deviceName", "s");

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(jsonObject.toString());
            wr.flush();
            wr.close();

            InputStream response = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Log.e("result", sb.toString());

        } catch (Exception e){
            Log.e("exception", e.getMessage());
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
