package com.aro.asistente_crohn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.aro.asistente_crohn.firsttime.Activity_1stTime;
import com.aro.asistente_crohn.firsttime.FirstTime_8Name_Activity;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;

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

                SharedPreferences sharedPrefs = getSharedPreferences("ASISTENTE_CROHN_PREFS", MODE_PRIVATE);

                if(!sharedPrefs.getBoolean("termsAccepted", false)){
                    Intent firstTime1 = new Intent(MainActivity.this, Activity_1stTime.class);
                    startActivity (firstTime1);
                    finish();
                } else {
                    if(sharedPrefs.getString("username", null) == null){
                        Intent firstTime8 = new Intent(MainActivity.this, FirstTime_8Name_Activity.class);
                        startActivity (firstTime8);
                        finish();
                    } else {
                        Intent intent = new Intent(MainActivity.this, Home_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }


            }
        }, SPLASH_TIME_OUT);
    }
}