package com.aro.asistente_crohn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aro.asistente_crohn.data.User;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class FirstTime_8Name_Activity extends AppCompatActivity {

    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time8_name_background);

        username = (EditText) findViewById(R.id.username);

        User user = new User(username.toString());
    }
}