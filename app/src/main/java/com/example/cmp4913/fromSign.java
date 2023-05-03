package com.example.cmp4913;
//import org.opencv.core.Core;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.core.Size;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.videoio.VideoCapture;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;

import android.annotation.SuppressLint;
import android.content.Context;
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
import org.pytorch.Module;
import org.pytorch.Tensor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class fromSign extends AppCompatActivity {

    ImageButton btn_InputCamera;
    Object video;
    private boolean isRecording = false;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_sign);

        Module i3dModel = null;
        try {
            i3dModel = Module.load(assetFilePath(getApplicationContext(), "torchscript_i3D_model.pt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    private String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }
        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }
    public Tensor generateTensor(long[] Size) {
        // Create a random array of floats
        Random rand = new Random();
        float[] arr = new float[(int)(Size[0]*Size[1])];
        for (int i = 0; i < Size[0]*Size[1]; i++) {
            arr[i] = -10000 + rand.nextFloat() * (20000);
        }
        // Create the tensor and return it
        return Tensor.fromBlob(arr, Size);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        VideoView videoView = new VideoView(this); //videoBiew stores the video
        if(data != null) {
            videoView.setVideoURI(data.getData());
            videoView.start();
            builder.setView(videoView).show();
        }
    }
}