package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;
import android.widget.ImageButton;

import java.util.List;

import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;

public interface ILoginActivityPresenter {
    void initButtonsByLoginModesNumber(WindowSizeTypes[] wsc);
    void setControlsValuesBySettings();
    void initAppDataManager();
    void getMessageFromAppDataManager(String message);
    void getCreatedButtonsFromAppDataManager(List<ImageButton> createdButtons);
}
