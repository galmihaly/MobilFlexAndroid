package hu.logcontrol.mobilflexandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hu.logcontrol.mobilflexandroid.adapters.LoginModesPagerAdapter;
import hu.logcontrol.mobilflexandroid.enums.FragmentTypes;
import hu.logcontrol.mobilflexandroid.enums.MessageIdentifiers;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;
import hu.logcontrol.mobilflexandroid.fragments.BarcodeFragment;
import hu.logcontrol.mobilflexandroid.fragments.PinCodeFragment;
import hu.logcontrol.mobilflexandroid.fragments.RFIDFragment;
import hu.logcontrol.mobilflexandroid.fragments.UserPassFragment;
import hu.logcontrol.mobilflexandroid.helpers.Helper;
import hu.logcontrol.mobilflexandroid.helpers.StateChangeHelper;
import hu.logcontrol.mobilflexandroid.interfaces.ILoginActivity;
import hu.logcontrol.mobilflexandroid.interfaces.IMessageListener;
import hu.logcontrol.mobilflexandroid.listeners.LoginButtonListener;
import hu.logcontrol.mobilflexandroid.listeners.ViewPagerListener;
import hu.logcontrol.mobilflexandroid.presenters.LoginActivityPresenter;

public class LoginActivity extends AppCompatActivity implements ILoginActivity, IMessageListener {

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
    private List<ImageButton> pagerButtons;

    private ImageButton backButton;

