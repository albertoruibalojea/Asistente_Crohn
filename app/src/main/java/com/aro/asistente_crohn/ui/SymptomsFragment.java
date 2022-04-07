package com.aro.asistente_crohn.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.Health;
import com.aro.asistente_crohn.model.ItemViewModel;
import com.aro.asistente_crohn.model.Symptom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class SymptomsFragment extends Fragment {

    private ItemViewModel viewModel;
    private ArrayList<Symptom> selectedSymptoms;
    private Health health;
    private Button btn;

    public SymptomsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_symptoms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectedSymptoms = new ArrayList<>();
        health = new Health();
        btn = (Button) view.findViewById(R.id.btn);

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);


        CardView card_viewRegistres = (CardView) view.findViewById(R.id.card_viewRegistres);
        card_viewRegistres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SymptomsActivity) requireActivity()).openFragment(new SymptomsRegistryFragment());
            }
        });


        GridLayout symptomGrid = (GridLayout) view.findViewById(R.id.symptomGrid);
        for (int i = 0; i < symptomGrid.getChildCount(); i++) {
            CardView card = (CardView) symptomGrid.getChildAt(i);
            card.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //Checking the actual color
                    //Select a symptom
                    if (card.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.negroGris)) {
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
                    else if (card.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.violeta)) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.negroGris));

                        //it means it was selected before, so we put off the symptom from the array
                        Iterator it = selectedSymptoms.iterator();
                        while (it.hasNext()) {
                            if (((Symptom) it.next()).getName().equalsIgnoreCase(card.getChildAt(0).getContentDescription().toString())) {
                                it.remove();
                            }
                        }
                    }

                    if (health.getCourage() != null) {
                        btn.setEnabled(true);
                    }
                }
            });
        }

        GridLayout courageGrid = (GridLayout) view.findViewById(R.id.courageGrid);
        for (int i = 0; i < courageGrid.getChildCount(); i++) {
            TextView text = (TextView) courageGrid.getChildAt(i);
            text.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    text.setTextSize(48);

                    //we need to reajust other child in case they were selected before
                    for (int i = 0; i < courageGrid.getChildCount(); i++) {
                        TextView text2 = (TextView) courageGrid.getChildAt(i);
                        if (!text2.equals(text)) {
                            text2.setTextSize(36);
                        }
                    }

                    //Adding actual courage to Health
                    //relatedSymptoms arraylist in Health only shows symptoms if crohnActive==true
                    health.setCourage(Integer.parseInt(text.getContentDescription().toString()));

                    if (!selectedSymptoms.isEmpty()) {
                        btn.setEnabled(true);
                    }

                }
            });
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we need to check if there is symptoms and the courage level
                if (!selectedSymptoms.isEmpty() && (health.getCourage() != null)) {

                    //we save the data into the Room Persistence Database
                    for (Symptom s : selectedSymptoms) {
                        viewModel.insertSymptom(s);
                    }

                    viewModel.insertHealth(health);

                    //System.out.println(viewModel.getAllSymptoms().getValue());
                    //System.out.println(viewModel.getAllSymptoms().getValue().get(0));
                    //System.out.println(viewModel.getAllSymptoms().getValue().size());


                    //reset ui
                    GridLayout symptomGrid = (GridLayout) requireView().findViewById(R.id.symptomGrid);
                    for (int i = 0; i < symptomGrid.getChildCount(); i++) {
                        CardView card = (CardView) symptomGrid.getChildAt(i);
                        if (card.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.violeta)) {
                            card.setCardBackgroundColor(getResources().getColor(R.color.negroGris));
                        }
                    }

                    for (int i = 0; i < courageGrid.getChildCount(); i++) {
                        TextView text2 = (TextView) courageGrid.getChildAt(i);
                        text2.setTextSize(36);
                    }

                    btn.setEnabled(false);
                }
            }
        });
    }
}