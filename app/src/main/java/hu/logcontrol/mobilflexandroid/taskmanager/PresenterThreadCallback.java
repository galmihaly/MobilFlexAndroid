package hu.logcontrol.mobilflexandroid.taskmanager;

import android.os.Message;

public interface PresenterThreadCallback {
    void sendMessageToHandler(Message message);
}
