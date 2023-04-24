package hu.logcontrol.mobilflexandroid.interfaces;

import hu.logcontrol.mobilflexandroid.adapters.LanguagesSpinnerAdapter;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;

public interface IMainActivityPresenter {
    void openActivityByEnum(ViewEnums viewEnum);
    LanguagesSpinnerAdapter getSpinnerAdapter();
    void translateTextBySelectedLanguage(String languageID);
    void saveLanguageToSettingsFile(String languageID);
    int getCurrentLanguageFromSettingsFile();
    void initAppDataManager();
    void startProgram();
    void sendResultToPresenter(String resultMessage);
}
