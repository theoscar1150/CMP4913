package com.example.cmp4913;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.VideoView;

import org.jetbrains.annotations.Nullable;

public class fromSign extends AppCompatActivity {

    ImageButton btn_InputCamera;
    private boolean isRecording = false;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_sign);
        btn_InputCamera = (ImageButton) findViewById(R.id.btn_inputCamera);
        btn_InputCamera.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                // Check if the app has camera permission
                if (ContextCompat.checkSelfPermission(fromSign.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Request camera permission
                    ActivityCompat.requestPermissions(fromSign.this, new String[] { Manifest.permission.CAMERA }, CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    // Camera permission is already granted
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    startActivityForResult(intent,1);
                }
            }
        });
    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        VideoView videoView = new VideoView(this);
        if(data.getData() != null) {
            videoView.setVideoURI(data.getData());
            videoView.start();
            builder.setView(videoView).show();
        }
    }
}