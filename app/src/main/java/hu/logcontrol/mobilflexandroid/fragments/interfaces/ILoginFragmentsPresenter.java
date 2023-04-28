package hu.logcontrol.mobilflexandroid.fragments.interfaces;

import hu.logcontrol.mobilflexandroid.enums.FragmentTypes;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;

public interface ILoginFragmentsPresenter {
    void initAppDataManager();
    void setControlsValuesBySettings();
    void setControlsTextBySettings(FragmentTypes fragmentType);
    void openActivityByEnum(ViewEnums viewEnum);
}
