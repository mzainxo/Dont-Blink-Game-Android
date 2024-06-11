package com.example.dontblinkappx;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dontblinkappx.databinding.ActivityMainBinding;
import com.example.dontblinkappx.ui.help.HelpFragment;
import com.example.dontblinkappx.ui.leaderboard.LeaderboardFragment;
import com.example.dontblinkappx.ui.play.PlayFragment;
import com.example.dontblinkappx.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setBackground(null);
        final String username = getIntent().getStringExtra("username");

        // Assuming you have a method to retrieve the current user's key
        String currentUserKey = username;

        // Start DetectionActivity and pass the currentUserKey
        Intent intent = new Intent(MainActivity.this, DetectionActivity.class);
        intent.putExtra("currentUserKey", currentUserKey);
        //startActivity(intent);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            Fragment selectedFragment = null;
            if (itemId == R.id.navigation_home) {
                selectedFragment = new PlayFragment();
            } else if (itemId == R.id.navigation_leaderboard) {
                selectedFragment = new LeaderboardFragment();
            } else if (itemId == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
            } else if (itemId == R.id.navigation_help) {
                selectedFragment = new HelpFragment();
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }

            return true;
        });

        // Load PlayFragment as the default fragment when the activity starts
        if (savedInstanceState == null) {
            replaceFragment(new PlayFragment());
        }

        // Set the navigation bar color to match the BottomNavigationView background color
        getWindow().setNavigationBarColor(
                getResources().getColor(R.color.yellow1, getTheme())
        );
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
