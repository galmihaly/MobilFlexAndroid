package hu.logcontrol.mobilflexandroid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;

import hu.logcontrol.mobilflexandroid.R;

public class BarcodeFragment extends Fragment {

    private View view;

    private TextInputEditText loginBarcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_barcode_mobile_portrait, container, false);

        loginBarcode = view.findViewById(R.id.loginBarcode_ET_mobile_portrait);

        return view;
    }
}