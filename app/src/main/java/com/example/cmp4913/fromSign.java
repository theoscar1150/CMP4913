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
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class fromSign extends AppCompatActivity {

    ImageButton btn_InputCamera;
    Object video;
    private boolean isRecording = false;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int NUM_FRAMES = 10;
    Module model;
    String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_sign);

        btn_InputCamera = (ImageButton) findViewById(R.id.btn_inputCamera);
        btn_InputCamera.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            VideoView videoView = new VideoView(this);
            //AlertDialog.Builder builder = new AlertDialog.Builder(this);

            if (data != null && data.getData() != null) {

                Uri videoUri = data.getData();
                String videoPath = getVideoPathFromUri(videoUri);
                Log.d("fromSign", "Selected video: " + videoPath);
                try {
                    uploadVideo(videoPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(this, "test", Toast.LENGTH_LONG).show();

            }
        }
    }

    private String getVideoPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String videoPath = cursor.getString(columnIndex);
        cursor.close();
        return videoPath;
    }

    private void uploadVideo(final String videoPath) throws IOException {

        // Create a multipart request body with the video file
        File file = new File(videoPath);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("video", file.getName(),
                        RequestBody.create(MediaType.parse("video/*"), file))
                .build();

        // Create the request object with the URL and request body
        Request request = new Request.Builder()
                .url("http://10.25.153.145:5000")
                .post(requestBody)
                .build();

        // Send the request asynchronously
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle the failure
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_LONG).show();
            }
        });


//        //Step 1: create an HTTP client
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//
//        //Step 2: create an HTTP post request to send the video file
//        HttpPost httpPost = new HttpPost("http://your-flask-server-url.com/predict"); //Replace with your Flask server URL
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.addBinaryBody(
//                "file", //Specify the name of the file parameter
//                new File(videoPath),
//                ContentType.MULTIPART_FORM_DATA,
//                "file.mp4" //Specify the name of the file
//        );
//        HttpEntity multipart = builder.build();
//        httpPost.setEntity(multipart);
//
//        //Step 3: send the HTTP post request and receive the response
//        CloseableHttpResponse response = httpClient.execute(httpPost);
//        HttpEntity responseEntity = response.getEntity();
//
//        //Step 4: convert the response entity to a string and extract the prediction result
//        String responseString = EntityUtils.toString(responseEntity, "UTF-8");
//        JSONObject responseJson = new JSONObject(responseString);
//        String predictionResult = responseJson.getString("result");
//
//        //Step 5: print the prediction result
//        Log.d("fromSign", "Prediction result: " + predictionResult);
//        Toast.makeText(this, "Prediction result: " + predictionResult, Toast.LENGTH_LONG).show();
    }


}