package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;

import hu.logcontrol.mobilflexandroid.enums.ViewEnums;

public interface IWebViewActivityPresenter {
    void openActivityByEnum(ViewEnums viewEnum, int applicationId, int applicationsSize);
    void getValuesFromSettingsPrefFile(int applicationId);
    void getURLfromSettings(int applicationId);
    void initAppDataManager();
    void initWebAPIServices();
    void getThemesSpinnerValues(int applicationId);

    void saveValueToCurrentThemeId(int applicationId, int themesId);
}
