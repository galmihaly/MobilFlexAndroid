package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;

public interface IWebViewActivity {
    void openViewByIntent(Intent intent);
    void changeStateAppbarLayout(String backgroundColor, String backgroundGradientColor);
    void getMessageFromPresenter(String message);
    void loadLoginWebAPIUrl(String loginWebAPIUrl);
    void changeMobileBarsColors(String statusBarColor, String navigationBarColor);
    void changeStateLoginButton(String buttonBackgroundColor, String buttonBackgroundGradientColor);
}
