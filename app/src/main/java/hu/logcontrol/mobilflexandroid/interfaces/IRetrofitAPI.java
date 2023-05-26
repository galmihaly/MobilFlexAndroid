package hu.logcontrol.mobilflexandroid.interfaces;

import com.google.gson.JsonObject;

import hu.logcontrol.mobilflexandroid.models.MainWebAPIResponseObject;
import hu.logcontrol.mobilflexandroid.models.SettingsWebAPIResponseObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRetrofitAPI {
    @Headers("Content-Type: application/json")
    @POST("{apiName}")
    Call<MainWebAPIResponseObject> postDeviceRequestObject(@Path("apiName") String apiName, @Body JsonObject jsonObject);

    // TODO: meg kell oldani azt, hogy paraméterként kapja meg a api nevet (szét kell bontani az URL sztringet)

    @Headers("Content-Type: application/json")
    @POST("{apiName}")
    Call<SettingsWebAPIResponseObject> postLoginRequestObject(@Path("apiName") String apiName, @Body JsonObject jsonObject);
}
