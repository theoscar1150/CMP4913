package com.example.cmp4913;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class toSign extends AppCompatActivity {

    // Set up media recorder
    MediaRecorder mediaRecorder = new MediaRecorder();
    MediaPlayer mediaPlayer = new MediaPlayer();
    ImageButton btn_recordVoice;
    EditText txt_input;
    String sentence;
    VideoView videoView;
    protected static final int RESULT_SPEECH = 1;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_sign);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(getApplicationContext()));
        }

        btn_recordVoice = (ImageButton) findViewById(R.id.btn_recordVoice);
        txt_input = (EditText) findViewById(R.id.inputText);
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.a);
        MediaController mediaController = new MediaController(this);
        //link mediaController to videoView
        mediaController.setAnchorView(videoView);
        //allow mediaController to control our videoView
        videoView.setMediaController(mediaController);
        videoView.start();
        txt_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
            {
                if(i == EditorInfo.IME_ACTION_DONE)
                {
                    String input = txt_input.getText().toString();
                    if(!input.isEmpty())
                    {
                        boolean flag = false;
                        for (int x =0; x < input.length(); ++x)
                        {
                            if(input.matches(".*[\\d!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~].*"))
                            {
                               flag = true;
                               break;
                            }
                        }
                        if (flag)
                        {
                            Toast.makeText(getApplicationContext(),"the input contains digits and/or symbols, input a sentence without digits or symbols",Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            sentence = txt_input.getText().toString(); //Stored sentece after the user inputs text
                            txt_input.setText("");

                            /*Parse Sentence START*/
                            Python py = Python.getInstance();
                            PyObject pyo = py.getModule("translateToASL").callAttr("translateToASL", sentence);
                            String str = pyo.toString();
                            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                            /*Parse Sentence END*/
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"the input contains nothing, input a sentence please",Toast.LENGTH_LONG).show();
                    }

                }
                return false;
            }
        });

        btn_recordVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Your device doesn't support Speech to Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(getApplicationContext(),text.get(0),Toast.LENGTH_LONG).show();
                    txt_input.setText(text.get(0));
                }
                break;
        }
    }

}