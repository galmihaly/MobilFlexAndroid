package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;
import android.widget.ImageButton;

import java.util.List;

import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;

public interface ILoginActivityPresenter {
    void initTaskManager();
    void initSettingsPreferenceFile();
    void initButtonsByLoginModesNumber(WindowSizeTypes[] wsc);
    void sendMessageToView(String message);
    void sendCreatedButtonsToView(List<ImageButton> createdButtons);
    void saveSettingsToPreferences(Intent intent);
    void setControlsValuesBySettings();
}
