package hu.logcontrol.mobilflexandroid.models;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SettingsObject {

    private String deviceIdentifier;
    private String deviceName;
    private UUID applicationIdentifier;
    private String applicationName;
    private String applicationTitle;
    private String applicationLead;
    private String applicationDescription;
    private String applicationVersion;
    private int applicationEnabledLoginFlag;
    private String logoImageUrl;
    private String loginWebApiUrl;
    private String mainWebApiUrl;
    private String settingsWebApiUrl;
    private String helpWebApiUrl;
    private List<String> languages;
    private List<String> wordCodes;
    private HashMap<String, String> translations;
    private String backgroundColor;
    private String backgroundGradientColor;
    private String foregroundColor;
    private String buttonBackgroundColor;
    private String buttonBackgroundGradientColor;
    private String buttonForegroundColor;
    private String controlColor;

    public SettingsObject(String deviceIdentifier, String deviceName, UUID applicationIdentifier, String applicationName, String applicationTitle, String applicationLead, String applicationDescription, String applicationVersion, int applicationEnabledLoginFlag, String logoImageUrl, String loginWebApiUrl, String mainWebApiUrl, String settingsWebApiUrl, String helpWebApiUrl, List<String> languages, List<String> wordCodes, HashMap<String, String> translations, String backgroundColor, String backgroundGradientColor, String foregroundColor, String buttonBackgroundColor, String buttonBackgroundGradientColor, String buttonForegroundColor, String controlColor) {
        this.deviceIdentifier = deviceIdentifier;
        this.deviceName = deviceName;
        this.applicationIdentifier = applicationIdentifier;
        this.applicationName = applicationName;
        this.applicationTitle = applicationTitle;
        this.applicationLead = applicationLead;
        this.applicationDescription = applicationDescription;
        this.applicationVersion = applicationVersion;
        this.applicationEnabledLoginFlag = applicationEnabledLoginFlag;
        this.logoImageUrl = logoImageUrl;
        this.loginWebApiUrl = loginWebApiUrl;
        this.mainWebApiUrl = mainWebApiUrl;
        this.settingsWebApiUrl = settingsWebApiUrl;
        this.helpWebApiUrl = helpWebApiUrl;
        this.languages = languages;
        this.wordCodes = wordCodes;
        this.translations = translations;
        this.backgroundColor = backgroundColor;
        this.backgroundGradientColor = backgroundGradientColor;
        this.foregroundColor = foregroundColor;
        this.buttonBackgroundColor = buttonBackgroundColor;
        this.buttonBackgroundGradientColor = buttonBackgroundGradientColor;
        this.buttonForegroundColor = buttonForegroundColor;
        this.controlColor = controlColor;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public UUID getApplicationIdentifier() {
        return applicationIdentifier;
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

    public String getLogoImageUrl() {
        return logoImageUrl;
    }

    public String getLoginWebApiUrl() {
        return loginWebApiUrl;
    }

    public String getMainWebApiUrl() {
        return mainWebApiUrl;
    }

    public String getSettingsWebApiUrl() {
        return settingsWebApiUrl;
    }

    public String getHelpWebApiUrl() {
        return helpWebApiUrl;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public List<String> getWordCodes() {
        return wordCodes;
    }

    public HashMap<String, String> getTranslations() {
        return translations;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getBackgroundGradientColor() {
        return backgroundGradientColor;
    }

    public String getForegroundColor() {
        return foregroundColor;
    }

    public String getButtonBackgroundColor() {
        return buttonBackgroundColor;
    }

    public String getButtonBackgroundGradientColor() {
        return buttonBackgroundGradientColor;
    }

    public String getButtonForegroundColor() {
        return buttonForegroundColor;
    }

    public String getControlColor() {
        return controlColor;
    }
}
