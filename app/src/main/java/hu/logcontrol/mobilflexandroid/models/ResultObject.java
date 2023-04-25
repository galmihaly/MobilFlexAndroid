package hu.logcontrol.mobilflexandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultObject {

    @SerializedName("resultCode")
    @Expose
    private String resultCode;

    @SerializedName("device")
    @Expose
    private Device device;

    public ResultObject(String resultCode, Device device) {
        this.resultCode = resultCode;
        this.device = device;
    }

    public String getResultCode() {
        return resultCode;
    }

    public Device getDevice() {
        return device;
    }

    @Override
    public String toString() {
        return "ResultObject{" +
                "resultCode='" + resultCode + '\'' +
                ", device='" + device + '\'' +
                '}';
    }
}
