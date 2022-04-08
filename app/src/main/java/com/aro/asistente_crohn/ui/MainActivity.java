package com.aro.asistente_crohn.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.firsttime.Activity_1stTime;
import com.aro.asistente_crohn.firsttime.FirstTime_8Name_Activity;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS = "ASISTENTE_CROHN_PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //we should have 2 different handlers
        // - first time
        // - the rest

        //handler to create the splash screen
        int splashTimeOut = 4000;
        new Handler().postDelayed(() -> {

            if(!areTermsAccepted()){
                Intent firstTime1 = new Intent(MainActivity.this, Activity_1stTime.class);
                startActivity (firstTime1);
                finish();
            } else {
                if(!isUsername()){
                    Intent firstTime8 = new Intent(MainActivity.this, FirstTime_8Name_Activity.class);
                    startActivity (firstTime8);
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }


        }, splashTimeOut);
    }

    public boolean areTermsAccepted(){
        return getSharedPreferences(PREFS, MODE_PRIVATE).getBoolean("termsAccepted", false);
    }

    public boolean isUsername(){
        return getSharedPreferences(PREFS, MODE_PRIVATE).getString("username", null) != null;

    }
}