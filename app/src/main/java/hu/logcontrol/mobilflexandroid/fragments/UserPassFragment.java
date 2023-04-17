package hu.logcontrol.mobilflexandroid.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.fragments.interfaces.IUserPassFragment;
import hu.logcontrol.mobilflexandroid.fragments.presenters.UserPassPresenter;

public class UserPassFragment extends Fragment implements IUserPassFragment {

    private View view;
    private UserPassPresenter userPassPresenter;

    private TextInputLayout loginUsername;
    private TextInputLayout loginPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_userpass_mobile_portrait, container, false);

        initView();
        initPresenter();
        initSettingsPreferenceFile();
        setControlsValuesBySettings();
        initButtonListeners();

        return view;
    }

    private void initView(){
        if(view == null) return;
        loginUsername = view.findViewById(R.id.loginUsernameTL_mobile_portrait);
        loginPassword = view.findViewById(R.id.loginPasswordTL_mobile_portrait);
    }

    private void initPresenter() {
        userPassPresenter = new UserPassPresenter(this, getContext());
        userPassPresenter.initTaskManager();
    }

    private void initSettingsPreferenceFile() {
        if(userPassPresenter == null) return;
        userPassPresenter.initSettingsPreferenceFile();
    }

    private void setControlsValuesBySettings() {
        if(userPassPresenter == null) return;
        userPassPresenter.setControlsValuesBySettings();
    }

    private void initButtonListeners(){
        if(loginUsername == null) return;
        if(loginPassword == null) return;

        loginUsername.setOnClickListener(v -> {

        });

        loginPassword.setOnClickListener(v -> {

        });
    }

    @Override
    public void changeStateTextInputEditText(String controlColor, String textColor) {
        if(loginUsername == null) return;
        if(loginPassword == null) return;

        Log.e("changeStateTextInputEditText", "beléptem ide");

        loginUsername.setBoxStrokeColor(Color.parseColor(textColor));
        loginUsername.setHintTextColor(ColorStateList.valueOf(Color.parseColor(textColor)));

        loginPassword.setBoxStrokeColor(Color.parseColor(textColor));
        loginPassword.setHintTextColor(ColorStateList.valueOf(Color.parseColor(textColor)));
    }
}