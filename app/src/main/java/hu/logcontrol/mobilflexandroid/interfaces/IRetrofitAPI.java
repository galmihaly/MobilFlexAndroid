package hu.logcontrol.mobilflexandroid.interfaces;

import com.google.gson.JsonObject;

import hu.logcontrol.mobilflexandroid.models.MainWebAPIResponseObject;
import hu.logcontrol.mobilflexandroid.models.SettingsWebAPIResponseObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IRetrofitAPI {
    @Headers("Content-Type: application/json")
    @POST("device")
    Call<MainWebAPIResponseObject> postDeviceRequestObject(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("Login")
    Call<SettingsWebAPIResponseObject> postLoginRequestObject(@Body JsonObject jsonObject);
}
