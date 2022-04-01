package com.aro.asistente_crohn;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aro.asistente_crohn.model.Health;
import com.aro.asistente_crohn.model.Symptom;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;


public class Fragment_Symptoms extends Fragment {

    private ArrayList<Symptom> selectedSymptoms;
    private Health health;
    private Button btn;

    public Fragment_Symptoms() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Symptoms newInstance() {
        Fragment_Symptoms fragment = new Fragment_Symptoms();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedSymptoms = new ArrayList<>();
        health = new Health();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_symptoms, container, false);

        btn = (Button) rootView.findViewById(R.id.btn);

        GridLayout symptomGrid = (GridLayout) rootView.findViewById(R.id.symptomGrid);
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
                }
            });
        }

        GridLayout courageGrid = (GridLayout) rootView.findViewById(R.id.courageGrid);
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

                    btn.setEnabled(true);
                }
            });
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we need to check if there is symptoms and the courage level
                //we sent this to the Parent Activity

            }
        });


        return rootView;
    }
}