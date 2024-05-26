package com.example.dontblinkappx;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView imageView = findViewById(R.id.imageView);
        TextView loadingText = findViewById(R.id.loadingText);

        // Load GIF using Glide
        Glide.with(this).asGif().load(R.drawable.minion_load).into(imageView);

        // Delay before starting MainActivity (adjust as needed)
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, DetectionActivity.class);
                startActivity(intent);
                finish();
            }
        }, 7000); // 3000 milliseconds (3 seconds) delay
    }
}
