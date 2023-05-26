package hu.logcontrol.mobilflexandroid.interfaces;

import java.util.HashMap;
import java.util.List;

import hu.logcontrol.mobilflexandroid.adapters.LanguagesSpinnerAdapter;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;

public interface IMainActivityPresenter {
    void openActivityByEnum(ViewEnums viewEnum, int applicationNumber, int applicationId, int isFromLoginPage);
    LanguagesSpinnerAdapter getSpinnerAdapter();
    void translateTextBySelectedLanguage(String languageID);
    void saveLanguageToSettingsFile(String languageID);
    int getCurrentLanguageFromSettingsFile();
    void initAppDataManager();
    void startProgram(int delay);
    void processResultMessage(int resultCode);
    void sendMessageToView(String message);
}
