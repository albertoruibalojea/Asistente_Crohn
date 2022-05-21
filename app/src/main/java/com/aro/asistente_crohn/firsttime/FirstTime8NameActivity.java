package com.aro.asistente_crohn.firsttime;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.aro.asistente_crohn.expert.SymptomConstants;
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

            Spinner spinner = view.findViewById(R.id.spinner2);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spinner_values, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemClickListener((adapterView, view1, i, l) -> {
                String name = SymptomConstants.PATTERN_GENERIC;
                if (adapter.getItem(i).toString().equalsIgnoreCase(SymptomConstants.PATTERN_SMALL_BOWEL)) {
                    name = SymptomConstants.PATTERN_SMALL_BOWEL;
                } else if (adapter.getItem(i).toString().equalsIgnoreCase(SymptomConstants.PATTERN_COLON)) {
                    name = SymptomConstants.PATTERN_COLON;
                } else if (adapter.getItem(i).toString().equalsIgnoreCase(SymptomConstants.PATTERN_UPPER_TRACT)) {
                    name = SymptomConstants.PATTERN_UPPER_TRACT;
                } else if (adapter.getItem(i).toString().equalsIgnoreCase(SymptomConstants.PATTERN_PERIANAL)) {
                    name = SymptomConstants.PATTERN_PERIANAL;
                }

                editor.putString("pattern", name);
            });

            editor.apply();


            Intent intent = new Intent(FirstTime8NameActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}