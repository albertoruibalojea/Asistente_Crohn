package com.aro.asistente_crohn;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.aro.asistente_crohn.model.Health;
import com.aro.asistente_crohn.model.ItemViewModel;
import com.aro.asistente_crohn.model.Symptom;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;


public class Symptoms_Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ItemViewModel viewModel;
    private ArrayList<Symptom> selectedSymptoms;
    private Health health;
    private Button btn;
    BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);

        selectedSymptoms = new ArrayList<>();
        health = new Health();
        btn = (Button) findViewById(R.id.btn);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.symptoms);

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);


        CardView card_viewRegistres = (CardView) findViewById(R.id.card_viewRegistres);
        card_viewRegistres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new Symptoms_Fragment());
            }
        });


        GridLayout symptomGrid = (GridLayout) findViewById(R.id.symptomGrid);
        for (int i= 0; i < symptomGrid.getChildCount(); i++){
            CardView card = (CardView) symptomGrid.getChildAt(i);
            card.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    //Checking the actual color
                    //Select a symptom
                    if(card.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.negroGris)){
                        card.setCardBackgroundColor(getResources().getColor(R.color.violeta));

                        //If a a symptom is selected, we add it to the arraylist
                        Symptom symptom = new Symptom();
                        symptom.setName(card.getChildAt(0).getContentDescription().toString());
                        symptom.setRegisteredDate(Calendar.getInstance().getTime());

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(symptom.getRegisteredDate());
                        calendar.add(Calendar.DAY_OF_YEAR, 90);
                        symptom.setLimitDate(calendar.getTime());

                        //adding it to the arraylist
                        selectedSymptoms.add(symptom);

                    } //Unselect a symptom
                    else if(card.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.violeta)){
                        card.setCardBackgroundColor(getResources().getColor(R.color.negroGris));

                        //it means it was selected before, so we put off the symptom from the array
                        Iterator it = selectedSymptoms.iterator();
                        while(it.hasNext()){
                            if( ((Symptom) it.next()).getName().equalsIgnoreCase(card.getChildAt(0).getContentDescription().toString()) ){
                                it.remove();
                            }
                        }
                    }

                    if(health != null){
                        btn.setEnabled(true);
                    }
                }
            });
        }

        GridLayout courageGrid = (GridLayout) findViewById(R.id.courageGrid);
        for (int i= 0; i < courageGrid.getChildCount(); i++){
            TextView text = (TextView) courageGrid.getChildAt(i);
            text.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    text.setTextSize(48);

                    //we need to reajust other child in case they were selected before
                    for (int i= 0; i < courageGrid.getChildCount(); i++){
                        TextView text2 = (TextView) courageGrid.getChildAt(i);
                        if(!text2.equals(text)){
                            text2.setTextSize(36);
                        }
                    }

                    //Adding actual courage to Health
                    //relatedSymptoms arraylist in Health only shows symptoms if crohnActive==true
                    health.setCourage(Integer.parseInt(text.getContentDescription().toString()));

                    if(!selectedSymptoms.isEmpty()){
                        btn.setEnabled(true);
                    }

                }
            });
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we need to check if there is symptoms and the courage level
                if(!selectedSymptoms.isEmpty() && (health.getCourage() != null)){

                    //we save the data into the Room Persistence Database
                    for(Symptom s : selectedSymptoms){
                        viewModel.insertSymptom(s);
                    }

                    viewModel.insertHealth(health);

                    //System.out.println(viewModel.getAllSymptoms().getValue());
                    //System.out.println(viewModel.getAllSymptoms().getValue().get(0));
                    //System.out.println(viewModel.getAllSymptoms().getValue().size());
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(Symptoms_Activity.this, Home_Activity.class);
                startActivity(intent);
                finish();
            /*case R.id.symptoms:
                openFragment(new Symptoms_Activity());
                return true;*/
            /*case R.id.state:
                openFragment(new Fragment_State());
                return true;*/
        }
        return false;
    }

    void openFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.constraintView, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}