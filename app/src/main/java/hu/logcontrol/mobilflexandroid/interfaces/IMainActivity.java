package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;

public interface IMainActivity {
    void openViewByIntent(Intent intent);
    void getMessageFromPresenter(String message);
    void setTextToMessageTV(String message);
}
