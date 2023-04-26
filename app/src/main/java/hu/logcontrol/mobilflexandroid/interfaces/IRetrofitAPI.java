package hu.logcontrol.mobilflexandroid.interfaces;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.UUID;

import hu.logcontrol.mobilflexandroid.models.Device;
import hu.logcontrol.mobilflexandroid.models.DeviceRequest;
import hu.logcontrol.mobilflexandroid.models.Job;
import hu.logcontrol.mobilflexandroid.models.ResultObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IRetrofitAPI {

    @GET
    Call<Device> getDevices();

    @Headers("Content-Type: application/json")
    @POST("device")
    Call<ResultObject> postDeviceRequestObject(@Body JsonObject jsonObject);
}
