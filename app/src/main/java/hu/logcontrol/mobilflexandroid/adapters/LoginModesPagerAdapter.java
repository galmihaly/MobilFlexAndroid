package hu.logcontrol.mobilflexandroid.adapters;

import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.logcontrol.mobilflexandroid.enums.FragmentTypes;
import hu.logcontrol.mobilflexandroid.helpers.Helper;

public class LoginModesPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> baseFragmentList = new ArrayList<>();
    private final HashMap<FragmentTypes, Fragment> baseFragmentHashMap = new HashMap<>();
    private final List<ImageButton> imageButtonList = new ArrayList<>();

    private GradientDrawable lowAlphaBackground;
    private GradientDrawable highAlphaBackground;

    public LoginModesPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setLowAlphaBackground(GradientDrawable lowAlphaBackground) {
        this.lowAlphaBackground = lowAlphaBackground;
    }

    public void setHighAlphaBackground(GradientDrawable highAlphaBackground) {
        this.highAlphaBackground = highAlphaBackground;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return baseFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return baseFragmentList.size();
    }

    public void addFragment(Fragment fragment){
        this.baseFragmentList.add(fragment);
    }

    public void setCurrentButtonColorByPosition(int position){
        if(imageButtonList == null) return;
        Helper.changeButtonColor(imageButtonList, position, lowAlphaBackground, highAlphaBackground);
    }

    public void addItemToHashMap(FragmentTypes fragmentTypes, Fragment fragment){ baseFragmentHashMap.put(fragmentTypes, fragment); }

    public void addButtonToList(ImageButton imageButtons){
        imageButtonList.add(imageButtons);
    }

    public void setBackgroundToButtons(GradientDrawable g){
        if(g == null) return;
        if(imageButtonList == null) return;

        for (int i = 0; i < this.imageButtonList.size(); i++) {
            imageButtonList.get(i).setBackground(g);
            imageButtonList.get(i).setImageAlpha(255);
            imageButtonList.get(i).getBackground().setAlpha(255);
        }
    }

    public int setCurrentButtonColorByEnum(FragmentTypes fragmentTypes){
        if(baseFragmentHashMap == null) return -1;
        if(baseFragmentList == null) return -1;
        if(imageButtonList == null) return -1;

        for (Map.Entry<FragmentTypes, Fragment> entry : baseFragmentHashMap.entrySet()){
            if(entry.getKey() == fragmentTypes){
                for (int i = 0; i < baseFragmentList.size(); i++) {
                    if(entry.getValue() != null){
                        if(entry.getValue().equals(baseFragmentList.get(i))){
                            Helper.changeButtonColor(imageButtonList, i, lowAlphaBackground, highAlphaBackground);
                            return i;
                        }
                    }
                }
                break;
            }
        }

        return -1;
    }
}