    private int applicationId;
    private int isFromLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initPresenter();
        initAppDataManager();
        getDatasFromIntent();
        initWebAPIServices();
        setControlsValuesBySettings();
        getLogoFromExternalStorage();
        initLoginButtons();
        initBackButton();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setControlsValuesBySettings();
        getLogoFromExternalStorage();
        refreshViewButtonsAndPager();
    }

    private void refreshViewButtonsAndPager() {
        if(pagerButtons == null) return;
        if(llay == null) return;
        if(loginModesPagerAdapter == null) return;

        GradientDrawable[] g = loginActivityPresenter.getCurrentThemeForButton(applicationId);

        loginModesPagerAdapter.setBackgroundToButtons(g[0]);
        loginModesPagerAdapter.setHighAlphaBackground(g[0]);
        loginModesPagerAdapter.setLowAlphaBackground(g[1]);
        viewPagerListener.onPageSelected(loginModesPager.getCurrentItem());
    }

    private void getLogoFromExternalStorage() {
        if(loginActivityPresenter == null) return;
        loginActivityPresenter.getLogoImageFromExternalStorage(applicationId);
    }

    private void initPresenter(){
        loginActivityPresenter = new LoginActivityPresenter(this, getApplicationContext());
    }

    private void initAppDataManager(){
        if(loginActivityPresenter == null) return;
        loginActivityPresenter.initAppDataManager();
    }

    private void initWebAPIServices() {
        if(loginActivityPresenter == null) return;
        loginActivityPresenter.initWebAPIServices();
    }

    private void setControlsValuesBySettings() {
        if(loginActivityPresenter == null) return;
        loginActivityPresenter.setControlsValuesBySettings(applicationId);
    }

    private void initLoginButtons() {
        if(loginActivityPresenter == null) return;
        if(wst == null) return;
        loginActivityPresenter.initButtonsByLoginModesNumber(wst, applicationId);
    }

    private void getDatasFromIntent(){

        Intent i = getIntent();

        if(i != null){
            applicationId = i.getIntExtra("applicationId", -1);
            isFromLoginPage = i.getIntExtra("isFromLoginPage", -1);
        }
    }

    private void initBackButton(){
        if(loginActivityPresenter == null) return;
        if(backButton == null) return;

        backButton.setOnClickListener(v -> {
            loginActivityPresenter.openActivityByEnum(ViewEnums.PROGRAMS_ACTIVITY, applicationId);
        });
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

            backButton = findViewById(R.id.backbutton_mobile_portrait);
        }
        else if(wst[0] == WindowSizeTypes.MEDIUM && wst[1] == WindowSizeTypes.COMPACT){

            setContentView(R.layout.login_activity_mobile_landscape);
            activityCL = findViewById(R.id.activityCL_mobile_landscape);
            loginTV = findViewById(R.id.loginTV_mobile_landscape);
            loginLogo = findViewById(R.id.loginLogo_mobile_landscape);
            llay = findViewById(R.id.logButtonsCL_mobile_landscape);
            loginModesPager = findViewById(R.id.loginModesCL1_mobile_landscape);

            applicationLeadTV = findViewById(R.id.applicationLead_mobile_landscape);
            applicationTitleTV = findViewById(R.id.applicationTitle_mobile_landscape);

            backButton = findViewById(R.id.backbutton_mobile_landscape);
        }
        else if(wst[0] == WindowSizeTypes.MEDIUM && wst[1] == WindowSizeTypes.EXPANDED){

            setContentView(R.layout.login_activity_tablet_portrait);
            activityCL = findViewById(R.id.activityCL_tablet_portrait);
            loginTV = findViewById(R.id.loginTV_tablet_portrait);
            loginLogo = findViewById(R.id.loginLogo_tablet_portrait);
            llay = findViewById(R.id.logButtonsCL_tablet_portrait);
            loginModesPager = findViewById(R.id.loginModes_tablet_portrait);

            applicationLeadTV = findViewById(R.id.applicationLead_tablet_portrait);
            applicationTitleTV = findViewById(R.id.applicationTitle_tablet_portrait);

            backButton = findViewById(R.id.backbutton_tablet_portrait);
        }
        else if(wst[0] == WindowSizeTypes.EXPANDED && wst[1] == WindowSizeTypes.MEDIUM){

            setContentView(R.layout.login_activity_tablet_landscape);
            activityCL = findViewById(R.id.activityCL_tablet_landscape);
            loginTV = findViewById(R.id.loginTV_tablet_landscape);
            loginLogo = findViewById(R.id.loginLogo_tablet_landscape);
            llay = findViewById(R.id.logButtonsCL_tablet_landscape);
            loginModesPager = findViewById(R.id.loginModesCL1_tablet_landscape);

            applicationLeadTV = findViewById(R.id.applicationLead_tablet_landscape);
            applicationTitleTV = findViewById(R.id.applicationTitle_tablet_landscape);

            backButton = findViewById(R.id.backbutton_tablet_landscape);
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
    public void getLogoFromPresenter(Bitmap bitmap) {
        if(loginLogo == null) return;
        if(bitmap == null) return;
        loginLogo.setImageBitmap(bitmap);
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
    public void changeMobileBarsColors(String statusBarColor, String navigationBarColor){
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor(statusBarColor));
        window.setNavigationBarColor(Color.parseColor(navigationBarColor));
    }

    @Override
    public void changeBackButtonColors(String buttonBackgroundColor, String buttonBackgroundGradientColor){
        if(backButton == null) return;
        if(buttonBackgroundColor == null) return;
        if(buttonBackgroundGradientColor == null) return;

        StateChangeHelper.changeStateButton(backButton, buttonBackgroundColor, buttonBackgroundGradientColor);
    }

    @Override
    public void sendCreatedButtonsToView(List<ImageButton> createdButtons) {
        if(llay == null) return;
        if(createdButtons == null) return;
        if(loginActivityPresenter == null) return;

        pagerButtons = createdButtons;

        llay.invalidate();

        loginModesPagerAdapter = new LoginModesPagerAdapter(getSupportFragmentManager(), getLifecycle());

        GradientDrawable[] g = loginActivityPresenter.getCurrentThemeForButton(applicationId);

        for (int i = 0; i < createdButtons.size(); i++) {
            switch (createdButtons.get(i).getId()){
                case MessageIdentifiers.BUTTON_USER_PASS:{
                    loginAccAndPassButton = createdButtons.get(i);

                    if(g != null) loginAccAndPassButton.setBackground(g[0]);

                    Fragment fragment = new UserPassFragment();

                    if(wst != null && applicationId != -1) {
                        Helper.sendDisplaySizesToFragments(fragment, wst, applicationId, isFromLoginPage);
                    }

                    if(createdButtons.size() != 1) llay.addView(loginAccAndPassButton);
                    loginModesPagerAdapter.addButtonToList(loginAccAndPassButton);
                    loginModesPagerAdapter.addFragment(fragment);
                    loginModesPagerAdapter.addItemToHashMap(FragmentTypes.USERPASSFRAGMENT, fragment);

                    break;
                }
                case MessageIdentifiers.BUTTON_PINCODE:{
                    loginPinButton = createdButtons.get(i);

                    if(g != null) loginPinButton.setBackground(g[0]);

                    Fragment fragment = new PinCodeFragment();

                    if(wst != null && applicationId != -1) {
                        Helper.sendDisplaySizesToFragments(fragment, wst, applicationId, isFromLoginPage);
                    }

                    if(createdButtons.size() != 1) llay.addView(loginPinButton);
                    loginModesPagerAdapter.addButtonToList(loginPinButton);
                    loginModesPagerAdapter.addFragment(fragment);
                    loginModesPagerAdapter.addItemToHashMap(FragmentTypes.PINCODEFRAGMENT, fragment);

                    break;
                }
                case MessageIdentifiers.BUTTON_RFID:{
                    loginRFIDButton = createdButtons.get(i);

                    if(g != null) loginRFIDButton.setBackground(g[0]);

                    Fragment fragment = new RFIDFragment();

                    if(wst != null && applicationId != -1) {
                        Helper.sendDisplaySizesToFragments(fragment, wst, applicationId, isFromLoginPage);
                    }

                    if(createdButtons.size() != 1) llay.addView(loginRFIDButton);
                    loginModesPagerAdapter.addButtonToList(loginRFIDButton);
                    loginModesPagerAdapter.addFragment(fragment);
                    loginModesPagerAdapter.addItemToHashMap(FragmentTypes.RFIDFRAGMENT, fragment);

                    break;
                }
                case MessageIdentifiers.BUTTON_BARCODE:{
                    loginBarcodeButton = createdButtons.get(i);

                    if(g != null) loginBarcodeButton.setBackground(g[0]);

                    Fragment fragment = new BarcodeFragment();

                    if(wst != null && applicationId != -1) {
                        Helper.sendDisplaySizesToFragments(fragment, wst, applicationId, isFromLoginPage);
                    }

                    if(createdButtons.size() != 1) llay.addView(loginBarcodeButton);
                    loginModesPagerAdapter.addButtonToList(loginBarcodeButton);
                    loginModesPagerAdapter.addFragment(fragment);
                    loginModesPagerAdapter.addItemToHashMap(FragmentTypes.BARCODEFRAGMENT, fragment);

                    break;
                }
            }
        }

        loginModesPagerAdapter.setHighAlphaBackground(g[0]);
        loginModesPagerAdapter.setLowAlphaBackground(g[1]);

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

    @Override
    public void sendMessage(String message) {
        if(message == null) return;
        new Handler(Looper.getMainLooper()).post(() -> { Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); });
    }

    @Override
    public void sendIntent(Intent intent) {
        if(intent == null) return;
        startActivity(intent);
    }
}