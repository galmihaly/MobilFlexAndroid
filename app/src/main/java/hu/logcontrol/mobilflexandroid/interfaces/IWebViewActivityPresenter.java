package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;

import hu.logcontrol.mobilflexandroid.enums.ViewEnums;

public interface IWebViewActivityPresenter {
    void openActivityByEnum(ViewEnums viewEnum, int applicationId, int defaultThemeId);
    void getValuesFromSettingsPrefFile(int applicationId, int defaultThemeId);
    void getURLfromSettings(int applicationId);
    void initAppDataManager();
    void initWebAPIServices();
}
