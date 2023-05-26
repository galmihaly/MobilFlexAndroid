package hu.logcontrol.mobilflexandroid.helpers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import hu.logcontrol.mobilflexandroid.enums.MessageIdentifiers;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;

public class Helper {

    public static WindowSizeTypes[] getWindowSizes(AppCompatActivity appCompatActivity) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        appCompatActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        float widthDp = width / appCompatActivity.getResources().getDisplayMetrics().density;

        WindowSizeTypes[] widthWindowSizeClass = new WindowSizeTypes[2];

        if (widthDp < 600f) { widthWindowSizeClass[0] = WindowSizeTypes.COMPACT; }
        else if (widthDp < 840f) { widthWindowSizeClass[0] = WindowSizeTypes.MEDIUM; }
        else { widthWindowSizeClass[0] = WindowSizeTypes.EXPANDED; }

        float heightDp = height / appCompatActivity.getResources().getDisplayMetrics().density;

        if (heightDp < 600f) { widthWindowSizeClass[1] = WindowSizeTypes.COMPACT;}
        else if (heightDp < 900f) { widthWindowSizeClass[1] = WindowSizeTypes.MEDIUM; }
        else { widthWindowSizeClass[1] = WindowSizeTypes.EXPANDED; }

        return widthWindowSizeClass;
    }

    public static Message createMessage(int id, String dataString) {
        Bundle bundle = new Bundle();
        bundle.putString(MessageIdentifiers.MESSAGE_BODY, dataString);
        Message message = new Message();
        message.what = id;
        message.setData(bundle);

        return message;
    }

    public static void changeButtonColor(List<ImageButton> imageButtonList, int isActive, GradientDrawable lowAlphaBackground, GradientDrawable highAlphaBackground){
        if(imageButtonList == null) return;
        if(lowAlphaBackground == null) return;
        if(highAlphaBackground == null) return;

        for (int i = 0; i < imageButtonList.size(); i++) {
            if(i != isActive){
                imageButtonList.get(i).setBackground(lowAlphaBackground);

                Drawable wrappedDrawable = DrawableCompat.wrap(imageButtonList.get(i).getDrawable());
                DrawableCompat.setTint(wrappedDrawable, Color.GRAY);
            }
            else {
                imageButtonList.get(i).setBackground(highAlphaBackground);

                Drawable wrappedDrawable = DrawableCompat.wrap(imageButtonList.get(i).getDrawable());
                DrawableCompat.setTint(wrappedDrawable, Color.BLACK);
            }
        }
    }

    public static void sendDisplaySizesToFragments(Fragment fragment, WindowSizeTypes[] windowSizeClasses, int applicationId, int isFromLoginPage) {
        if(fragment == null) return;
        if(windowSizeClasses == null) return;

        Bundle bundle = new Bundle();
        bundle.putSerializable("windowHeightEnum", windowSizeClasses[0]);
        bundle.putSerializable("windowWidthEnum", windowSizeClasses[1]);
        bundle.putInt("applicationId", applicationId);
        bundle.putInt("isFromLoginPage", isFromLoginPage);

        fragment.setArguments(bundle);
    }

    public static boolean isInternetConnection(Context context){

        boolean isConnected;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm != null){
            Network connectedNetwork = cm.getActiveNetwork();
            if(connectedNetwork != null){
                NetworkCapabilities nc = cm.getNetworkCapabilities(connectedNetwork);
                if(nc != null && nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                    isConnected = true;
                }
                else {
                    isConnected = false;
                }
            }
            else {
                isConnected = false;
            }
        }
        else {
            isConnected = false;
        }

        return isConnected;
    }

    public static List<Integer> splitStringToIntList(String t, String character) {
        if(t == null) return null;
        if(t.length() == 0) return null;

        List<Integer> result = new ArrayList<>();

        if(t.length() == 1){
            result.add(Integer.parseInt(t));
        }
        else {
            String[] stringArray = t.split(character);

            for (int i = 0; i < stringArray.length; i++) {
                result.add(Integer.parseInt(stringArray[i]));
            }
        }

        return result;
    }
}
