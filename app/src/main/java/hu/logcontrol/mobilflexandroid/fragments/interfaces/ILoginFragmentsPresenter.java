package hu.logcontrol.mobilflexandroid.fragments.interfaces;

import hu.logcontrol.mobilflexandroid.enums.FragmentTypes;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;

public interface ILoginFragmentsPresenter {
    void initAppDataManager();
    void setControlsValuesBySettings(int applicationId);
    void setControlsTextBySettings(FragmentTypes fragmentType);
    void openActivityByEnum(ViewEnums viewEnum, int applicationId, int isFromLoginPage);
    void initWebAPIServices(int applicationId);
    void startLogin(String identifier, String authenticationToken, int loginModeEnum, int applicationId, int isFromLoginPage);
    void sendMessageToView(String message);
    void processResultMessage(int responseCode, int applicationId, int isFromLoginPage);
}
