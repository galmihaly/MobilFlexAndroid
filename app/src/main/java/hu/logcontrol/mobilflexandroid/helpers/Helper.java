package hu.logcontrol.mobilflexandroid.helpers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import java.util.List;

import hu.logcontrol.mobilflexandroid.enums.FragmentTypes;
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

    public static void changeButtonColor(List<ImageButton> imageButtonList, int isActive){
        for (int i = 0; i < imageButtonList.size(); i++) {
            if(i != isActive){
                imageButtonList.get(i).getBackground().setAlpha(60);
                imageButtonList.get(i).setImageAlpha(60);

                Drawable wrappedDrawable = DrawableCompat.wrap(imageButtonList.get(i).getDrawable());
                DrawableCompat.setTint(wrappedDrawable, Color.GRAY);
            }
            else {
                imageButtonList.get(i).getBackground().setAlpha(255);
                imageButtonList.get(i).setImageAlpha(255);

                Drawable wrappedDrawable = DrawableCompat.wrap(imageButtonList.get(i).getDrawable());
                DrawableCompat.setTint(wrappedDrawable, Color.BLACK);
            }
        }
    }

    public static void sendDisplaySizesToFragments(Fragment fragment, WindowSizeTypes[] windowSizeClasses) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("windowHeightEnum", windowSizeClasses[0]);
        bundle.putSerializable("windowWidthEnum", windowSizeClasses[1]);
//        bundle.putSerializable("fragmentType", fragmentTypes);

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
}
