package hu.logcontrol.mobilflexandroid.fragments.interfaces;

import hu.logcontrol.mobilflexandroid.enums.FragmentTypes;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;

public interface ILoginFragmentsPresenter {
    void initAppDataManager();
    void setControlsValuesBySettings(int defaultThemeId, int applicationId);
    void setControlsTextBySettings(FragmentTypes fragmentType);
    void openActivityByEnum(ViewEnums viewEnum, int applicationId, int defaultTheme);
    void initWebAPIServices();
    void startLogin(String identifier, String authenticationToken, int loginModeEnum);
}
