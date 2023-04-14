package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;
import android.widget.ImageButton;

import java.util.List;

public interface ILoginActivity {
    void openViewByIntent(Intent intent);
    void getMessageFromPresenter(String message);
    void changeStateLoginTV(String message, String color);
    void changeStateLoginLogo(int logoID);
    void changeStateMainActivityCL(String startedGradientColor, String endedGradientColor);
    void sendCreatedButtonsToView(List<ImageButton> createdButtons);
}
