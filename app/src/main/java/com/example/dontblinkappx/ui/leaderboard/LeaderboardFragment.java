package com.example.dontblinkappx.ui.leaderboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dontblinkappx.R;

import java.io.IOException;

public class LeaderboardFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("username", Context.MODE_PRIVATE);
        String storedUsername = sharedPref.getString("username", "");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    public void onResume() {
        super.onResume();
        // Ensure cameraSource is started if not null

        if (getActivity() != null) {
            getActivity().setTitle("Leaderboard"); // Set the title for the PlayFragment
        }
    }
}