package com.example.dontblinkappx;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    DetectionActivity detectionActivity = new DetectionActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        FullScreenVideoView videoView = findViewById(R.id.videoView);
        TextView loadingText = findViewById(R.id.loadingText);

        // Set the video URI and start playback
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.minionvid);
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true); // Loop the video if needed
            mp.start(); // Start the video
        });

        // Delay before starting DetectionActivity (adjust as needed)
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, DetectionActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000); // 4000 milliseconds (4 seconds) delay
    }
}
