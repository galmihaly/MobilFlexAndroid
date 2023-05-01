package hu.logcontrol.mobilflexandroid.models;

import android.graphics.Bitmap;

import java.util.List;

public class ProgramsResultObject {

    private String titles;
    private String backgroundColors;
    private String backgroundGradientColors;
    private Bitmap logos;
    private int defaultThemeId;

    public ProgramsResultObject(String titles, String backgroundColors, String backgroundGradientColors, Bitmap logos, int defaultThemeId) {
        this.titles = titles;
        this.backgroundColors = backgroundColors;
        this.backgroundGradientColors = backgroundGradientColors;
        this.logos = logos;
        this.defaultThemeId = defaultThemeId;
    }

    public String getTitles() {
        return titles;
    }

    public String getBackgroundColors() {
        return backgroundColors;
    }

    public String getBackgroundGradientColors() {
        return backgroundGradientColors;
    }

    public Bitmap getLogos() {
        return logos;
    }

    public int getDefaultThemeId() {
        return defaultThemeId;
    }
}
