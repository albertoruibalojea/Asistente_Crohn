package com.aro.asistente_crohn.firsttime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aro.asistente_crohn.Home_Activity;
import com.aro.asistente_crohn.R;

public class FirstTime_8Name_Activity extends AppCompatActivity {

    EditText username;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time8_name);


        //Once the user writes the username, we get it and save it into SharedPreferences
        username = (EditText) findViewById(R.id.username);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //saving the username
                SharedPreferences.Editor editor = getSharedPreferences("ASISTENTE_CROHN_PREFS", MODE_PRIVATE).edit();
                editor.putString("username", username.getText().toString());
                editor.commit();

                Intent intent = new Intent(FirstTime_8Name_Activity.this, Home_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}