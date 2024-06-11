package com.example.dontblinkappx.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dontblinkappx.R;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("username", Context.MODE_PRIVATE);
        String storedUsername = sharedPref.getString("username", "");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);



    }
}