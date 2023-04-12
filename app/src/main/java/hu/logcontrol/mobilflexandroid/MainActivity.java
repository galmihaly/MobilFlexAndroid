package hu.logcontrol.mobilflexandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;
import hu.logcontrol.mobilflexandroid.helpers.Helper;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivity;
import hu.logcontrol.mobilflexandroid.presenters.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initPresenter();
        initFunctions();
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* SettingsActivity fügvényei */

    private void getBaseValuesFromSettingsFile() {

    }

//    private void initSharedPreferenceFile() { settingsActivityPresenter.initSharedPreferenceFile(); }

    private void initFunctions() {

    }

    private void initPresenter() {
        mainActivityPresenter = new MainActivityPresenter(this, getApplicationContext());
        mainActivityPresenter.initTaskManager();
    }

    void initView(){
        WindowSizeTypes[] wst = Helper.getWindowSizes(this);
        int orientation = this.getResources().getConfiguration().orientation;

        // PDA -> 4,3 inch
        if(wst[0] == WindowSizeTypes.COMPACT && wst[1] == WindowSizeTypes.COMPACT){

//            setContentView(R.layout.activity_settings_portrait_mobile);

        }
        else if(wst[0] == WindowSizeTypes.COMPACT && wst[1] == WindowSizeTypes.MEDIUM){

            setContentView(R.layout.main_activity_mobile_portrait);

        }
        else if(wst[0] == WindowSizeTypes.MEDIUM && wst[1] == WindowSizeTypes.COMPACT){

//            setContentView(R.layout.activity_settings_landscape_mobile);

        }
    }
    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* IMainActivity interfész függvényei */
    @Override
    public void openViewByIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void getMessageFromPresenter(String message) {
        if(message == null) return;
        new Handler(Looper.getMainLooper()).post(() -> { Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); });
    }
    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
}