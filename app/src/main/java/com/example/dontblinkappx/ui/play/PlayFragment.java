package com.example.dontblinkappx.ui.play;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.example.dontblinkappx.LoadingActivity;
import com.example.dontblinkappx.R;
public class PlayFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        TextView textView = view.findViewById(R.id.username_txt);

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("username", Context.MODE_PRIVATE);
        String storedUsername = sharedPref.getString("username", "");

        textView.setText("Howdy, " + storedUsername);

        Button gameButton = view.findViewById(R.id.gameButton);
        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start DetectionActivity
                Intent intent = new Intent(getActivity(), LoadingActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}