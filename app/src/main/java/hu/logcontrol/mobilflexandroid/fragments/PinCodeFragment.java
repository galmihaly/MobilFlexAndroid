package hu.logcontrol.mobilflexandroid.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.enums.FragmentTypes;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;
import hu.logcontrol.mobilflexandroid.fragments.interfaces.ILoginFragments;
import hu.logcontrol.mobilflexandroid.fragments.presenters.LoginFragmentsPresenter;
import hu.logcontrol.mobilflexandroid.helpers.StateChangeHelper;

public class PinCodeFragment extends Fragment implements ILoginFragments {

    private View view;
    private LoginFragmentsPresenter loginFragmentsPresenter;

    private TextInputLayout loginPinCode1;
    private TextInputEditText loginPinCode2;

    private Button loginButton;
    private WindowSizeTypes[] wsc = new WindowSizeTypes[2];
    private int defaultThemeId;
    private int applicationId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        wsc[0]  = (WindowSizeTypes) getArguments().getSerializable("windowHeightEnum");
        wsc[1] =  (WindowSizeTypes) getArguments().getSerializable("windowWidthEnum");
        defaultThemeId = getArguments().getInt("defaultThemeId");
        applicationId = getArguments().getInt("applicationId");

        if(wsc[0] != null && wsc[1] != null){
            if((wsc[0] == WindowSizeTypes.COMPACT && wsc[1] == WindowSizeTypes.MEDIUM) ||
                    (wsc[0] == WindowSizeTypes.MEDIUM && wsc[1] == WindowSizeTypes.COMPACT)){

                view = inflater.inflate(R.layout.fragment_pincode_mobile, container, false);

                loginPinCode1 = view.findViewById(R.id.loginPinCode_TL_mobile_portrait);
                loginPinCode2 = view.findViewById(R.id.loginPinCode_ET_mobile_portrait);
                loginButton = view.findViewById(R.id.pinCodeLogBut_mobile_portrait);
            }
            else if((wsc[0] == WindowSizeTypes.MEDIUM && wsc[1] == WindowSizeTypes.EXPANDED) ||
                    (wsc[0] == WindowSizeTypes.EXPANDED && wsc[1] == WindowSizeTypes.MEDIUM)){

                view = inflater.inflate(R.layout.fragment_pincode_tablet, container, false);

                loginPinCode1 = view.findViewById(R.id.loginPinCode_TL_tablet_portrait);
                loginPinCode2 = view.findViewById(R.id.loginPinCode_ET_tablet_portrait);
                loginButton = view.findViewById(R.id.pinCodeLogBut_tablet_portrait);
            }
        }

        initPresenter();
        initAppDataManager();
        initWebAPIServices();
        setControlsValuesBySettings();
        setControlsTextsBySettings();
        initButtonListeners();

        return view;
    }


    private void initPresenter() {
        loginFragmentsPresenter = new LoginFragmentsPresenter(this, getContext());
    }

    private void initAppDataManager(){
        if(loginFragmentsPresenter == null) return;
        loginFragmentsPresenter.initAppDataManager();
    }

    private void initWebAPIServices() {
        if(loginFragmentsPresenter == null) return;
        loginFragmentsPresenter.initWebAPIServices();
    }

    private void setControlsValuesBySettings() {
        if(loginFragmentsPresenter == null) return;
        loginFragmentsPresenter.setControlsValuesBySettings(defaultThemeId, applicationId);
    }

    private void setControlsTextsBySettings() {
        if(loginFragmentsPresenter == null) return;
        loginFragmentsPresenter.setControlsTextBySettings(FragmentTypes.PINCODEFRAGMENT);
    }

    private void initButtonListeners(){
        if(loginButton == null) return;
        if(loginPinCode2 == null) return;
        if(loginFragmentsPresenter == null) return;

        int loginModeEnum = 2;

        loginButton.setOnClickListener(v -> {
            loginFragmentsPresenter.startLogin(loginPinCode2.getText().toString(), null, loginModeEnum);
            loginFragmentsPresenter.openActivityByEnum(ViewEnums.WEBVIEW_ACTIVITY, applicationId, defaultThemeId);
        });
    }

    @Override
    public void changeStateUserPassElements(String controlColor, String textColor) {
        if(loginPinCode1 == null) return;
        if(loginPinCode2 == null) return;

        StateChangeHelper.changeStateTextInputEditText(loginPinCode1, loginPinCode2, controlColor, textColor);
    }

    @Override
    public void changeStateLoginButton(String buttonBackgroundColor, String buttonBackgroundGradientColor, String buttonForeGroundColor, String buttonLabel) {
        if(buttonBackgroundColor == null) return;
        if(buttonBackgroundGradientColor == null) return;
        if(buttonForeGroundColor == null) return;
        if(loginButton == null) return;

        StateChangeHelper.changeStateLoginButton(loginButton, buttonBackgroundColor, buttonBackgroundGradientColor, buttonForeGroundColor, buttonLabel);
    }

    @Override
    public void changeTextInputElemenets(String pinCodeText, String changeText) {
        if(loginPinCode1 == null) return;
        loginPinCode1.setHint(pinCodeText);
    }

    @Override
    public void openViewByIntent(Intent intent) {
        startActivity(intent);
    }
}