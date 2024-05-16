package com.aro.asistente_crohn.view.ui.firsttime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aro.asistente_crohn.R;

public class Activity1stTime extends AppCompatActivity {

    TextView saltarGuia;
    ProgressBar progressBar;
    TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time1);

        saltarGuia = findViewById(R.id.saltar_guia);
        saltarGuia.setOnClickListener(view -> {
            Intent intent = new Intent(Activity1stTime.this, Fragment1st1Hello.class);
            startActivity(intent);
            finish();
        });

        openFragment(new Fragment1st1Hello());

        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
    }

    void openFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.relativeLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}