package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;

import java.util.List;

import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.models.ProgramsResultObject;

public interface IProgramsPresenter {
    void initAppDataManager();
    void getDownloadedLogoFromWeb(int applicationNumber);
    void openActivityByEnum(ViewEnums viewEnum, int defaultThemeId, int applicationId);
    void getDatasFromPresenter(List<ProgramsResultObject> programsResultObjectList);
}
