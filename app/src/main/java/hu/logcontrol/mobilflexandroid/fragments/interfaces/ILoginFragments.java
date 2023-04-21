package hu.logcontrol.mobilflexandroid.fragments.interfaces;

import android.content.Intent;

public interface ILoginFragments {
    void changeStateUserPassElements(String controlColor, String textColor);
    void changeStateLoginButton(String buttonBackgroundColor, String buttonBackgroundGradientColor, String buttonForeGroundColor, String buttonLabel);
    void changeTextInputElemenets(String changeText1, String changeText2);
    void openViewByIntent(Intent intent);
}
