package com.aro.asistente_crohn.view.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.view.ui.firsttime.Activity1stTime;
import com.aro.asistente_crohn.view.ui.firsttime.FirstTime8NameActivity;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS = "com.aro.asistente_crohn_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //handler to create the splash screen
        int splashTimeOut = 2500;
        new Handler().postDelayed(() -> {

            if(!areTermsAccepted()){
                Intent firstTime1 = new Intent(MainActivity.this, Activity1stTime.class);
                startActivity (firstTime1);
                finish();
            } else {
                if(!isUsername()){
                    Intent firstTime8 = new Intent(MainActivity.this, FirstTime8NameActivity.class);
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