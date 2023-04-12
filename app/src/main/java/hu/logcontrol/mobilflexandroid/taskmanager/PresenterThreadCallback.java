package hu.logcontrol.mobilflexandroid.taskmanager;

import android.os.Message;

public interface PresenterThreadCallback {
    void sendResultToPresenter(Message message);
}
