package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageButton;

import java.util.List;

public interface ILoginActivity {
    void openViewByIntent(Intent intent);
    void getMessageFromPresenter(String message);
    void changeStateLoginTV(String message, String color);
    void changeStateMainActivityCL(String startedGradientColor, String endedGradientColor);
    void changeStateApplicationLeadTextbox(String applicationDescription, String textColor);
    void changeStateApplicationTitleTextbox(String applicationName, String textColor);
    void sendCreatedButtonsToView(List<ImageButton> createdButtons);
    void changeMobileBarsColors(String statusBarColor, String navigationBarColor);
    void getLogoFromPresenter(Bitmap bitmap);
    void changeBackButtonColors(String buttonBackgroundColor, String buttonBackgroundGradientColor);
}
