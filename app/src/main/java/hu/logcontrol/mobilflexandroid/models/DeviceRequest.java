package hu.logcontrol.mobilflexandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class DeviceRequest {

    private UUID id;
    private String deviceId;
    private String deviceName;

    public DeviceRequest(UUID id, String deviceId, String deviceName) {
        this.id = id;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }

    public UUID getId() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }
}
