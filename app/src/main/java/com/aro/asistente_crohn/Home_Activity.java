package com.aro.asistente_crohn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aro.asistente_crohn.model.ItemViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home_Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    TextView displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Get the username and show it on Home screen
        displayName = (TextView) findViewById(R.id.displayName);
        SharedPreferences preferences = this.getSharedPreferences("ASISTENTE_CROHN_PREFS", MODE_PRIVATE);
        displayName.setText(preferences.getString("username", null));

        this.generateClickeableLayouts();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.home:
                openFragment(new Fragment_Home());
                return true;*/
            case R.id.symptoms:
                Intent intent = new Intent(Home_Activity.this, Symptoms_Activity.class);
                startActivity(intent);
                finish();
                return true;
            /*case R.id.state:
                openFragment(new Fragment_State());
                return true;*/
        }
        return false;
    }

    public void generateClickeableLayouts(){
        /*RelativeLayout card_state = (RelativeLayout) rootView.findViewById(R.id.card_state);
        card_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Home_Activity) getActivity()).openFragment(new Fragment_State());
            }
        });*/

        RelativeLayout card_symptoms = (RelativeLayout) findViewById(R.id.card_symptoms);
        card_symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Activity.this, Symptoms_Activity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

}