package hu.logcontrol.mobilflexandroid.datamanager;

import android.util.Log;

import com.google.gson.JsonObject;

import hu.logcontrol.mobilflexandroid.interfaces.IRetrofitAPI;
import hu.logcontrol.mobilflexandroid.interfaces.ISettingsWebAPIService;
import hu.logcontrol.mobilflexandroid.models.SettingsWebAPIResponseObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsWebAPIService implements Callback<SettingsWebAPIResponseObject> {

    private static SettingsWebAPIService mIsntance;
    private ISettingsWebAPIService iSettingsWebAPIService;
    private IRetrofitAPI iRetrofitAPI;

    private SettingsWebAPIService(String baseUrl, ISettingsWebAPIService iSettingsWebAPIService){

        this.iRetrofitAPI = new retrofit2.Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IRetrofitAPI.class);

        this.iSettingsWebAPIService = iSettingsWebAPIService;
    }

    public static SettingsWebAPIService getRetrofitInstance(String baseUrl, ISettingsWebAPIService iSettingsWebAPIService) {
        if (mIsntance == null) {
            mIsntance = new SettingsWebAPIService(baseUrl, iSettingsWebAPIService);
        }
        return mIsntance;
    }

    public void clearInstance(){
        if(mIsntance == null) return;
        mIsntance = null;
    }

    public void sendLoginDetails(int loginModeEnum, String identifier, String authenticationToken, String data, boolean createSession){
        if(iRetrofitAPI == null) return;
        if(iSettingsWebAPIService == null) return;

        Log.e("loginModeEnum", String.valueOf(loginModeEnum));
        Log.e("identifier", identifier);
        Log.e("authenticationToken", authenticationToken);
        Log.e("data", data);
        Log.e("createSession", String.valueOf(createSession));

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("loginModeEnum", loginModeEnum);
        jsonObject.addProperty("identifier", identifier);
        jsonObject.addProperty("authenticationToken", authenticationToken);
        jsonObject.addProperty("data", data);
        jsonObject.addProperty("createSession", createSession);

        Call<SettingsWebAPIResponseObject> userCall = iRetrofitAPI.postLoginRequestObject(jsonObject);
        userCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<SettingsWebAPIResponseObject> call, Response<SettingsWebAPIResponseObject> response) {
        if(response.isSuccessful() && response.body() != null){
            SettingsWebAPIResponseObject s = response.body();
            iSettingsWebAPIService.onSuccesSettingsWebAPI(s);
        }
    }

    @Override
    public void onFailure(Call<SettingsWebAPIResponseObject> call, Throwable t) {
        Log.e("SettingsWebAPIService", t.getMessage());
        iSettingsWebAPIService.onFailureSettingsWebAPI();
    }
}
