package com.aro.asistente_crohn.view.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.service.expert.SymptomConstants;
import com.aro.asistente_crohn.view.ui.firsttime.Activity1stTime;
import com.aro.asistente_crohn.view.ui.firsttime.FirstTime8NameActivity;
import com.aro.asistente_crohn.view.viewmodel.ItemViewModel;
import com.aro.asistente_crohn.model.Symptom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SymptomsRegistryFragment extends Fragment {

    String paramDate;
    List<Symptom> cacheSymptomList = new ArrayList<>();
    ArrayList<Symptom> selectedSymptoms = new ArrayList<>();
    ArrayList<String> deletedSymptoms = new ArrayList<>();

    public SymptomsRegistryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paramDate = getArguments().getString("date");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_symptoms_registry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss zzz", new Locale("es", "ES"));
        Date date = Calendar.getInstance().getTime();

        if(!paramDate.isEmpty()){
            try {
                date = simpleDateFormat.parse(paramDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //we need the actual registry date
        Date finalDate = date;
        Date before = Calendar.getInstance().getTime();
        Date after = Calendar.getInstance().getTime();
        before.setDate(date.getDate());before.setHours(00); before.setMinutes(00); before.setSeconds(00);
        after.setDate(date.getDate());after.setHours(23); after.setMinutes(59); after.setSeconds(59);

        //Observer from Repository to lookup the Symptom LiveData
        viewModel.getSelectedDaySymptoms(before, after).observe(getViewLifecycleOwner(), symptomList -> {
            if (symptomList != null) {
                cacheSymptomList.addAll(symptomList);
            }
        });

        ImageView infoBtn = view.findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(this::openInfo);

        simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("es", "ES"));
        TextView textDate = view.findViewById(R.id.textDate);
        textDate.setText(simpleDateFormat.format(date));

        Button btn = view.findViewById(R.id.btn);
        btn.setEnabled(true);

        GridLayout symptomGrid = view.findViewById(R.id.symptomGrid);
        //handler to create the splash screen
        int splashTimeOut = 2000;
        new Handler().postDelayed(() -> this.checkSelected(view, symptomGrid), splashTimeOut);
        for (int i = 0; i < symptomGrid.getChildCount(); i++) {
            CardView card = (CardView) symptomGrid.getChildAt(i);
            card.setOnClickListener(view12 -> {
                //Checking the actual color
                //Select a symptom
                if (card.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.negroGris)) {
                    card.setCardBackgroundColor(getResources().getColor(R.color.violeta));

                    //If a a symptom is selected, we add it to the arraylist
                    Symptom symptom = new Symptom();
                    symptom.setName(card.getChildAt(0).getContentDescription().toString());
                    symptom.setRegisteredDate(finalDate);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(finalDate);
                    calendar.add(Calendar.DAY_OF_YEAR, 90);
                    symptom.setLimitDate(calendar.getTime());

                    //check if the symptom is not already registered today
                    //If not empty, it means the user has registered symptoms today
                    int eq = 0;
                    if(!cacheSymptomList.isEmpty()){
                        for(Symptom s : cacheSymptomList){
                            if(s.getName().equals(symptom.getName())){
                                eq = 1;
                                break;
                            }
                        }
                    }

                    //If the symptom is not registered today, we add it to the array
                    if(cacheSymptomList.isEmpty() || eq == 0) {
                        //adding it to the arraylist
                        selectedSymptoms.add(symptom);
                        deletedSymptoms.removeIf(o -> o.equalsIgnoreCase(symptom.getName()));
                    }

                } //Unselect a symptom
                else if (card.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.violeta)) {
                    card.setCardBackgroundColor(getResources().getColor(R.color.negroGris));

                    //it means it was selected before, so we put off the symptom from the array
                    selectedSymptoms.removeIf(o -> o.getName().equalsIgnoreCase(card.getChildAt(0).getContentDescription().toString()));
                    deletedSymptoms.add(card.getChildAt(0).getContentDescription().toString());
                }
            });
        }

        btn.setOnClickListener(view14 -> {
            //we need to check if there is symptoms and the courage level
            if (!selectedSymptoms.isEmpty() || !deletedSymptoms.isEmpty()) {

                //we save the data into the Room Persistence Database
                for (Symptom s : selectedSymptoms) {
                    viewModel.insertSymptom(s);
                }

                //We delete the data is needed
                for(Symptom s : cacheSymptomList){
                    for(String t : deletedSymptoms){
                        if(s.getName().equalsIgnoreCase(t)){
                            viewModel.deleteSymptom(s);
                            break;
                        }
                    }
                }

                //reset ui
                GridLayout symptomGrid1 = requireView().findViewById(R.id.symptomGrid);
                for (int i = 0; i < symptomGrid1.getChildCount(); i++) {
                    CardView card = (CardView) symptomGrid1.getChildAt(i);
                    if (card.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.violeta)) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.negroGris));
                    }
                }

                //Success alert dialog
                this.sendAlert(view);
            }
        });
    }

    public void checkSelected(View view, GridLayout symptomGrid){
        for (int i = 0; i < symptomGrid.getChildCount(); i++) {
            CardView card = (CardView) symptomGrid.getChildAt(i);

            if(!cacheSymptomList.isEmpty()){
                for(Symptom s : cacheSymptomList){
                    if(card.getChildAt(0).getContentDescription().toString().equalsIgnoreCase(s.getName())){
                        card.setCardBackgroundColor(view.getContext().getResources().getColor(R.color.violeta));
                        selectedSymptoms.add(s);
                    }
                }
            }
        }
    }

    public void sendAlert(View view){
        //Success alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_notification, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.findViewById(R.id.buttonOk).setOnClickListener(view1 -> alertDialog.dismiss());
    }

    public void openInfo(View view){
        //Success alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.fragment_symptoms_registry_meaning, viewGroup, false);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.findViewById(R.id.dismiss).setOnClickListener(view1 -> alertDialog.dismiss());
    }
}