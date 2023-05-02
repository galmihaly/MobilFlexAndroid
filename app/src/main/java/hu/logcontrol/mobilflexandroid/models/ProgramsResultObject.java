package hu.logcontrol.mobilflexandroid.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.List;

public class ProgramsResultObject {

    private String title;
    private String backgroundColor;
    private String backgroundGradientColor;
    private Bitmap logo;
    private int defaultThemeId;
    private int applicationId;

    public ProgramsResultObject(String title, String backgroundColor, String backgroundGradientColor, Bitmap logo, int defaultThemeId, int applicationId) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.backgroundGradientColor = backgroundGradientColor;
        this.logo = logo;
        this.defaultThemeId = defaultThemeId;
        this.applicationId = applicationId;
    }

    public String getTitle() {
        return title;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getBackgroundGradientColor() {
        return backgroundGradientColor;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public int getDefaultThemeId() {
        return defaultThemeId;
    }

    public int getApplicationId() {
        return applicationId;
    }
}
