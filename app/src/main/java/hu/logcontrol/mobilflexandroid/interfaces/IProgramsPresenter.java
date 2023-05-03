package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;

import java.util.List;

import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.models.ProgramsResultObject;

public interface IProgramsPresenter {
    void initAppDataManager();
    void getDownloadedLogoFromWeb(int applicationNumber);
    void sendFileNamesToView(List<String> fileNames);
    void openActivityByEnum(ViewEnums viewEnum, int defaultThemeId, int applicationId);
    void getDrawablesFromSVGFiles(List<String> fileNames, int applicationNumber);
    void getDatasFromPresenter(List<ProgramsResultObject> programsResultObjectList);
}
