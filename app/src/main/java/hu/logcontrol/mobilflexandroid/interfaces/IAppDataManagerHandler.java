package hu.logcontrol.mobilflexandroid.interfaces;

import android.graphics.Bitmap;
import android.widget.ImageButton;

import java.util.List;

import hu.logcontrol.mobilflexandroid.models.ProgramsResultObject;

public interface IAppDataManagerHandler {
    void sendMessageToPresenter(String message);
    void sendCreatedButtonsToPresenter(List<ImageButton> createdButtons);
    void sendDowloadedLogoToPresenter(List<ProgramsResultObject> logo);
}
