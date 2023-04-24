package hu.logcontrol.mobilflexandroid.interfaces;

import hu.logcontrol.mobilflexandroid.adapters.LanguagesSpinnerAdapter;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;

public interface IMainActivityPresenter {
    void initTaskManager();
    void openActivityByEnum(ViewEnums viewEnum);
    LanguagesSpinnerAdapter getSpinnerAdapter();
    void translateTextBySelectedLanguage(String languageID);
//    void initPublicSharedPreferenceFiles();
    void saveLanguageToSettingsFile(String languageID);
//    void initSettingsPreferenceFile();
    int getCurrentLanguageFromSettingsFile();
    void initAppDataManager();
}
