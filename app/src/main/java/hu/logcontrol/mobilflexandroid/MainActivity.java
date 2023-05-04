package hu.logcontrol.mobilflexandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import hu.logcontrol.mobilflexandroid.adapters.LanguagesSpinnerAdapter;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;
import hu.logcontrol.mobilflexandroid.helpers.Helper;
import hu.logcontrol.mobilflexandroid.interfaces.IMainActivity;
import hu.logcontrol.mobilflexandroid.presenters.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private MainActivityPresenter mainActivityPresenter;

    private TextView messageTV;
    private Spinner languageSelector;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        startWrite();
        initPresenter();
        initAppDataManager();
        initWebAPIServices();
        saveBaseUrl();
        initLanguagesSpinner();
        initFunctions();
    }

    public void startWrite() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("", "Jelenleg rendelkezik írási joggal!");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 786);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Engedély elutasítva!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (requestCode == 786) {
            Toast.makeText(this, "Engedély elfogadva!", Toast.LENGTH_SHORT).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* SettingsActivity fügvényei */

    private void initAppDataManager() {
        if(mainActivityPresenter == null) return;
        mainActivityPresenter.initAppDataManager();
    }

    private void initWebAPIServices() {
        if(mainActivityPresenter == null) return;
        mainActivityPresenter.initWebAPIServices();
    }

    private void initLanguagesSpinner() {
        if(languageSelector != null) {

            LanguagesSpinnerAdapter adapter = mainActivityPresenter.getSpinnerAdapter();

            languageSelector.setAdapter(adapter);

            int currentLanguage = mainActivityPresenter.getCurrentLanguageFromSettingsFile();
            if(currentLanguage != -1) languageSelector.setSelection(currentLanguage);

            languageSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    int languageID = Integer.parseInt(parent.getItemAtPosition(position).toString());

                    switch (languageID){
                        case R.drawable.ic_hu2:{
                            mainActivityPresenter.saveLanguageToSettingsFile("HU");
                            mainActivityPresenter.translateTextBySelectedLanguage("HU");
                            break;
                        }
                        case R.drawable.ic_brit:{
                            mainActivityPresenter.saveLanguageToSettingsFile("EN");
                            mainActivityPresenter.translateTextBySelectedLanguage("EN");
                            break;
                        }
                        case R.drawable.ic_german:{
                            mainActivityPresenter.saveLanguageToSettingsFile("DE");
                            mainActivityPresenter.translateTextBySelectedLanguage("DE");
                            break;
                        }
                    }
                }

                @Override  public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
    }

    private void saveBaseUrl() {
        if(mainActivityPresenter == null) return;
        mainActivityPresenter.saveBaseUrl();
    }

    private void initFunctions() {
        if(button != null){
            button.setOnClickListener(v -> {
                mainActivityPresenter.startProgram();
            });
        }
    }

    private void initPresenter() {
        mainActivityPresenter = new MainActivityPresenter(this, getApplicationContext());
    }


    void initView(){
        WindowSizeTypes[] wst = Helper.getWindowSizes(this);

        // PDA -> 4,3 inch
        if(wst[0] == WindowSizeTypes.COMPACT && wst[1] == WindowSizeTypes.COMPACT){

            setContentView(R.layout.main_activity_pda_portrait);
            messageTV = findViewById(R.id.messageTV_pda_portrait);
        }
        else if(wst[0] == WindowSizeTypes.COMPACT && wst[1] == WindowSizeTypes.MEDIUM){

            setContentView(R.layout.main_activity_mobile_portrait);
            messageTV = findViewById(R.id.messageTV_mobile_portrait);
            languageSelector = findViewById(R.id.languageSelector_mobile_portrait);

            button = findViewById(R.id.button_mobile_portrait);
        }
        else if(wst[0] == WindowSizeTypes.MEDIUM && wst[1] == WindowSizeTypes.COMPACT){

            setContentView(R.layout.main_activity_mobile_landscape);
            messageTV = findViewById(R.id.messageTV_mobile_landscape);
            languageSelector = findViewById(R.id.languageSelector_mobile_landscape);

            button = findViewById(R.id.button_mobile_landscape);
        }
        else if(wst[0] == WindowSizeTypes.MEDIUM && wst[1] == WindowSizeTypes.EXPANDED){

            setContentView(R.layout.main_activity_tablet_portrait);
            messageTV = findViewById(R.id.messageTV_tablet_portrait);
            languageSelector = findViewById(R.id.languageSelector_tablet_portrait);

            button = findViewById(R.id.button_tablet_portrait);
        }
        else if(wst[0] == WindowSizeTypes.EXPANDED && wst[1] == WindowSizeTypes.MEDIUM){

            setContentView(R.layout.main_activity_tablet_landscape);
            messageTV = findViewById(R.id.messageTV_tablet_landscape);
            languageSelector = findViewById(R.id.languageSelector_tablet_landscape);

            button = findViewById(R.id.button_tablet_landscape);
        }

        if(messageTV != null) messageTV.setText(null);
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

    @Override
    public void setTextToMessageTV(String message) {
        if(message == null) return;
        if(messageTV == null) return;
        messageTV.setText(message);
    }
    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
}