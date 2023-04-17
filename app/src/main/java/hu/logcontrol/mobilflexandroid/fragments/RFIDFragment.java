package hu.logcontrol.mobilflexandroid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;

import hu.logcontrol.mobilflexandroid.R;

public class RFIDFragment extends Fragment {

    private View view;

    private TextInputEditText loginRFID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_rfid_mobile_portrait, container, false);

        loginRFID = view.findViewById(R.id.loginRFID_ET_mobile_portrait);

        return view;
    }
}