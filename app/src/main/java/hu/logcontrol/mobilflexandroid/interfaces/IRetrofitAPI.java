package hu.logcontrol.mobilflexandroid.interfaces;

import com.google.gson.JsonObject;

import hu.logcontrol.mobilflexandroid.models.Device;
import hu.logcontrol.mobilflexandroid.models.ResultObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IRetrofitAPI {
    @Headers("Content-Type: application/json")
    @POST("device")
    Call<ResultObject> postDeviceRequestObject(@Body JsonObject jsonObject);
}
