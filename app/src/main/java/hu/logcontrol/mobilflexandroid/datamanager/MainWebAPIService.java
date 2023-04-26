package hu.logcontrol.mobilflexandroid.datamanager;


import android.util.Log;

import com.google.gson.JsonObject;

import hu.logcontrol.mobilflexandroid.interfaces.IMainWebAPIService;
import hu.logcontrol.mobilflexandroid.interfaces.IRetrofitAPI;
import hu.logcontrol.mobilflexandroid.models.ResultObject;
import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainWebAPIService implements Callback<ResultObject>{

    private static MainWebAPIService mIsntance;
    private IMainWebAPIService iMainWebAPIService;
    private IRetrofitAPI iRetrofitAPI;

    private MainWebAPIService(String baseUrl, IMainWebAPIService iMainWebAPIService){

        this.iRetrofitAPI = new retrofit2.Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IRetrofitAPI.class);

        this.iMainWebAPIService = iMainWebAPIService;
    }

    public static MainWebAPIService getRetrofitInstance(String baseUrl, IMainWebAPIService iMainWebAPIService) {
        if (mIsntance == null) {
            mIsntance = new MainWebAPIService(baseUrl, iMainWebAPIService);
        }
        return mIsntance;
    }

    public void clearInstance(){
        if(mIsntance == null) return;
        mIsntance = null;
    }

    public void sendDeviceDetails(String id, String deviceId, String deviceName){
        if(iRetrofitAPI == null) return;
        if(iMainWebAPIService == null) return;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("deviceId", deviceId);
        jsonObject.addProperty("deviceName", deviceName);

        Call<ResultObject> userCall = iRetrofitAPI.postDeviceRequestObject(jsonObject);
        userCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
        if(response.isSuccessful() && response.body() != null && response.body().getDevice() != null){
            ResultObject resultObject = response.body();
            iMainWebAPIService.onSucces(resultObject);
        }
    }

    @Override
    public void onFailure(Call<ResultObject> call, Throwable t) {
        Log.e("onFailure", t.getMessage());
    }
}
