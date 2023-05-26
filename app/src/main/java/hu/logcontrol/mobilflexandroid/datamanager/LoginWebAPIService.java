package hu.logcontrol.mobilflexandroid.datamanager;

import android.util.Log;

import com.google.gson.JsonObject;

import hu.logcontrol.mobilflexandroid.interfaces.IRetrofitAPI;
import hu.logcontrol.mobilflexandroid.interfaces.ISettingsWebAPIService;
import hu.logcontrol.mobilflexandroid.models.SettingsWebAPIResponseObject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginWebAPIService implements Callback<SettingsWebAPIResponseObject> {

    private static LoginWebAPIService mIsntance;
    private ISettingsWebAPIService iSettingsWebAPIService;
    private IRetrofitAPI iRetrofitAPI;

    private static int appId = -1;
    private static int isFromPage = -1;

    private LoginWebAPIService(String baseUrl, ISettingsWebAPIService iSettingsWebAPIService){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        this.iRetrofitAPI = new retrofit2.Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(IRetrofitAPI.class);

        this.iSettingsWebAPIService = iSettingsWebAPIService;
    }

    public synchronized static LoginWebAPIService getInstance(String baseUrl, ISettingsWebAPIService iSettingsWebAPIService) {
        mIsntance = new LoginWebAPIService(baseUrl, iSettingsWebAPIService);
        return mIsntance;
    }

    public void clearInstance(){
        if(mIsntance == null) return;
        mIsntance = null;
    }

    public void sendLoginDetails(int loginModeEnum, String identifier, String authenticationToken, String data, boolean createSession, int applicationId, int isFromLoginPage, String serverName){
        if(iRetrofitAPI == null) return;
        if(iSettingsWebAPIService == null) return;

        appId = applicationId;
        isFromPage = isFromLoginPage;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("loginModeEnum", loginModeEnum);
        jsonObject.addProperty("identifier", identifier);
        jsonObject.addProperty("authenticationToken", authenticationToken);
        jsonObject.addProperty("data", data);
        jsonObject.addProperty("createSession", createSession);

        Call<SettingsWebAPIResponseObject> userCall = iRetrofitAPI.postLoginRequestObject(serverName, jsonObject);
        userCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<SettingsWebAPIResponseObject> call, Response<SettingsWebAPIResponseObject> response) {
        if(response.isSuccessful() && response.body() != null){
            iSettingsWebAPIService.onSuccesSettingsWebAPI(response.body(), appId, isFromPage);
        }
    }

    @Override
    public void onFailure(Call<SettingsWebAPIResponseObject> call, Throwable t) {
        Log.e("SettingsWebAPIService", t.getMessage());
        iSettingsWebAPIService.onFailureSettingsWebAPI();
    }
}
