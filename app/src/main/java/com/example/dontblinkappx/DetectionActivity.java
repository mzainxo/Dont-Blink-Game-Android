package com.example.dontblinkappx;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
    TextView textView;
    SurfaceView surface_camera_preview;
    CameraSource cameraSource;

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
            surface_camera_preview = findViewById(R.id.surface_camera_preview);

            // Add SurfaceHolder callback
            surface_camera_preview.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder holder) {
                    startCameraSource();
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                    // Handle surface changes if necessary
                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                    if (cameraSource != null) {
                        cameraSource.stop();
                    }
                }
            });

            createCameraSource();
        }
    }

    public void createCameraSource() {
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
        // Restart the camera source if it exists
        startCameraSource();
        setTitle("Welcome"); // Set the title for the DetectionActivity
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the camera source to release resources
        if (cameraSource != null) {
            cameraSource.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the camera source when the activity is destroyed
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private class EyesTracker extends Tracker<Face> {
        private final float THRESHOLD = 0.75f;

        @Override
        public void onUpdate(Detector.Detections<Face> detections, Face face) {
            if (face.getIsLeftEyeOpenProbability() > THRESHOLD || face.getIsRightEyeOpenProbability() > THRESHOLD) {
                Log.i(TAG, "onUpdate: Eyes Detected");
                showStatus("Eyes Detected and open, so video continues");
            } else {
                showStatus("Eyes Detected and closed, so video paused");
            }
        }

        @Override
        public void onMissing(Detector.Detections<Face> detections) {
            super.onMissing(detections);
            showStatus("Face Not Detected yet!");
        }

        @Override
        public void onDone() {
            super.onDone();
        }
    }

    private class FaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new EyesTracker();
        }
    }

    // Method to update status text on UI
    public void showStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(message);
            }
        });
    }
}
