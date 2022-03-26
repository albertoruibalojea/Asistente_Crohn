package com.aro.asistente_crohn.firsttime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aro.asistente_crohn.R;

public class Activity_1stTime extends AppCompatActivity {

    Button btn;
    TextView saltar_guia;
    ProgressBar progressBar;
    TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time1);

        openFragment(new Fragment_1st_1Hello());

        saltar_guia = findViewById(R.id.saltar_guia);
        saltar_guia.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Activity_1stTime.this, FirstTime_7Terms_Activity.class);
                startActivity(intent);
                finish();
            }
        });

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