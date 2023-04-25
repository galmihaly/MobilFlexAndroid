package hu.logcontrol.mobilflexandroid.models;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class DeviceRequest {

    @SerializedName("Id")
    private UUID id;

    @SerializedName("DeviceId")
    private String deviceId;

    @SerializedName("DeviceName")
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
