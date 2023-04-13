package hu.logcontrol.mobilflexandroid.interfaces;

import hu.logcontrol.mobilflexandroid.adapters.LanguagesSpinnerAdapter;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;

public interface IMainActivityPresenter {
    void initTaskManager();
    void openActivityByEnum(ViewEnums viewEnum);
    LanguagesSpinnerAdapter getSpinnerAdapter();
    void translateTextBySelectedLanguage(int languageID);
    void initPublicSharedPreferenceFiles();
    void saveLanguageToSettingsFile(int languageID);
    void initSettingsPreferenceFile();
    void saveStartedLanguageToSettingsFile(int selectedItem);
    int getCurrentLanguageFromSettingsFile();
}
