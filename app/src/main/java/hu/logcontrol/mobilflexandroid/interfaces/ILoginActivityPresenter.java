package hu.logcontrol.mobilflexandroid.interfaces;

import android.widget.ImageButton;

import java.util.List;

import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;

public interface ILoginActivityPresenter {
    void initTaskManager();
    void initTextsByCurrentValue();
    void initSettingsPreferenceFile();
    void initButtonsByLoginModesNumber(int loginModesNumber, WindowSizeTypes[] wsc, int[] colors);
    void sendMessageToView(String message);
    void sendCreatedButtonsToView(List<ImageButton> createdButtons);
}
