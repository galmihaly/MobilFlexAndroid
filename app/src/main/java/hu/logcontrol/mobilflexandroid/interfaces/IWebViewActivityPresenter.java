package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;

import hu.logcontrol.mobilflexandroid.enums.ViewEnums;

public interface IWebViewActivityPresenter {
    void openActivityByEnum(ViewEnums viewEnum, Intent backActivityIntent);
    void getValuesFromSettingsPrefFile(Intent intent);
    void getURLfromSettings(Intent intent);
    void initAppDataManager();
}
