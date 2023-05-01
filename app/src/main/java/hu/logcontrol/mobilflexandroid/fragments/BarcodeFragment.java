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

public class BarcodeFragment extends Fragment implements ILoginFragments {

    private View view;
    private LoginFragmentsPresenter loginFragmentsPresenter;

    private TextInputLayout loginBarCode1;
    private TextInputEditText loginBarCode2;

    private Button loginButton;
    private WindowSizeTypes[] wsc = new WindowSizeTypes[2];
    private String defaultThemeId;
    private String applicationId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        wsc[0]  = (WindowSizeTypes) getArguments().getSerializable("windowHeightEnum");
        wsc[1] =  (WindowSizeTypes) getArguments().getSerializable("windowWidthEnum");
        defaultThemeId = getArguments().getString("defaultThemeId");
        applicationId = getArguments().getString("applicationId");

        if(wsc[0] != null && wsc[1] != null){
            if((wsc[0] == WindowSizeTypes.COMPACT && wsc[1] == WindowSizeTypes.MEDIUM) ||
                    (wsc[0] == WindowSizeTypes.MEDIUM && wsc[1] == WindowSizeTypes.COMPACT)){

                view = inflater.inflate(R.layout.fragment_barcode_mobile, container, false);

                loginBarCode1 = view.findViewById(R.id.loginBarcode_TL_mobile_portrait);
                loginBarCode2 = view.findViewById(R.id.loginBarcode_ET_mobile_portrait);
                loginButton = view.findViewById(R.id.barcodeLogBut_mobile_portrait);
            }
            else if((wsc[0] == WindowSizeTypes.MEDIUM && wsc[1] == WindowSizeTypes.EXPANDED) ||
                    (wsc[0] == WindowSizeTypes.EXPANDED && wsc[1] == WindowSizeTypes.MEDIUM)){

                view = inflater.inflate(R.layout.fragment_barcode_tablet, container, false);

                loginBarCode1 = view.findViewById(R.id.loginBarcode_TL_tablet_portrait);
                loginBarCode2 = view.findViewById(R.id.loginBarcode_ET_tablet_portrait);
                loginButton = view.findViewById(R.id.barcodeLogBut_tablet_portrait);
            }
        }

        initPresenter();
        initAppDataManager();
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

    private void setControlsValuesBySettings() {
        if(loginFragmentsPresenter == null) return;
        if(applicationId == null) return;
        if(defaultThemeId == null) return;
        loginFragmentsPresenter.setControlsValuesBySettings(applicationId, defaultThemeId);
    }

    private void setControlsTextsBySettings() {
        if(loginFragmentsPresenter == null) return;
        loginFragmentsPresenter.setControlsTextBySettings(FragmentTypes.BARCODEFRAGMENT);
    }

    private void initButtonListeners(){
        if(loginButton == null) return;
        if(loginFragmentsPresenter == null) return;
        loginButton.setOnClickListener(v -> { loginFragmentsPresenter.openActivityByEnum(ViewEnums.WEBVIEW_ACTIVITY); });
    }

    @Override
    public void changeStateUserPassElements(String controlColor, String textColor) {
        if(loginBarCode1 == null) return;
        if(loginBarCode2 == null) return;

        StateChangeHelper.changeStateTextInputEditText(loginBarCode1, loginBarCode2, controlColor, textColor);
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
    public void changeTextInputElemenets(String barCodeText, String changeText) {
        if(loginBarCode1 == null) return;
        loginBarCode1.setHint(barCodeText);
    }

    @Override
    public void openViewByIntent(Intent intent) {
        startActivity(intent);
    }
}