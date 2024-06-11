package com.example.dontblinkappx.ui.leaderboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dontblinkappx.LeaderboardEntry;
import com.example.dontblinkappx.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("username", Context.MODE_PRIVATE);
        String storedUsername = sharedPref.getString("username", "");

        // Declare DatabaseReference for users
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

// Attach ValueEventListener to usersRef to retrieve users and their best times
        usersRef.orderByChild("BestTime").limitToLast(5).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Initialize a list to store top five users based on best time
                List<LeaderboardEntry> topFiveUsersByBestTime = new ArrayList<>();

                // Iterate through each user
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Get the user's username, best time, high score, games played, and status
                    String username = userSnapshot.getKey();
                    Integer bestTime = userSnapshot.child("BestTime").getValue(Integer.class);
                    Integer highScore = userSnapshot.child("HighScore").getValue(Integer.class);

                    // Add the user's data to the list
                    if (username != null && bestTime != null && highScore != null) {
                        LeaderboardEntry entry = new LeaderboardEntry(username, bestTime, highScore);
                        topFiveUsersByBestTime.add(entry);
                    }
                }
                Collections.sort(topFiveUsersByBestTime);
                LeaderboardEntry firstentry = topFiveUsersByBestTime.get(0);
                String username1 = firstentry.getUsername();
                int highScore1 = firstentry.getHighScore();
                int bestTime1 = firstentry.getBestTime();

                TextView username1lb = getView().findViewById(R.id.name_text1);
                username1lb.setText(String.valueOf(username1));

                TextView bestTime1lb = getView().findViewById(R.id.htime_text1);
                bestTime1lb.setText(String.valueOf(bestTime1));

                TextView highScore1lb = getView().findViewById(R.id.hscore_text1);
                highScore1lb.setText(String.valueOf(highScore1));


                LeaderboardEntry secondentry = topFiveUsersByBestTime.get(1);
                String username2 = secondentry.getUsername();
                int highScore2 = secondentry.getHighScore();
                int bestTime2 = secondentry.getBestTime();

                TextView username2lb = getView().findViewById(R.id.name_text2);
                username2lb.setText(String.valueOf(username2));

                TextView bestTime2lb = getView().findViewById(R.id.htime_text2);
                bestTime2lb.setText(String.valueOf(bestTime2));

                TextView highScore2lb = getView().findViewById(R.id.hscore_text2);
                highScore2lb.setText(String.valueOf(highScore2));

                LeaderboardEntry thirdentry = topFiveUsersByBestTime.get(2);
                String username3 = thirdentry.getUsername();
                int highScore3 = thirdentry.getHighScore();
                int bestTime3 = thirdentry.getBestTime();

                TextView username3lb = getView().findViewById(R.id.name_text3);
                username3lb.setText(String.valueOf(username3));

                TextView bestTime3lb = getView().findViewById(R.id.htime_text3);
                bestTime3lb.setText(String.valueOf(bestTime3));

                TextView highScore3lb = getView().findViewById(R.id.hscore_text3);
                highScore3lb.setText(String.valueOf(highScore3));

                LeaderboardEntry fourthentry = topFiveUsersByBestTime.get(3);
                String username4 = fourthentry.getUsername();
                int highScore4 = fourthentry.getHighScore();
                int bestTime4 = fourthentry.getBestTime();

                TextView username4lb = getView().findViewById(R.id.name_text4);
                username4lb.setText(String.valueOf(username4));

                TextView bestTime4lb = getView().findViewById(R.id.htime_text4);
                bestTime4lb.setText(String.valueOf(bestTime4));

                TextView highScore4lb = getView().findViewById(R.id.hscore_text4);
                highScore4lb.setText(String.valueOf(highScore4));

                LeaderboardEntry fifthentry = topFiveUsersByBestTime.get(4);
                String username5 = fifthentry.getUsername();
                int highScore5 = fifthentry.getHighScore();
                int bestTime5 = fifthentry.getBestTime();

                TextView username5lb = getView().findViewById(R.id.name_text5);
                username5lb.setText(String.valueOf(username5));

                TextView bestTime5lb = getView().findViewById(R.id.htime_text5);
                bestTime5lb.setText(String.valueOf(bestTime5));

                TextView highScore5lb = getView().findViewById(R.id.hscore_text5);
                highScore5lb.setText(String.valueOf(highScore5));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("YourActivity", "Error fetching data", databaseError.toException());
            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);

//// Retrieve the first entry from the list
//        LeaderboardEntry firstEntry = leaderboardEntries.get(0);
//
//        // Extract username, best time, and high score from the first entry
//        String username1 = firstEntry.getUsername();
//        TextView username1lb = getView().findViewById(R.id.name_text1);
//        username1lb.setText(String.valueOf(username1));
//
//        int bestTime1 = firstEntry.getBestTime();
//        TextView bestTime1lb = getView().findViewById(R.id.htime_text1);
//        bestTime1lb.setText(Integer.valueOf(bestTime1));
//
//        int highScore1 = firstEntry.getHighScore();
//        TextView highScorelb = getView().findViewById(R.id.hscore_text1);
//        highScorelb.setText(Integer.valueOf(highScore1));



    }


    public void onResume() {
        super.onResume();
        // Ensure cameraSource is started if not null

        if (getActivity() != null) {
            getActivity().setTitle("Leaderboard"); // Set the title for the PlayFragment
        }
    }
}