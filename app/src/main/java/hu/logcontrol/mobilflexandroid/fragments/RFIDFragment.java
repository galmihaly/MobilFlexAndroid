package hu.logcontrol.mobilflexandroid.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import hu.logcontrol.mobilflexandroid.LoginActivity;
import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.enums.FragmentTypes;
import hu.logcontrol.mobilflexandroid.enums.LoginModes;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;
import hu.logcontrol.mobilflexandroid.fragments.interfaces.ILoginFragments;
import hu.logcontrol.mobilflexandroid.fragments.presenters.LoginFragmentsPresenter;
import hu.logcontrol.mobilflexandroid.helpers.StateChangeHelper;
import hu.logcontrol.mobilflexandroid.interfaces.IMessageListener;

public class RFIDFragment extends Fragment implements ILoginFragments {

    private View view;
    private LoginFragmentsPresenter loginFragmentsPresenter;

    private TextInputLayout loginRFID1;
    private TextInputEditText loginRFID2;

    private Button loginButton;
    private WindowSizeTypes[] wsc = new WindowSizeTypes[2];
    private int applicationId;
    private int isFromLoginPage;

    private IMessageListener IMessageListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            IMessageListener = (LoginActivity) context;
        } catch (ClassCastException e) {
            Log.e("ClassCastException", e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        wsc[0]  = (WindowSizeTypes) getArguments().getSerializable("windowHeightEnum");
        wsc[1] =  (WindowSizeTypes) getArguments().getSerializable("windowWidthEnum");
        applicationId = getArguments().getInt("applicationId");
        isFromLoginPage = getArguments().getInt("isFromLoginPage");

        if(wsc[0] != null && wsc[1] != null){
            if((wsc[0] == WindowSizeTypes.COMPACT && wsc[1] == WindowSizeTypes.MEDIUM) ||
                    (wsc[0] == WindowSizeTypes.MEDIUM && wsc[1] == WindowSizeTypes.COMPACT)){

                view = inflater.inflate(R.layout.fragment_rfid_mobile, container, false);

                loginRFID1 = view.findViewById(R.id.loginRFID_TL_mobile_portrait);
                loginRFID2 = view.findViewById(R.id.loginRFID_ET_mobile_portrait);
                loginButton = view.findViewById(R.id.rfidLogBut_mobile_portrait);
            }
            else if((wsc[0] == WindowSizeTypes.MEDIUM && wsc[1] == WindowSizeTypes.EXPANDED) ||
                    (wsc[0] == WindowSizeTypes.EXPANDED && wsc[1] == WindowSizeTypes.MEDIUM)){

                view = inflater.inflate(R.layout.fragment_rfid_tablet, container, false);

                loginRFID1 = view.findViewById(R.id.loginRFID_TL_tablet_portrait);
                loginRFID2 = view.findViewById(R.id.loginRFID_ET_tablet_portrait);
                loginButton = view.findViewById(R.id.rfidLogBut_tablet_portrait);
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

    @Override
    public void onResume() {
        super.onResume();

        initPresenter();
        initAppDataManager();
        initWebAPIServices();
        setControlsValuesBySettings();
        setControlsTextsBySettings();
        initButtonListeners();
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
        loginFragmentsPresenter.initWebAPIServices(applicationId);
    }

    private void setControlsValuesBySettings() {
        if(loginFragmentsPresenter == null) return;
        loginFragmentsPresenter.setControlsValuesBySettings(applicationId);
    }

    private void setControlsTextsBySettings() {
        if(loginFragmentsPresenter == null) return;
        loginFragmentsPresenter.setControlsTextBySettings(FragmentTypes.RFIDFRAGMENT);
    }

    private void initButtonListeners(){
        if(loginButton == null) return;
        if(loginRFID2 == null) return;
        if(loginFragmentsPresenter == null) return;

        loginButton.setOnClickListener(v -> {
            loginFragmentsPresenter.startLogin(loginRFID2.getText().toString(), "", LoginModes.Rfid, applicationId, isFromLoginPage);
        });
    }

    @Override
    public void changeStateUserPassElements(String controlColor, String textColor) {
        if(loginRFID1 == null) return;
        if(loginRFID2 == null) return;

        StateChangeHelper.changeStateTextInputEditText(loginRFID1, loginRFID2, controlColor, textColor);
    }

    @Override
    public void changeTextInputElemenets(String rfidText, String changeText){
        if(loginRFID1 == null) return;
        loginRFID1.setHint(rfidText);
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
    public void openViewByIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void sendMessageToView(String message) {
        if(message == null) return;
        if(IMessageListener == null) return;
        IMessageListener.sendMessage(message);
    }

    @Override
    public void sendIntentToView(Intent intent) {
        if(intent == null) return;
        if(IMessageListener == null) return;
        IMessageListener.sendIntent(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        view = null;
        loginFragmentsPresenter = null;

        loginRFID1 = null;
        loginRFID2 = null;

        loginButton = null;

        wsc = null;
    }
}