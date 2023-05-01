package hu.logcontrol.mobilflexandroid.presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import hu.logcontrol.mobilflexandroid.LoginActivity;
import hu.logcontrol.mobilflexandroid.datamanager.AppDataManager;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.interfaces.IProgramsActivity;
import hu.logcontrol.mobilflexandroid.interfaces.IProgramsPresenter;
import hu.logcontrol.mobilflexandroid.models.ProgramsResultObject;

public class ProgramsPresenter implements IProgramsPresenter {

    private IProgramsActivity iProgramsActivity;
    private Context context;

    private AppDataManager appDataManager;

    public ProgramsPresenter(IProgramsActivity iProgramsActivity, Context context) {
        this.iProgramsActivity = iProgramsActivity;
        this.context = context.getApplicationContext();
    }

    @Override
    public void openActivityByEnum(ViewEnums viewEnum, int defaultThemeId, int applicationId) {
        if(viewEnum == null) return;
        if(defaultThemeId == -1) return;
        if(applicationId == -1) return;
        if(iProgramsActivity == null) return;
        if(appDataManager == null) return;

        Intent intent = null;

        switch (viewEnum){
            case LOGIN_ACTIVITY:{
                intent = new Intent(context, LoginActivity.class);
                intent.putExtra("defaultThemeId", defaultThemeId);
                intent.putExtra("applicationId", applicationId);
                break;
            }
        }

        if(intent == null) return;
        if(iProgramsActivity != null) iProgramsActivity.openViewByIntent(intent);
    }

    @Override
    public void initAppDataManager() {
        appDataManager = new AppDataManager(context, this);
        appDataManager.createPreferenceFileService();
        appDataManager.initLanguagesPrefFile();
        appDataManager.initSettingsPrefFile();
        appDataManager.initBaseMessagesPrefFile();
        appDataManager.initTaskManager();
    }

    @Override
    public void getDataFromAppDataManager(Intent intent) {
        if(appDataManager == null) return;

        int applicationsSize = intent.getIntExtra("applicationsSize", -1);
        Log.e("applicationsSize", String.valueOf(applicationsSize));
        if(applicationsSize != -1) appDataManager.getDataFromAppDataManager(applicationsSize);
    }

    @Override
    public void sendDatasLogoToPresenter(List<ProgramsResultObject> programsResultObjects) {
        if(iProgramsActivity == null) return;
        if(programsResultObjects == null) return;
        iProgramsActivity.getProgramsCardElements(programsResultObjects);
    }
}