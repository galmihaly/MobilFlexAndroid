package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;
import android.widget.ImageButton;

import java.util.List;

import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;

public interface ILoginActivityPresenter {
    void initButtonsByLoginModesNumber(WindowSizeTypes[] wsc, int applicationId, int defaultThemeId);
    void setControlsValuesBySettings(int applicationId, int defaultThemeId);
    void initAppDataManager();
    void getMessageFromAppDataManager(String message);
    void getCreatedButtonsFromAppDataManager(List<ImageButton> createdButtons);
    void openActivityByEnum(ViewEnums viewEnum, int applicationId, int defaultTheme);
    void getLogoImageFromExternalStorage(int applicationId, int defaultThemeId);
    void initWebAPIServices();
}
