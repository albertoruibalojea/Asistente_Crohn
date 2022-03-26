package com.aro.asistente_crohn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //we should have 2 different handlers
        // - first time
        // - the rest

        //handler to create the splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent firstTime1 = new Intent(MainActivity.this, Activity_1stTime.class);
                startActivity (firstTime1);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}