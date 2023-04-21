package hu.logcontrol.mobilflexandroid.interfaces;

import hu.logcontrol.mobilflexandroid.enums.ViewEnums;

public interface IWebViewActivityPresenter {
    void initTaskManager();
    void openActivityByEnum(ViewEnums viewEnum);
    void initSharedPreferenceFile();
    void getValuesFromSettingsPrefFile();
    void getURLfromSettings();
}
