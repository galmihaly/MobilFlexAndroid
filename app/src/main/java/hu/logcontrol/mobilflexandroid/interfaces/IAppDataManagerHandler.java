package hu.logcontrol.mobilflexandroid.interfaces;

import android.widget.ImageButton;

import java.util.List;

public interface IAppDataManagerHandler {
    void sendResultFromWebAPICallingTask(String weakReferenceNotification);
    void sendMessageToLoginView(String message);
    void sendCreatedButtonsToLoginView(List<ImageButton> createdButtons);
}
