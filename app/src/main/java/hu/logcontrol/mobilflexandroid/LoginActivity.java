package hu.logcontrol.mobilflexandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hu.logcontrol.mobilflexandroid.enums.MessageIdentifiers;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;
import hu.logcontrol.mobilflexandroid.helpers.Helper;
import hu.logcontrol.mobilflexandroid.interfaces.ILoginActivity;
import hu.logcontrol.mobilflexandroid.presenters.LoginActivityPresenter;
import hu.logcontrol.mobilflexandroid.presenters.MainActivityPresenter;

public class LoginActivity extends AppCompatActivity implements ILoginActivity {

    private LoginActivityPresenter loginActivityPresenter;
    private WindowSizeTypes[] wst;

    private ConstraintLayout activityCL;
    private TextView loginTV;
    private ImageView loginLogo;

    //LoginButtons
    private ImageButton loginAccAndPassButton;
    private ImageButton loginPinButton;
    private ImageButton loginRFIDButton;
    private ImageButton loginBarcodeButton;
    private LinearLayout llay;

    private int loginModesNumber = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initPresenter();
        initSettingsPreferenceFile();
        initLoginButtons();
        initFunctions();
    }

    private void initLoginButtons() {
        if(loginActivityPresenter != null && wst != null){
            int[] colors = {Color.parseColor("#FFFF0000"),Color.parseColor("#FF00FFFF")};
            loginActivityPresenter.initButtonsByLoginModesNumber(loginModesNumber, wst, colors);
        }
    }

    private void initFunctions() {

    }

    private void initPresenter() {
        loginActivityPresenter = new LoginActivityPresenter(this, getApplicationContext());
        loginActivityPresenter.initTaskManager();
    }

    private void initSettingsPreferenceFile() {
        if(loginActivityPresenter != null) {
            loginActivityPresenter.initSettingsPreferenceFile();
        }
    }

    void initView(){
        wst = Helper.getWindowSizes(this);

        // PDA -> 4,3 inch
        if(wst[0] == WindowSizeTypes.COMPACT && wst[1] == WindowSizeTypes.COMPACT){

            setContentView(R.layout.login_activity_mobile_portrait);
            activityCL = findViewById(R.id.activityCL_mobile_portrait);
        }
        else if(wst[0] == WindowSizeTypes.COMPACT && wst[1] == WindowSizeTypes.MEDIUM){

            setContentView(R.layout.login_activity_mobile_portrait);
            activityCL = findViewById(R.id.activityCL_mobile_portrait);
            loginTV = findViewById(R.id.loginTV_mobile_portrait);
            loginLogo = findViewById(R.id.loginLogo_mobile_portrait);
            llay = findViewById(R.id.logButtonsCL_mobile_portrait);
            changeStateMainActivityCL("#000000", "#FFFFFFFF");
            changeStateLoginTV("Belépés", "#FFFF0000");
            changeStateLoginLogo(R.drawable.ic_baseline_album);
        }
//        else if(wst[0] == WindowSizeTypes.MEDIUM && wst[1] == WindowSizeTypes.COMPACT){
//
//            setContentView(R.layout.main_activity_mobile_landscape);
//            activityCL = findViewById(R.id.activityCL_mobile_portrait);
//        }
//        else if(wst[0] == WindowSizeTypes.MEDIUM && wst[1] == WindowSizeTypes.EXPANDED){
//
//            setContentView(R.layout.main_activity_tablet_portrait);
//            activityCL = findViewById(R.id.activityCL_mobile_portrait);
//        }
//        else if(wst[0] == WindowSizeTypes.EXPANDED && wst[1] == WindowSizeTypes.MEDIUM){
//
//            setContentView(R.layout.main_activity_tablet_landscape);
//            activityCL = findViewById(R.id.activityCL_mobile_portrait);
//        }
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* ILoginActivity interfész függvényei */
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
    public void changeStateLoginTV(String message, String color) {
        if(message == null) return;
        if(color == null) return;
        if(loginTV == null) return;

        loginTV.setText(message);
        loginTV.setTextColor(Color.parseColor(color));
    }

    @Override
    public void changeStateLoginLogo(int logoID) {
        if(loginLogo == null) return;
        loginLogo.setImageResource(logoID);
    }

    @Override
    public void changeStateMainActivityCL(String startedGradientColor, String endedGradientColor) {
        if(startedGradientColor == null) return;
        if(endedGradientColor == null) return;
        if(activityCL == null) return;

        int[] colors = {Color.parseColor(startedGradientColor),Color.parseColor(endedGradientColor)};
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);

        activityCL.setBackground(g);
    }

    @Override
    public void sendCreatedButtonsToView(List<ImageButton> createdButtons) {
        if(llay == null) return;
        if(createdButtons == null) return;
        llay.invalidate();

        Log.e("x", "beléptem ide");

        for (int i = 0; i < createdButtons.size(); i++) {

            switch (createdButtons.get(i).getId()){
                case MessageIdentifiers.BUTTON_USER_PASS:{
                    loginAccAndPassButton = createdButtons.get(i);
                    Log.e("loginAccAndPassButton", "beléptem ide");
                    llay.addView(loginAccAndPassButton);
//                    fragmentPagerAdapter.addButtonToList(imageButton);
                    break;
                }
                case MessageIdentifiers.BUTTON_PINCODE:{
                    loginPinButton = createdButtons.get(i);
                    Log.e("loginPinButton", "beléptem ide");
                    llay.addView(loginPinButton);
//                    fragmentPagerAdapter.addButtonToList(imageButton);
                    break;
                }
                case MessageIdentifiers.BUTTON_RFID:{
                    loginRFIDButton = createdButtons.get(i);
                    Log.e("loginRFIDButton", "beléptem ide");
                    llay.addView(loginRFIDButton);
//                    fragmentPagerAdapter.addButtonToList(imageButton);
                    break;
                }
                case MessageIdentifiers.BUTTON_BARCODE:{
                    loginBarcodeButton = createdButtons.get(i);
                    Log.e("loginBarcodeButton", "beléptem ide");
                    llay.addView(loginBarcodeButton);
//                    fragmentPagerAdapter.addButtonToList(imageButton);
                    break;
                }
            }
        }

        initLoginButtonsListeners();
    }

    private void initLoginButtonsListeners(){
    }


    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
}