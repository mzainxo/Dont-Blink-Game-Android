package com.example.dontblinkappx;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.dontblinkappx.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

public class DetectionActivity extends AppCompatActivity {

    private static final String TAG = "DetectionActivity";
    private TextView textView;
    private TextView timeSecondsTextView;
    private TextView countdownTextView;
    private SurfaceView surface_camera_preview;
    private CameraSource cameraSource;
    private int frameCount = 0;
    private long startTime;
    private Handler handler;
    public boolean shouldRestartGame = true;
    private boolean gameStopped = false; // Flag to prevent multiple stopAll calls

    // Public fields to store stopped frame count and time
    public int stoppedFrameCount;
    public long stoppedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        // Request CAMERA permission if not granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            Toast.makeText(this, "Grant Permission and restart app", Toast.LENGTH_SHORT).show();
        } else {
            textView = findViewById(R.id.textView);
            timeSecondsTextView = findViewById(R.id.time_seconds_text);
            surface_camera_preview = findViewById(R.id.surface_camera_preview);
            countdownTextView = findViewById(R.id.countdown_text);

            getWindow().setNavigationBarColor(
                    getResources().getColor(R.color.lyellow, getTheme())
            );
            // Add SurfaceHolder callback
            surface_camera_preview.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder holder) {
                    startCountdown();
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                    // Handle surface changes if necessary
                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                    stopAll("Surface Destroyed");
                }
            });

            createCameraSource();
        }
    }

    private void createCameraSource() {
        FaceDetector detector = new FaceDetector.Builder(this)
                .setTrackingEnabled(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        detector.setProcessor(new MultiProcessor.Builder<>(new FaceTrackerFactory()).build());

        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(1024, 768)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(60.0f)
                .build();
    }

    @SuppressLint("MissingPermission")
    private void startCameraSource() {
        if (cameraSource != null && surface_camera_preview.getHolder().getSurface().isValid()) {
            try {
                cameraSource.start(surface_camera_preview.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error starting camera source: " + e.getMessage());
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shouldRestartGame) {
            startCountdown();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DetectionActivity.this, MainActivity.class);
        intent.putExtra("fragment", "play");
        startActivity(intent);
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopAll("Screen Closed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAll("Screen Destroyed");
    }

    private class EyesTracker extends Tracker<Face> {
        private final float THRESHOLD = 0.70f;

        @Override
        public void onUpdate(Detector.Detections<Face> detections, Face face) {
            if (face.getIsLeftEyeOpenProbability() > THRESHOLD || face.getIsRightEyeOpenProbability() > THRESHOLD) {
                Log.i(TAG, "onUpdate: Eyes Detected");
                showStatus("Eyes Open");
            } else {
                stopAll("Eyes Blinked");
            }
            frameCount++;
            updateFrameCount();
        }

        @Override
        public void onMissing(Detector.Detections<Face> detections) {
            super.onMissing(detections);
            showStatus("Face Not Detected");
            stopAll("Face Not Detected");
        }

        @Override
        public void onDone() {
            super.onDone();
            stopAll("Eyes Blinked");
        }
    }

    private class FaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new EyesTracker();
        }
    }

    // Method to update status text on UI
    private void showStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(message);
            }
        });
    }

    // Method to update frame count on UI
    private void updateFrameCount() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                countdownTextView.setText(String.valueOf(frameCount));
            }
        });
    }

    // Method to start timer to update time in seconds
    private void startTimer() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - startTime;
                int seconds = (int) (elapsedTime / 1000);
                timeSecondsTextView.setText(String.valueOf(seconds));
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    public void openDialogueActivity(int frameCount, long stoppedTime) {
        GameOverDialog dialog = new GameOverDialog(DetectionActivity.this, frameCount, stoppedTime);
        dialog.setContentView(R.layout.activity_dialogue);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    // Method to stop timer and camera source
    private void stopAll(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (gameStopped)
                    return; // Prevent multiple calls to stopAll
                gameStopped = true;

                shouldRestartGame = false;
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                    handler = null;
                }

                // Store current frame count and time
                stoppedFrameCount = frameCount;
                stoppedTime = System.currentTimeMillis();

                showStatus(msg);

                if (cameraSource != null) {
                    cameraSource.stop();
                }
                // Prevent game from restarting automatically
                openDialogueActivity(stoppedFrameCount, stoppedTime - startTime);
            }
        });
    }

    // Method to start the countdown
    private void startCountdown() {
        final int[] countdownValues = {3, 2, 1, 0};
        handler = new Handler();
        for (int i = 0; i < countdownValues.length; i++) {
            final int index = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (countdownValues[index] == 0) {
                        showStatus("START");
                        startCameraSource();
                        startTime = System.currentTimeMillis();
                        startTimer();
                    } else {
                        textView.setText(String.valueOf(countdownValues[index]));
                    }
                }
            }, i * 1000);
        }
    }
}
