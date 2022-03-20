package com.aro.asistente_crohn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FirstTime_3_Activity extends AppCompatActivity {

    Button btn;
    TextView saltar_guia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time3);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstTime_3_Activity.this, FirstTime_4_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        saltar_guia = findViewById(R.id.saltar_guia);
        saltar_guia.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(FirstTime_3_Activity.this, FirstTime_7Terms_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}