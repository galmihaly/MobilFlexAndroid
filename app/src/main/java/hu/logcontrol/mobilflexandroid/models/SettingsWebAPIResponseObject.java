package hu.logcontrol.mobilflexandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingsWebAPIResponseObject {

    @SerializedName("loginResponseCode")
    @Expose
    private int loginResponseCode;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("sessionId")
    @Expose
    private String sessionId;

    public SettingsWebAPIResponseObject(int loginResponseCode, String userId, String sessionId) {
        this.loginResponseCode = loginResponseCode;
        this.userId = userId;
        this.sessionId = sessionId;
    }

    public int getLoginResponseCode() {
        return loginResponseCode;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }
}
