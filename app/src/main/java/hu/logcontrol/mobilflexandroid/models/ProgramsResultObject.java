package hu.logcontrol.mobilflexandroid.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.List;

public class ProgramsResultObject {

    private String title;
    private String backgroundColor;
    private String backgroundGradientColor;
    private Drawable logo;
    private int applicationId;
    private int applicationEnabledLoginFlag;
    private int applicationsSize;

    public ProgramsResultObject(String title, String backgroundColor, String backgroundGradientColor, Drawable logo, int applicationId, int applicationEnabledLoginFlag, int applicationsSize) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.backgroundGradientColor = backgroundGradientColor;
        this.logo = logo;
        this.applicationId = applicationId;
        this.applicationEnabledLoginFlag = applicationEnabledLoginFlag;
        this.applicationsSize = applicationsSize;
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

    public Drawable getLogo() {
        return logo;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public int getApplicationEnabledLoginFlag() {
        return applicationEnabledLoginFlag;
    }

    public int getApplicationsSize() {
        return applicationsSize;
    }
}
