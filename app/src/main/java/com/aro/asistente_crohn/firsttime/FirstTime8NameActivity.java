package com.aro.asistente_crohn.firsttime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

        //saving the username
        SharedPreferences.Editor editor = getSharedPreferences("com.aro.asistente_crohn_preferences", MODE_PRIVATE).edit();

        //Spinner values
        Spinner spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = SymptomConstants.PATTERN_ILEOCOLITIS;
                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Ileitis")) {
                    name = SymptomConstants.PATTERN_ILEITIS;
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Colitis")) {
                    name = SymptomConstants.PATTERN_COLITIS;
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Gastrointestinal alta")) {
                    name = SymptomConstants.PATTERN_UPPER_TRACT;
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Perianal")) {
                    name = SymptomConstants.PATTERN_PERIANAL;
                }

                editor.putString("pattern", name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                editor.putString("pattern", SymptomConstants.PATTERN_ILEOCOLITIS);
            }
        });


        //Once the user writes the username, we get it and save it into SharedPreferences
        EditText username = findViewById(R.id.username);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(view -> {

            if (username.getText().length() > 0 && username.getText().equals("Tu nombre")) {
                editor.putString("username", username.getText().toString());
            } else {
                editor.putString("username", "Usuario/a");
            }

        });

        editor.apply();


        Intent intent = new Intent(FirstTime8NameActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}