package hu.logcontrol.mobilflexandroid.interfaces;

import android.content.Intent;
import android.graphics.Bitmap;

import java.util.List;

import hu.logcontrol.mobilflexandroid.models.ProgramsResultObject;

public interface IProgramsActivity {
    void openViewByIntent(Intent intent);
    void getMessageFromPresenter(String message);
    void getFileNameList(List<String> filenNames);
    void getDatasFromPresenter(List<ProgramsResultObject> programsResultObjectList);
}
