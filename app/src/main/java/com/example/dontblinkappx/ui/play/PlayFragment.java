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
import com.example.dontblinkappx.UserScore;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        // Attach ValueEventListener to usersRef to retrieve high score, best time, and online user count among all users
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int maxHighScore = Integer.MIN_VALUE;
                int minBestTime = Integer.MIN_VALUE;
                int onlineUserCount = 0;

                // Iterate through each user
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Get the high score of the current user
                    Integer userHighScore = userSnapshot.child("HighScore").getValue(Integer.class);
                    if (userHighScore != null && userHighScore > maxHighScore) {
                        maxHighScore = userHighScore;
                    }

                    // Get the best time of the current user
                    Integer userBestTime = userSnapshot.child("BestTime").getValue(Integer.class);
                    if (userBestTime != null && userBestTime > minBestTime) {
                        minBestTime = userBestTime;
                    }

                    // Check if the user is online
                    String status = userSnapshot.child("Status").getValue(String.class);
                    if (status != null && status.equals("Online")) {
                        onlineUserCount++;
                    }
                }

                // Update the TextViews with the maximum high score, minimum best time, and online user count
                TextView maxHighScoreTextView = view.findViewById(R.id.tscountdown_text);
                maxHighScoreTextView.setText(String.valueOf(maxHighScore));

                TextView minBestTimeTextView = view.findViewById(R.id.ttime_count);
                minBestTimeTextView.setText(String.valueOf(minBestTime + "s"));

                TextView onlineUserCountTextView = view.findViewById(R.id.Onlinecount_count);
                onlineUserCountTextView.setText(String.valueOf(onlineUserCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("YourActivity", "Error fetching data", databaseError.toException());
            }
        });


        // Attach ValueEventListener to usersRef to retrieve users and their high scores
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Initialize a list to store user scores
                List<UserScore> userScores = new ArrayList<>();

                // Iterate through each user
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Get the user's ID and high score
                    String userId = userSnapshot.getKey();
                    Integer userHighScore = userSnapshot.child("HighScore").getValue(Integer.class);

                    // Add the user's score to the list
                    if (userId != null && userHighScore != null) {
                        userScores.add(new UserScore(userId, userHighScore));
                    }
                }

                // Sort the user scores in descending order of high scores
                Collections.sort(userScores, Collections.reverseOrder());

                // Find the current user's rank
                int currentUserRank = -1;
                for (int i = 0; i < userScores.size(); i++) {
                    if (userScores.get(i).userId.equals(storedUsername)) {
                        currentUserRank = i + 1;
                        break;
                    }
                }
                TextView rankcount = view.findViewById(R.id.rank_count);
                rankcount.setText(String.valueOf(currentUserRank));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("YourActivity", "Error fetching data", databaseError.toException());
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