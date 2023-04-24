package hu.logcontrol.mobilflexandroid.models;

import java.util.Date;
import java.util.List;

public class Device {

    private String deviceId;
    private String deviceName;
    private String active;
    private String lastDeviceLoginDate;
    private String comments;
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
}
