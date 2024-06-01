package com.example.dontblinkappx;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
public class GameOverDialog extends Dialog {

    private DetectionActivity detectionActivity;
    private Button playAgain, homeButton;
    private int frameCount;
    private long stoppedTime;

    public GameOverDialog(DetectionActivity context, int frameCount, long stoppedTime) {
        super(context);
        detectionActivity = context;
        this.frameCount = frameCount;
        this.stoppedTime = stoppedTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogue);

        playAgain = findViewById(R.id.btnPlayAgain);
        homeButton = findViewById(R.id.btnBackToHome);
        TextView frameCountTextView = findViewById(R.id.tvFrameCount);
        TextView stoppedTimeTextView = findViewById(R.id.tvTimeScored);

        frameCountTextView.setText("Frame Count: " + frameCount);
        stoppedTimeTextView.setText("Time: " + (stoppedTime / 1000) + "s");

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectionActivity.shouldRestartGame = true;  // Allow game to restart
                Intent intent = new Intent(detectionActivity, LoadingActivity.class);
                detectionActivity.startActivity(intent);
                dismiss();
                detectionActivity.finish();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectionActivity.shouldRestartGame = false;  // Do not restart game
                Intent intent = new Intent(detectionActivity, MainActivity.class);
                intent.putExtra("fragment", "play");
                detectionActivity.startActivity(intent);
                dismiss();
                detectionActivity.finish();
            }
        });
    }
}
