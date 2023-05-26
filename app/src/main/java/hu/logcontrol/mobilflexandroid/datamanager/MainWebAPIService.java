package hu.logcontrol.mobilflexandroid.datamanager;


import android.util.Log;

import com.google.gson.JsonObject;

import hu.logcontrol.mobilflexandroid.interfaces.IMainWebAPIService;
import hu.logcontrol.mobilflexandroid.interfaces.IRetrofitAPI;
import hu.logcontrol.mobilflexandroid.models.MainWebAPIResponseObject;
import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainWebAPIService implements Callback<MainWebAPIResponseObject>{

    private static MainWebAPIService mIsntance;
    private IMainWebAPIService iMainWebAPIService;
    private IRetrofitAPI iRetrofitAPI;

    private MainWebAPIService(String baseUrl, IMainWebAPIService iMainWebAPIService){

        this.iRetrofitAPI = new retrofit2.Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IRetrofitAPI.class);

        this.iMainWebAPIService = iMainWebAPIService;
    }

    public synchronized static MainWebAPIService getRetrofitInstance(String baseUrl, IMainWebAPIService iMainWebAPIService) {
        if (mIsntance == null) {
            mIsntance = new MainWebAPIService(baseUrl, iMainWebAPIService);
        }
        return mIsntance;
    }

    public void clearInstance(){
        if(mIsntance == null) return;
        mIsntance = null;
    }

    public void sendDeviceDetails(String id, String deviceId, String deviceName, String serverName){
        if(iRetrofitAPI == null) return;
        if(iMainWebAPIService == null) return;
        if(serverName == null) return;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("deviceId", deviceId);
        jsonObject.addProperty("deviceName", deviceName);

        Call<MainWebAPIResponseObject> userCall = iRetrofitAPI.postDeviceRequestObject(serverName ,jsonObject);
        userCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<MainWebAPIResponseObject> call, Response<MainWebAPIResponseObject> response) {
        if(response.isSuccessful() && response.body() != null){
            iMainWebAPIService.onSuccesMainWebAPI(response.body());
        }
    }

    @Override
    public void onFailure(Call<MainWebAPIResponseObject> call, Throwable t) {
        iMainWebAPIService.onFailureMainWebAPI(t.getMessage());
    }
}
