package hu.logcontrol.mobilflexandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import hu.logcontrol.mobilflexandroid.enums.LoginModeIdentifiers;

public class SettingsWebAPIResponseObject {

    @SerializedName("loginResponseCode")
    @Expose
    private int loginModeResponseCode;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("sessionId")
    @Expose
    private String sessionId;

    public SettingsWebAPIResponseObject(int loginModeResponseCode, String userId, String sessionId) {
        this.loginModeResponseCode = loginModeResponseCode;
        this.userId = userId;
        this.sessionId = sessionId;
    }

    public int getLoginModeResponseCode() {
        return loginModeResponseCode;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }
}
