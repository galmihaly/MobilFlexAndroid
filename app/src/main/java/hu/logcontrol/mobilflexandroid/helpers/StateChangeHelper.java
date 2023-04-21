package hu.logcontrol.mobilflexandroid.helpers;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class StateChangeHelper {

    public static void changeStateLoginButton(Button button, String buttonBackgroundColor, String buttonBackgroundGradientColor, String buttonForeGroundColor, String buttonLabel) {
        if(buttonBackgroundColor == null) return;
        if(buttonBackgroundGradientColor == null) return;
        if(buttonForeGroundColor == null) return;
        if(button == null) return;

        int[] colors = {Color.parseColor(buttonBackgroundColor),Color.parseColor(buttonBackgroundGradientColor)};
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
        g.setCornerRadius(60);

        button.setTextColor(Color.parseColor(buttonForeGroundColor));
        button.setText(buttonLabel);
        button.setBackground(g);
    }

    public static void changeStateLogoutButton(ImageButton logoutBut, String buttonBackgroundColor, String buttonBackgroundGradientColor) {
        if(logoutBut == null) return;
        if(buttonBackgroundColor == null) return;
        if(buttonBackgroundGradientColor == null) return;

        int[] colors = {Color.parseColor(buttonBackgroundColor),Color.parseColor(buttonBackgroundGradientColor)};
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
        g.setCornerRadius(20);

        logoutBut.setBackground(g);
    }

    public static void changeStateTextInputEditText(TextInputLayout til1, TextInputEditText tiet1, String controlColor, String textColor) {
        if(til1 == null) return;
        if(tiet1 == null) return;

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] { android.R.attr.state_focused}, // enabled
                new int[] {-android.R.attr.state_focused}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
        };

        int[] colors = new int[] {
                Color.parseColor(controlColor),
                Color.parseColor(controlColor),
                Color.parseColor(controlColor),
                Color.parseColor(controlColor)
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);

        til1.setBoxStrokeColorStateList(colorStateList);
        til1.setHintTextColor(ColorStateList.valueOf(Color.parseColor(textColor)));
        til1.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor(textColor)));

        tiet1.setTextColor(Color.parseColor(textColor));
    }
}
