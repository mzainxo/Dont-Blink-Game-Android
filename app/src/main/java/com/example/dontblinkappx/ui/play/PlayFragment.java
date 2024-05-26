package com.example.dontblinkappx.ui.play;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.dontblinkappx.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;
import java.util.ArrayList;

public class PlayFragment extends Fragment {

    private static final String TAG = "PlayFragment";
    TextView textView;
    SurfaceView surface_camera_preview;
    ArrayAdapter<String> adapter;
    ArrayList<String> list = new ArrayList<>();
    CameraSource cameraSource;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        // Request CAMERA permission if not granted
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            Toast.makeText(getContext(), "Grant Permission and restart app", Toast.LENGTH_SHORT).show();
        } else {
            textView = view.findViewById(R.id.textView);
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
            surface_camera_preview = view.findViewById(R.id.surface_camera_preview);

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

        return view;
    }

    public void createCameraSource() {
        FaceDetector detector = new FaceDetector.Builder(getContext())
                .setTrackingEnabled(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        detector.setProcessor(new MultiProcessor.Builder<>(new FaceTrackerFactory()).build());

        cameraSource = new CameraSource.Builder(getContext(), detector)
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
    public void onResume() {
        super.onResume();
        // Restart the camera source if it exists
        startCameraSource();
        if (getActivity() != null) {
            getActivity().setTitle("Welcome"); // Set the title for the PlayFragment
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop the camera source to release resources
        if (cameraSource != null) {
            cameraSource.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release the camera source when the fragment is destroyed
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
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(message);
            }
        });
    }
}
