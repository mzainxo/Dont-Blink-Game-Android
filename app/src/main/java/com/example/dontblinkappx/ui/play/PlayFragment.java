package com.example.dontblinkappx.ui.play;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.dontblinkappx.LoadingActivity;
import com.example.dontblinkappx.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        // Initialize DatabaseReference
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users").child(storedUsername);

        // Attach ValueEventListener to dbref
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the value of GamesPlayed
                int gamesPlayed = dataSnapshot.child("GamesPlayed").getValue(Integer.class);

                // Update the TextView with the retrieved value
                TextView gpcountText = view.findViewById(R.id.gpcount_text);
                gpcountText.setText(String.valueOf(gamesPlayed));

                // Get the value of HighScore
                int highScore = dataSnapshot.child("HighScore").getValue(Integer.class);

                // Update the TextView for HighScore
                TextView YourHighScore = view.findViewById(R.id.countdown_text);
                YourHighScore.setText(String.valueOf(highScore));

                // Get the value of BestTime
                int bestTime = dataSnapshot.child("BestTime").getValue(Integer.class);

                // Update the TextView for BestTime
                TextView YourBestTime = view.findViewById(R.id.htime_count);
                YourBestTime.setText(String.valueOf(bestTime + "s"));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("YourActivity", "Error fetching GamesPlayed", databaseError.toException());
            }
        });

        // Declare DatabaseReference for users
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Attach ValueEventListener to usersRef to count the number of users
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the count of users
                long userCount = dataSnapshot.getChildrenCount();

                // Update the TextView with the user count
                TextView tPlayersCount = view.findViewById(R.id.tplayers_count);
                tPlayersCount.setText(String.valueOf(userCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("YourActivity", "Error fetching user count", databaseError.toException());
            }
        });

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