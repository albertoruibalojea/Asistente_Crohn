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
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.aro.asistente_crohn.model.Symptom;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Fragment_Symptoms extends Fragment {

    private ArrayList<Symptom> selectedSymptoms;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_symptoms, container, false);

        GridLayout grid = (GridLayout) rootView.findViewById(R.id.symptomGrid);
        int childCount = grid.getChildCount();

        for (int i= 0; i < childCount; i++){
            CardView card = (CardView) grid.getChildAt(i);
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

                    }
                }
            });
        }

        return rootView;
    }
}