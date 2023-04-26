package hu.logcontrol.mobilflexandroid.models;

import androidx.annotation.StringRes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

import hu.logcontrol.mobilflexandroid.enums.LoginMode;

public class Application {

    @SerializedName("id")
    @Expose
    private UUID id;

    @SerializedName("applicationName")
    @Expose
    private String applicationName;

    @SerializedName("applicationTitle")
    @Expose
    private String applicationTitle;

    @SerializedName("applicationLead")
    @Expose
    private String applicationLead;

    @SerializedName("applicationDescription")
    @Expose
    private String applicationDescription;

    @SerializedName("applicationVersion")
    @Expose
    private String applicationVersion;

    @SerializedName("applicationEnabledLoginFlag")
    @Expose
    private int applicationEnabledLoginFlag;

    @SerializedName("loginUrl")
    @Expose
    private String loginUrl;

    @SerializedName("mainUrl")
    @Expose
    private String mainUrl;

    @SerializedName("settingsUrl")
    @Expose
    private String settingsUrl;

    @SerializedName("helpUrl")
    @Expose
    private String helpUrl;

    @SerializedName("applicationThemes")
    @Expose
    private List<ApplicationTheme> applicationThemeList;

    @SerializedName("defaultThemeId")
    @Expose
    private int defaultThemeId;

    @SerializedName("logoUrl")
    @Expose
    private String logoUrl;

    public Application(UUID id, String applicationName, String applicationTitle, String applicationLead, String applicationDescription, String applicationVersion, int applicationEnabledLoginFlag, String loginUrl, String mainUrl, String settingsUrl, String helpUrl, List<ApplicationTheme> applicationThemeList, int defaultThemeId, String logoUrl) {
        this.id = id;
        this.applicationName = applicationName;
        this.applicationTitle = applicationTitle;
        this.applicationLead = applicationLead;
        this.applicationDescription = applicationDescription;
        this.applicationVersion = applicationVersion;
        this.applicationEnabledLoginFlag = applicationEnabledLoginFlag;
        this.loginUrl = loginUrl;
        this.mainUrl = mainUrl;
        this.settingsUrl = settingsUrl;
        this.helpUrl = helpUrl;
        this.applicationThemeList = applicationThemeList;
        this.defaultThemeId = defaultThemeId;
        this.logoUrl = logoUrl;
    }

    public UUID getId() {
        return id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationTitle() {
        return applicationTitle;
    }

    public String getApplicationLead() {
        return applicationLead;
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public int getApplicationEnabledLoginFlag() {
        return applicationEnabledLoginFlag;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public String getSettingsUrl() {
        return settingsUrl;
    }

    public String getHelpUrl() {
        return helpUrl;
    }

    public List<ApplicationTheme> getApplicationThemeList() {
        return applicationThemeList;
    }

    public int getDefaultThemeId() {
        return defaultThemeId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", applicationName='" + applicationName + '\'' +
                ", applicationTitle='" + applicationTitle + '\'' +
                ", applicationLead='" + applicationLead + '\'' +
                ", applicationDescription='" + applicationDescription + '\'' +
                ", applicationVersion='" + applicationVersion + '\'' +
                ", applicationEnabledLoginFlag=" + applicationEnabledLoginFlag +
                ", loginUrl='" + loginUrl + '\'' +
                ", mainUrl='" + mainUrl + '\'' +
                ", settingsUrl='" + settingsUrl + '\'' +
                ", helpUrl='" + helpUrl + '\'' +
                ", applicationThemeList=" + applicationThemeList +
                ", defaultThemeId=" + defaultThemeId +
                ", logoUrl='" + logoUrl + '\'' +
                '}';
    }
}
