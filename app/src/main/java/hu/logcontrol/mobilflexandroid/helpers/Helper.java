package hu.logcontrol.mobilflexandroid.helpers;

import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;

public class Helper {

    public static WindowSizeTypes[] getWindowSizes(AppCompatActivity appCompatActivity) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        appCompatActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        float widthDp = width / appCompatActivity.getResources().getDisplayMetrics().density;

        WindowSizeTypes[] widthWindowSizeClass = new WindowSizeTypes[2];

        if (widthDp < 600f) { widthWindowSizeClass[0] = WindowSizeTypes.COMPACT; }
        else if (widthDp < 840f) { widthWindowSizeClass[0] = WindowSizeTypes.MEDIUM; }
        else { widthWindowSizeClass[0] = WindowSizeTypes.EXPANDED; }

        float heightDp = height / appCompatActivity.getResources().getDisplayMetrics().density;

        if (heightDp < 600f) { widthWindowSizeClass[1] = WindowSizeTypes.COMPACT;}
        else if (heightDp < 900f) { widthWindowSizeClass[1] = WindowSizeTypes.MEDIUM; }
        else { widthWindowSizeClass[1] = WindowSizeTypes.EXPANDED; }

        return widthWindowSizeClass;
    }
}
