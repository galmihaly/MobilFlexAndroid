package hu.logcontrol.mobilflexandroid.datamanager;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.List;
import java.util.UUID;

import hu.logcontrol.mobilflexandroid.interfaces.IMainWebAPIService;
import hu.logcontrol.mobilflexandroid.models.Application;
import hu.logcontrol.mobilflexandroid.models.ApplicationTheme;
import hu.logcontrol.mobilflexandroid.models.Device;
import hu.logcontrol.mobilflexandroid.models.DeviceRequest;
import hu.logcontrol.mobilflexandroid.models.Job;
import hu.logcontrol.mobilflexandroid.models.ResultObject;
import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainWebAPIService {

    private static MainWebAPIService mIsntance;

    private IMainWebAPIService iMainWebAPIService;

    private MainWebAPIService(String baseUrl){

        this.iMainWebAPIService = new retrofit2.Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IMainWebAPIService.class);
    }

    public static MainWebAPIService getRetrofitInstance(String baseUrl) {
        if (mIsntance == null) {
            mIsntance = new MainWebAPIService(baseUrl);
        }
        return mIsntance;
    }

    public void clearInstance(){
        if(mIsntance == null) return;
        mIsntance = null;
    }

    public void sendDeviceRequestObject(){
        if(iMainWebAPIService == null) return;
//        DeviceRequest d = new DeviceRequest(UUID.fromString("7a0e0865-08b2-488a-8a20-c327ce28e59d"), "TESZT", "bármi");

//        Log.d("sendDeviceRequestObject",  "belétem ide is");

        JSONObject jo = new JSONObject();
        try {
            jo.put("id", UUID.fromString("7a0e0865-08b2-488a-8a20-c327ce28e59d"));
            jo.put("deviceId", "TESZT");
            jo.put("deviceName", "bármi");

            Log.e("json", "\n" + jo.toString(4));

            Call<ResultObject> userCall = iMainWebAPIService.postDeviceRequestObject(jo);
            userCall.enqueue(new Callback<ResultObject>() {
                @Override
                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                    Log.e("response", response.body().toString());
                    Log.e("isSuccesful", response.body().getDevice().toString());

                    List<Application> s = response.body().getDevice().getApplicationList();
                    for (int i = 0; i < s.size(); i++) {
                        Log.e("isSuccesful", s.get(i).toString());

                        List<ApplicationTheme> t = s.get(i).getApplicationThemeList();

                        for (int j = 0; j < t.size(); j++) {
                            Log.e("isSuccesful", t.get(i).toString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultObject> call, Throwable t) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
