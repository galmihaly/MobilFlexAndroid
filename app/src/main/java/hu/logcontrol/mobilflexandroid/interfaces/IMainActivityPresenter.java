package hu.logcontrol.mobilflexandroid.interfaces;

import android.widget.ImageButton;

import java.util.List;

import hu.logcontrol.mobilflexandroid.adapters.LanguagesSpinnerAdapter;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;

public interface IMainActivityPresenter {
    void openActivityByEnum(ViewEnums viewEnum, int applicationNumber);
    LanguagesSpinnerAdapter getSpinnerAdapter();
    void translateTextBySelectedLanguage(String languageID);
    void saveLanguageToSettingsFile(String languageID);
    int getCurrentLanguageFromSettingsFile();
    void initAppDataManager();
    void startProgram();
    void sendBitmapLogoToPresenter(String resultMessage);
}
