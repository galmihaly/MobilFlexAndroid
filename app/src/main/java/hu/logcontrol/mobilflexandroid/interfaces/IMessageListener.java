package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;

public interface IMessageListener {
    void sendMessage(String message);
    void sendIntent(Intent intent);
}
