package com.aro.asistente_crohn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirstTime_8Name_Activity extends AppCompatActivity {

    EditText username;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time8_name_background);

        username = (EditText) findViewById(R.id.username);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstTime_8Name_Activity.this, Home_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}