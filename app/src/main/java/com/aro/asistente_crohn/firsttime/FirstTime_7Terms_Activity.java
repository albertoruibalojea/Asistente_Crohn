package com.aro.asistente_crohn.firsttime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aro.asistente_crohn.R;

public class FirstTime_7Terms_Activity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time7_terms);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = getSharedPreferences("ASISTENTE_CROHN_PREFS", MODE_PRIVATE).edit();
                editor.putBoolean("termsAccepted", true);

                Intent intent = new Intent(FirstTime_7Terms_Activity.this, FirstTime_8Name_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}