package com.aro.asistente_crohn.firsttime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.aro.asistente_crohn.ui.HomeActivity;
import com.aro.asistente_crohn.R;

public class FirstTime8NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time8_name);


        //Once the user writes the username, we get it and save it into SharedPreferences
        EditText username = findViewById(R.id.username);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(view -> {

            //saving the username
            SharedPreferences.Editor editor = getSharedPreferences("com.aro.asistente_crohn_preferences", MODE_PRIVATE).edit();

            if(username.getText().length() > 0){
                editor.putString("username", username.getText().toString());
            } else {
                editor.putString("username", "Usuario/a");
            }

            editor.apply();

            Intent intent = new Intent(FirstTime8NameActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}