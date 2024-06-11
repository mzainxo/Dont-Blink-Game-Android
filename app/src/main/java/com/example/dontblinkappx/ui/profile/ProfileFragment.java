package com.example.dontblinkappx.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dontblinkappx.LoginActivity;
import com.example.dontblinkappx.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private TextView usernameTextView;
    private Button logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("username", Context.MODE_PRIVATE);
        String storedUsername = sharedPref.getString("username", "");

        // Find the TextView and set the retrieved username
        usernameTextView = view.findViewById(R.id.username_txt);
        usernameTextView.setText(storedUsername);

        // Find the logout button
        logoutButton = view.findViewById(R.id.gameButton);

        // Set the logout button click listener
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout(storedUsername);
            }
        });

        return view;
    }

    private void handleLogout(String username) {
        // Set the user's status to "Offline" in Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(username);
        userRef.child("Status").setValue("Offline");

        // Log out the user (clear SharedPreferences)
        SharedPreferences sharedPref = getActivity().getSharedPreferences("username", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();

        // Redirect to LoginActivity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

        // Close the current activity
        getActivity().finish();
    }
}
