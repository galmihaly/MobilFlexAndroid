package hu.logcontrol.mobilflexandroid.interfaces;

import hu.logcontrol.mobilflexandroid.models.MainWebAPIResponseObject;
import hu.logcontrol.mobilflexandroid.models.SettingsWebAPIResponseObject;

public interface ISettingsWebAPIService {
    void onSuccesSettingsWebAPI(SettingsWebAPIResponseObject s);
    void onFailureSettingsWebAPI();
}
