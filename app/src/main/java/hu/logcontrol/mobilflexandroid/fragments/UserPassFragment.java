package hu.logcontrol.mobilflexandroid.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.fragments.interfaces.IUserPassFragment;
import hu.logcontrol.mobilflexandroid.fragments.presenters.UserPassPresenter;

public class UserPassFragment extends Fragment implements IUserPassFragment {

    private View view;
    private UserPassPresenter userPassPresenter;

    private TextInputLayout loginUsername1;
    private TextInputEditText loginUsername2;
    private TextInputLayout loginPassword1;
    private TextInputEditText loginPassword2;

    private Button loginButton;

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
        loginUsername1 = view.findViewById(R.id.loginUsernameTL_mobile_portrait);
        loginUsername2 = view.findViewById(R.id.loginUsernameET_mobile_portrait);
        loginPassword1 = view.findViewById(R.id.loginPasswordTL_mobile_portrait);
        loginPassword2 = view.findViewById(R.id.loginPasswordET_mobile_portrait);

        loginButton = view.findViewById(R.id.userpassLogBut_mobile_portrait);
    }

    private void initPresenter() {
        userPassPresenter = new UserPassPresenter(this, getContext());
        userPassPresenter.initTaskManager();
    }

    private void initSettingsPreferenceFile() {
        if(userPassPresenter == null) return;
        userPassPresenter.initSettingsPreferenceFile();
    }

    private void initLanguagesPreferenceFiles(){
        if(userPassPresenter == null) return;
        userPassPresenter.initLanguageSharedPreferenceFiles();
    }

    private void setControlsValuesBySettings() {
        if(userPassPresenter == null) return;
        userPassPresenter.setControlsValuesBySettings();
    }

    private void initButtonListeners(){
        if(loginButton == null) return;

        loginButton.setOnClickListener(v -> {

        });
    }

    @Override
    public void changeStateTextInputEditText(String controlColor, String textColor, String usernameTVlabel, String passwordTVLabel) {
        if(loginUsername1 == null) return;
        if(loginUsername2 == null) return;
        if(loginPassword1 == null) return;
        if(loginPassword2 == null) return;

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] { android.R.attr.state_focused}, // enabled
                new int[] {-android.R.attr.state_focused}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
        };

        int[] colors = new int[] {
                Color.parseColor(controlColor),
                Color.parseColor(controlColor),
                Color.parseColor(controlColor),
                Color.parseColor(controlColor)
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);

        loginUsername1.setBoxStrokeColorStateList(colorStateList);
        loginUsername1.setHintTextColor(ColorStateList.valueOf(Color.parseColor(textColor)));
        loginUsername1.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor(textColor)));
        loginUsername1.setHint(usernameTVlabel);

        loginUsername2.setTextColor(Color.parseColor(textColor));

        loginPassword1.setBoxStrokeColorStateList(colorStateList);
        loginPassword1.setHintTextColor(ColorStateList.valueOf(Color.parseColor(textColor)));
        loginPassword1.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor(textColor)));
        loginPassword1.setHint(passwordTVLabel);

        loginPassword2.setTextColor(Color.parseColor(textColor));
    }

    @Override
    public void changeStateLoginButton(String buttonBackgroundColor, String buttonBackgroundGradientColor, String buttonForeGroundColor, String buttonLabel) {
        if(buttonBackgroundColor == null) return;
        if(buttonBackgroundGradientColor == null) return;
        if(buttonForeGroundColor == null) return;
        if(loginButton == null) return;

        int[] colors = {Color.parseColor(buttonBackgroundColor),Color.parseColor(buttonBackgroundGradientColor)};
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
        g.setCornerRadius(60);

        loginButton.setTextColor(Color.parseColor(buttonForeGroundColor));
        loginButton.setText(buttonLabel);
        loginButton.setBackground(g);
    }
}