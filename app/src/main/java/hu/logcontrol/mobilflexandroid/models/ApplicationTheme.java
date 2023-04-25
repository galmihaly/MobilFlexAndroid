package hu.logcontrol.mobilflexandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class ApplicationTheme {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("applicationId")
    @Expose
    private UUID applicationId;

    @SerializedName("logoUrl")
    @Expose
    private String logoUrl;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("backgroundColor")
    @Expose
    private String backgroundColor;

    @SerializedName("backgroundGradientColor")
    @Expose
    private String backgroundGradientColor;

    @SerializedName("foregroundColor")
    @Expose
    private String foregroundColor;

    @SerializedName("buttonBackgroundColor")
    @Expose
    private String buttonBackgroundColor;

    @SerializedName("buttonBackgroundGradientColor")
    @Expose
    private String buttonBackgroundGradientColor;

    @SerializedName("buttonForegroundColor")
    @Expose
    private String buttonForegroundColor;

    @SerializedName("controlColor")
    @Expose
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

    @Override
    public String toString() {
        return "ApplicationTheme{" +
                "id=" + id +
                ", applicationId=" + applicationId +
                ", logoUrl='" + logoUrl + '\'' +
                ", name='" + name + '\'' +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", backgroundGradientColor='" + backgroundGradientColor + '\'' +
                ", foregroundColor='" + foregroundColor + '\'' +
                ", buttonBackgroundColor='" + buttonBackgroundColor + '\'' +
                ", buttonBackgroundGradientColor='" + buttonBackgroundGradientColor + '\'' +
                ", buttonForegroundColor='" + buttonForegroundColor + '\'' +
                ", controlColor='" + controlColor + '\'' +
                '}';
    }
}
