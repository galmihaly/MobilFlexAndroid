package hu.logcontrol.mobilflexandroid.models;

import java.util.List;
import java.util.UUID;

import hu.logcontrol.mobilflexandroid.enums.LoginMode;

public class Application {

    private UUID id;
    private String applicationName;
    private String applicationTitle;
    private String applicationLead;
    private String applicationDescription;
    private String applicationVersion;
    private LoginMode applicationEnabledLoginFlag;
    private String loginUrl;
    private String mainUrl;
    private String settingsUrl;
    private String helpUrl;
    private List<ApplicationTheme> applicationThemeList;
    private int defaultThemeId;
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
