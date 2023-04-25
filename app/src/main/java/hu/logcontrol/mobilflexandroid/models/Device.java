package hu.logcontrol.mobilflexandroid.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Device {

    @SerializedName("DeviceId")
    private String deviceId;

    @SerializedName("DeviceName")
    private String deviceName;

    @SerializedName("Active")
    private String active;

    @SerializedName("LastDeviceLoginDate")
    private String lastDeviceLoginDate;

    @SerializedName("Comments")
    private String comments;

    @SerializedName("Applications")
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
