package hu.logcontrol.mobilflexandroid.fragments.interfaces;

import hu.logcontrol.mobilflexandroid.enums.FragmentTypes;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;

public interface ILoginFragmentsPresenter {
    void initTaskManager();
    void initSettingsPreferenceFile();
    void setControlsValuesBySettings();
    void initLanguageSharedPreferenceFiles();
    void setControlsTextBySettings(FragmentTypes fragmentType);
    void openActivityByEnum(ViewEnums viewEnum);
}
