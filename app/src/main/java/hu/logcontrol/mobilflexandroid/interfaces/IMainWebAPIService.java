package hu.logcontrol.mobilflexandroid.interfaces;

import com.google.gson.JsonElement;

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

public interface IMainWebAPIService {

    @GET
    Call<Device> getDevices();

//    @FormUrlEncoded
//    @POST("/api/users")
//    Call<Job> postDeviceRequestObject(@Field("name") String id, @Field("job") String deviceId);

    @Headers("Content-Type: application/json")
    @POST("device")
    Call<ResultObject> postDeviceRequestObject(@Body JSONObject jsonObject);
}
