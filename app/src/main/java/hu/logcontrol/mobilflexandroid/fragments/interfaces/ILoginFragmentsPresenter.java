package hu.logcontrol.mobilflexandroid.fragments.interfaces;

import hu.logcontrol.mobilflexandroid.enums.FragmentTypes;

public interface ILoginFragmentsPresenter {
    void initTaskManager();
    void initSettingsPreferenceFile();
    void setControlsValuesBySettings();
    void initLanguageSharedPreferenceFiles();
    void setControlsTextBySettings(FragmentTypes fragmentType);
}
