package hu.logcontrol.mobilflexandroid.models;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class ApplicationTheme {

    @SerializedName("Id")
    private int id;

    @SerializedName("ApplicationId")
    private UUID applicationId;

    @SerializedName("LogoUrl")
    private String logoUrl;

    @SerializedName("Name")
    private String name;

    @SerializedName("BackgroundColor")
    private String backgroundColor;

    @SerializedName("BackgroundGradientColor")
    private String backgroundGradientColor;

    @SerializedName("ForegroundColor")
    private String foregroundColor;

    @SerializedName("ButtonBackgroundColor")
    private String buttonBackgroundColor;

    @SerializedName("ButtonBackgroundGradientColor")
    private String buttonBackgroundGradientColor;

    @SerializedName("ButtonForegroundColor")
    private String buttonForegroundColor;

    @SerializedName("ControlColor")
    private String controlColor;

    public ApplicationTheme(int id, UUID applicationId, String logoUrl, String name, String backgroundColor, String backgroundGradientColor, String foregroundColor, String buttonBackgroundColor, String buttonBackgroundGradientColor, String buttonForegroundColor, String controlColor) {
        this.id = id;
        this.applicationId = applicationId;
        this.logoUrl = logoUrl;
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.backgroundGradientColor = backgroundGradientColor;
        this.foregroundColor = foregroundColor;
        this.buttonBackgroundColor = buttonBackgroundColor;
        this.buttonBackgroundGradientColor = buttonBackgroundGradientColor;
        this.buttonForegroundColor = buttonForegroundColor;
        this.controlColor = controlColor;
    }

    public int getId() {
        return id;
    }

    public UUID getApplicationId() {
        return applicationId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getName() {
        return name;
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
