package hu.logcontrol.mobilflexandroid.listeners;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import hu.logcontrol.mobilflexandroid.adapters.LoginModesPagerAdapter;
import hu.logcontrol.mobilflexandroid.enums.FragmentTypes;

public class LoginButtonListener implements View.OnClickListener {

    private LoginModesPagerAdapter loginModesPagerAdapter;
    private ViewPager2 viewPager;
    private FragmentTypes fragmentType;

    public LoginButtonListener(LoginModesPagerAdapter loginModesPagerAdapter, ViewPager2 viewPager, FragmentTypes fragmentType) {
        this.loginModesPagerAdapter = loginModesPagerAdapter;
        this.viewPager = viewPager;
        this.fragmentType = fragmentType;
    }

    @Override
    public void onClick(View view) {

        int position = loginModesPagerAdapter.setCurrentButtonColorByEnum(fragmentType);
        viewPager.setCurrentItem(position, true);
    }
}
