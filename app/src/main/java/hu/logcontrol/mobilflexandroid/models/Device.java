package hu.logcontrol.mobilflexandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Device {

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("deviceName")
    @Expose
    private String deviceName;

    @SerializedName("active")
    @Expose
    private String active;

    @SerializedName("lastDeviceLoginDate")
    @Expose
    private String lastDeviceLoginDate;

    @SerializedName("comments")
    @Expose
    private String comments;

    @SerializedName("applications")
    @Expose
    private List<Application> applicationList;

    public Device(String deviceId, String deviceName, String active, String lastDeviceLoginDate, String comments, List<Application> applicationList) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.active = active;
        this.lastDeviceLoginDate = lastDeviceLoginDate;
        this.comments = comments;
        this.applicationList = applicationList;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getActive() {
        return active;
    }

    public String getLastDeviceLoginDate() {
        return lastDeviceLoginDate;
    }

    public String getComments() {
        return comments;
    }

    public List<Application> getApplicationList() {
        return applicationList;
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", active='" + active + '\'' +
                ", lastDeviceLoginDate='" + lastDeviceLoginDate + '\'' +
                ", comments='" + comments + '\'' +
                ", applicationList=" + applicationList +
                '}';
    }
}
