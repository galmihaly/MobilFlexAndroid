package hu.logcontrol.mobilflexandroid.listeners;

import androidx.viewpager2.widget.ViewPager2;

import hu.logcontrol.mobilflexandroid.adapters.LoginModesPagerAdapter;

public class ViewPagerListener extends ViewPager2.OnPageChangeCallback {

    private LoginModesPagerAdapter loginModesPagerAdapter;

    public ViewPagerListener(LoginModesPagerAdapter loginModesPagerAdapter) {
        this.loginModesPagerAdapter = loginModesPagerAdapter;
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        this.loginModesPagerAdapter.setCurrentButtonColorByPosition(position);
    }
}
