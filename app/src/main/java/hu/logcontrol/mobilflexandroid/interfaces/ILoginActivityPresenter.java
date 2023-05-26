package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.widget.ImageButton;

import java.util.List;

import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;

public interface ILoginActivityPresenter {
    void initButtonsByLoginModesNumber(WindowSizeTypes[] wsc, int applicationId);
    void setControlsValuesBySettings(int applicationId);
    void initAppDataManager();
    void getMessageFromAppDataManager(String message);
    void getCreatedButtonsFromAppDataManager(List<ImageButton> createdButtons);
    void openActivityByEnum(ViewEnums viewEnum, int applicationId);
    void getLogoImageFromExternalStorage(int applicationId);
    void initWebAPIServices();
    GradientDrawable[] getCurrentThemeForButton(int applicationId);
}
