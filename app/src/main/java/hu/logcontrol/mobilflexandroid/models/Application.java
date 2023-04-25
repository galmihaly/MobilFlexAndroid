package hu.logcontrol.mobilflexandroid.models;

import androidx.annotation.StringRes;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

import hu.logcontrol.mobilflexandroid.enums.LoginMode;

public class Application {

    @SerializedName("Id")
    private UUID id;

    @SerializedName("ApplicationName")
    private String applicationName;

    @SerializedName("ApplicationTitle")
    private String applicationTitle;

    @SerializedName("ApplicationLead")
    private String applicationLead;

    @SerializedName("ApplicationDescription")
    private String applicationDescription;

    @SerializedName("ApplicationVersion")
    private String applicationVersion;

    @SerializedName("ApplicationEnabledLoginFlag")
    private LoginMode applicationEnabledLoginFlag;

    @SerializedName("LoginUrl")
    private String loginUrl;

    @SerializedName("MainUrl")
    private String mainUrl;

    @SerializedName("SettingsUrl")
    private String settingsUrl;

    @SerializedName("HelpUrl")
    private String helpUrl;

    @SerializedName("ApplicationThemes")
    private List<ApplicationTheme> applicationThemeList;

    @SerializedName("DefaultThemeId")
    private int defaultThemeId;

    @SerializedName("LogoUrl")
    private String logoUrl;

    public Application(UUID id, String applicationName, String applicationTitle, String applicationLead, String applicationDescription, String applicationVersion, LoginMode applicationEnabledLoginFlag, String loginUrl, String mainUrl, String settingsUrl, String helpUrl, List<ApplicationTheme> applicationThemeList, int defaultThemeId, String logoUrl) {
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

    public LoginMode getApplicationEnabledLoginFlag() {
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
}
