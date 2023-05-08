package hu.logcontrol.mobilflexandroid.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
import hu.logcontrol.mobilflexandroid.helpers.Helper;
import hu.logcontrol.mobilflexandroid.helpers.StateChangeHelper;

public class UserPassFragment extends Fragment implements ILoginFragments {

    private View view;
    private LoginFragmentsPresenter loginFragmentsPresenter;

    private TextInputLayout loginUsername1;
    private TextInputEditText loginUsername2;
    private TextInputLayout loginPassword1;
    private TextInputEditText loginPassword2;

    private Button loginButton;

    private WindowSizeTypes[] wsc = new WindowSizeTypes[2];
    private int defaultThemeId;
    private int applicationId;
    private boolean isFromLoginPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        wsc[0]  = (WindowSizeTypes) getArguments().getSerializable("windowHeightEnum");
        wsc[1] =  (WindowSizeTypes) getArguments().getSerializable("windowWidthEnum");
        defaultThemeId = getArguments().getInt("defaultThemeId");
        applicationId = getArguments().getInt("applicationId");
        isFromLoginPage = getArguments().getBoolean("isFromLoginPage");

        if(wsc[0] != null && wsc[1] != null){
            if((wsc[0] == WindowSizeTypes.COMPACT && wsc[1] == WindowSizeTypes.MEDIUM) ||
                (wsc[0] == WindowSizeTypes.MEDIUM && wsc[1] == WindowSizeTypes.COMPACT)){

                view = inflater.inflate(R.layout.fragment_userpass_mobile, container, false);

                loginUsername1 = view.findViewById(R.id.loginUsernameTL_mobile_portrait);
                loginUsername2 = view.findViewById(R.id.loginUsernameET_mobile_portrait);
                loginPassword1 = view.findViewById(R.id.loginPasswordTL_mobile_portrait);
                loginPassword2 = view.findViewById(R.id.loginPasswordET_mobile_portrait);

                loginButton = view.findViewById(R.id.userpassLogBut_mobile_portrait);
            }
            else if((wsc[0] == WindowSizeTypes.MEDIUM && wsc[1] == WindowSizeTypes.EXPANDED) ||
                    (wsc[0] == WindowSizeTypes.EXPANDED && wsc[1] == WindowSizeTypes.MEDIUM)){

                view = inflater.inflate(R.layout.fragment_userpass_tablet, container, false);

                loginUsername1 = view.findViewById(R.id.loginUsernameTL_tablet_portrait);
                loginUsername2 = view.findViewById(R.id.loginUsernameET_tablet_portrait);
                loginPassword1 = view.findViewById(R.id.loginPasswordTL_tablet_portrait);
                loginPassword2 = view.findViewById(R.id.loginPasswordET_tablet_portrait);

                loginButton = view.findViewById(R.id.userpassLogBut_tablet_portrait);
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
        loginFragmentsPresenter.setControlsTextBySettings(FragmentTypes.USERPASSFRAGMENT);
    }

    private void initButtonListeners(){
        if(loginButton == null) return;
        if(loginUsername2 == null) return;
        if(loginPassword2 == null) return;
        if(loginFragmentsPresenter == null) return;

        int loginModeEnum = 1;

        loginButton.setOnClickListener(v -> {
            loginFragmentsPresenter.startLogin(loginUsername2.getText().toString(), loginPassword2.getText().toString(), loginModeEnum);
            loginFragmentsPresenter.openActivityByEnum(ViewEnums.WEBVIEW_ACTIVITY, applicationId, defaultThemeId, isFromLoginPage);
        });
    }

    @Override
    public void changeStateUserPassElements(String controlColor, String textColor) {
        if(loginUsername1 == null) return;
        if(loginUsername2 == null) return;
        if(loginPassword1 == null) return;
        if(loginPassword2 == null) return;

        StateChangeHelper.changeStateTextInputEditText(loginUsername1, loginUsername2, controlColor, textColor);
        StateChangeHelper.changeStateTextInputEditText(loginPassword1, loginPassword2, controlColor, textColor);
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
    public void changeTextInputElemenets(String usernameText, String passwordText) {
        if(loginUsername1 == null) return;
        if(loginPassword1 == null) return;
        loginUsername1.setHint(usernameText);
        loginPassword1.setHint(passwordText);
    }

    @Override
    public void openViewByIntent(Intent intent) {
        startActivity(intent);
    }
}