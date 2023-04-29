package com.example.cmp4913;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btn_toSign;
    Button btn_fromSign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ConstraintLayout constraintLayout = findViewById(R.id.mainlayout);
        //AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        //animationDrawable.setEnterFadeDuration(2500);
        //animationDrawable.setExitFadeDuration(5000);
        //animationDrawable.start();

        setContentView(R.layout.activity_main);
        btn_toSign = (Button) findViewById(R.id.btn_toSign);
        btn_fromSign = (Button) findViewById(R.id.btn_fromSign);
        btn_toSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, toSign.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
        btn_fromSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, fromSign.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }


}