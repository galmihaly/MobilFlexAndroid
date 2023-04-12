package hu.logcontrol.tekomalogin.taskmanager;

import android.os.Message;

public interface PresenterThreadCallback {
    void sendResultToPresenter(Message message);
}
