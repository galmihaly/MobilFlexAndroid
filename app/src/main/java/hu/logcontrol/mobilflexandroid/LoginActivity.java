package hu.logcontrol.mobilflexandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.List;

import hu.logcontrol.mobilflexandroid.adapters.LoginModesPagerAdapter;
import hu.logcontrol.mobilflexandroid.enums.FragmentTypes;
import hu.logcontrol.mobilflexandroid.enums.MessageIdentifiers;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;
import hu.logcontrol.mobilflexandroid.fragments.BarcodeFragment;
import hu.logcontrol.mobilflexandroid.fragments.PinCodeFragment;
import hu.logcontrol.mobilflexandroid.fragments.RFIDFragment;
import hu.logcontrol.mobilflexandroid.fragments.UserPassFragment;
import hu.logcontrol.mobilflexandroid.helpers.Helper;
import hu.logcontrol.mobilflexandroid.interfaces.ILoginActivity;
import hu.logcontrol.mobilflexandroid.listeners.LoginButtonListener;
import hu.logcontrol.mobilflexandroid.listeners.ViewPagerListener;
import hu.logcontrol.mobilflexandroid.presenters.LoginActivityPresenter;

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

    private TextView applicationLeadTV;
    private TextView applicationTitleTV;

    private LinearLayout llay;

    private ViewPager2 loginModesPager;
    private ViewPagerListener viewPagerListener;

    private LoginButtonListener userPassLogButListener;
    private LoginButtonListener pinCodeLogButListener;
    private LoginButtonListener rfidLogButListener;
    private LoginButtonListener barcodeLogButListener;

    private LoginModesPagerAdapter loginModesPagerAdapter;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initPresenter();
        initSettingsPreferenceFile();
        initLanguagePreferenceFiles();
        initSettingsDataFromWebAPI();
        setControlsValuesBySettings();
        initLoginButtons();

        String serialNumber;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);

            serialNumber = (String) get.invoke(c, "gsm.sn1");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ril.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ro.serialno");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "sys.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = Build.SERIAL;

            Log.e("ser", serialNumber);

            // If none of the methods above worked
            if (serialNumber.equals("")) serialNumber = null;
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = null;
        }
    }

    private void setControlsValuesBySettings() {
        if(loginActivityPresenter == null) return;
        loginActivityPresenter.setControlsValuesBySettings();
    }

    private void initSettingsDataFromWebAPI() {
        if(loginActivityPresenter == null) return;
        loginActivityPresenter.saveSettingsToPreferences(getIntent());
    }

    private void initLoginButtons() {
        if(loginActivityPresenter != null && wst != null){
            loginActivityPresenter.initButtonsByLoginModesNumber(wst);
        }
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

    private void initLanguagePreferenceFiles() {
        if(loginActivityPresenter != null) {
            loginActivityPresenter.initLanguageSharedPreferenceFiles();
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
            loginModesPager = findViewById(R.id.loginModes_mobile_portrait);

            applicationLeadTV = findViewById(R.id.applicationLead_mobile_portrait);
            applicationTitleTV = findViewById(R.id.applicationTitle_mobile_portrait);

            changeStateLoginLogo(R.drawable.ic_baseline_album);
        }
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
    public void changeStateApplicationLeadTextbox(String applicationDescription, String textColor) {
        if(applicationDescription == null) return;
        if(textColor == null) return;
        if(applicationLeadTV == null) return;

        applicationLeadTV.setTextColor(Color.parseColor(textColor));
        applicationLeadTV.setText(applicationDescription);
    }

    @Override
    public void changeStateApplicationTitleTextbox(String applicationName, String textColor) {
        if(applicationName == null) return;
        if(textColor == null) return;
        if(applicationTitleTV == null) return;

        applicationTitleTV.setTextColor(Color.parseColor(textColor));
        applicationTitleTV.setText(applicationName);
    }

    @Override
    public void sendCreatedButtonsToView(List<ImageButton> createdButtons) {
        if(llay == null) return;
        if(createdButtons == null) return;
        llay.invalidate();

        loginModesPagerAdapter = new LoginModesPagerAdapter(getSupportFragmentManager(), getLifecycle());

        for (int i = 0; i < createdButtons.size(); i++) {
            switch (createdButtons.get(i).getId()){
                case MessageIdentifiers.BUTTON_USER_PASS:{
                    loginAccAndPassButton = createdButtons.get(i);
                    Fragment fragment = new UserPassFragment();

                    llay.addView(loginAccAndPassButton);
                    loginModesPagerAdapter.addButtonToList(loginAccAndPassButton);
                    loginModesPagerAdapter.addFragment(fragment);
                    loginModesPagerAdapter.addItemToHashMap(FragmentTypes.USERPASSFRAGMENT, fragment);

                    break;
                }
                case MessageIdentifiers.BUTTON_PINCODE:{
                    loginPinButton = createdButtons.get(i);
                    Fragment fragment = new PinCodeFragment();

                    llay.addView(loginPinButton);
                    loginModesPagerAdapter.addButtonToList(loginPinButton);
                    loginModesPagerAdapter.addFragment(fragment);
                    loginModesPagerAdapter.addItemToHashMap(FragmentTypes.PINCODEFRAGMENT, fragment);

                    break;
                }
                case MessageIdentifiers.BUTTON_RFID:{
                    loginRFIDButton = createdButtons.get(i);
                    Fragment fragment = new RFIDFragment();

                    llay.addView(loginRFIDButton);
                    loginModesPagerAdapter.addButtonToList(loginRFIDButton);
                    loginModesPagerAdapter.addFragment(fragment);
                    loginModesPagerAdapter.addItemToHashMap(FragmentTypes.RFIDFRAGMENT, fragment);

                    break;
                }
                case MessageIdentifiers.BUTTON_BARCODE:{
                    loginBarcodeButton = createdButtons.get(i);
                    Fragment fragment = new BarcodeFragment();

                    llay.addView(loginBarcodeButton);
                    loginModesPagerAdapter.addButtonToList(loginBarcodeButton);
                    loginModesPagerAdapter.addFragment(fragment);
                    loginModesPagerAdapter.addItemToHashMap(FragmentTypes.BARCODEFRAGMENT, fragment);

                    break;
                }
            }
        }

        if(loginModesPager != null) loginModesPager.setAdapter(loginModesPagerAdapter);
        viewPagerListener = new ViewPagerListener(loginModesPagerAdapter);

        initLoginButtonFunctions();
    }

    private void initLoginButtonFunctions() {

        if(loginModesPager != null && viewPagerListener != null) { loginModesPager.registerOnPageChangeCallback(viewPagerListener); }

        if(loginAccAndPassButton != null){
            userPassLogButListener = new LoginButtonListener(loginModesPagerAdapter, loginModesPager, FragmentTypes.USERPASSFRAGMENT);
            loginAccAndPassButton.setOnClickListener(userPassLogButListener);
        }

        if(loginPinButton != null){
            pinCodeLogButListener = new LoginButtonListener(loginModesPagerAdapter, loginModesPager, FragmentTypes.PINCODEFRAGMENT);
            loginPinButton.setOnClickListener(pinCodeLogButListener);
        }

        if(loginBarcodeButton != null){
            barcodeLogButListener = new LoginButtonListener(loginModesPagerAdapter, loginModesPager, FragmentTypes.BARCODEFRAGMENT);
            loginBarcodeButton.setOnClickListener(barcodeLogButListener);
        }

        if(loginRFIDButton != null){
            rfidLogButListener = new LoginButtonListener(loginModesPagerAdapter, loginModesPager, FragmentTypes.RFIDFRAGMENT);
            loginRFIDButton.setOnClickListener(rfidLogButListener);
        }
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
}