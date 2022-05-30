package com.aro.asistente_crohn.view.ui.firsttime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.aro.asistente_crohn.R;

public class FirstTime7TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time7_terms);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(view -> {

            SharedPreferences.Editor editor = getSharedPreferences("com.aro.asistente_crohn_preferences", MODE_PRIVATE).edit();
            editor.putBoolean("termsAccepted", true);
            editor.apply();

            Intent intent = new Intent(FirstTime7TermsActivity.this, FirstTime8NameActivity.class);
            startActivity(intent);
            finish();
        });
    }
}