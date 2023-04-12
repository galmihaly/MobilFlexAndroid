package hu.logcontrol.mobilflexandroid.interfaces;

import hu.logcontrol.mobilflexandroid.enums.ViewEnums;

public interface IMainActivityPresenter {
    void initTaskManager();
    void openActivityByEnum(ViewEnums viewEnum);
}
