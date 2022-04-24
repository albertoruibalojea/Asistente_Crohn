package com.aro.asistente_crohn.ui;

import android.app.AlertDialog;
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
import com.aro.asistente_crohn.model.ItemViewModel;
import com.aro.asistente_crohn.model.Symptom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SymptomsRegistryFragment extends Fragment {

    String paramDate;

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

        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss zzz", new Locale("es", "ES"));
        Date date = Calendar.getInstance().getTime();

        if(!paramDate.isEmpty()){
            try {
                date = simpleDateFormat.parse(paramDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("es", "ES"));
        TextView textDate = view.findViewById(R.id.textDate);
        textDate.setText(simpleDateFormat.format(date));


        ArrayList<Symptom> selectedSymptoms = new ArrayList<>();
        Button btn = view.findViewById(R.id.btn);

        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        GridLayout symptomGrid = view.findViewById(R.id.symptomGrid);
        for (int i = 0; i < symptomGrid.getChildCount(); i++) {
            CardView card = (CardView) symptomGrid.getChildAt(i);

            //we need the actual registry date
            Date finalDate = date;
            Date before = Calendar.getInstance().getTime();
            Date after = Calendar.getInstance().getTime();
            before.setDate(date.getDate());before.setHours(00); before.setMinutes(00); before.setSeconds(00);
            after.setDate(date.getDate());after.setHours(23); after.setMinutes(59); after.setSeconds(59);

            card.setOnClickListener(view12 -> {
                btn.setEnabled(true);

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
                    //Observer from Repository to lookup the LiveData
                    viewModel.getSelectedDaySymptoms(before, after).observe(requireActivity(), symptomList -> {
                        List<Symptom> cacheSymptomList = new ArrayList<>();
                        if (symptomList != null) {
                            cacheSymptomList.addAll(symptomList);
                        }

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
                        }
                    });


                } //Unselect a symptom
                else if (card.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.violeta)) {
                    card.setCardBackgroundColor(getResources().getColor(R.color.negroGris));

                    //it means it was selected before, so we put off the symptom from the array
                    selectedSymptoms.removeIf(o -> o.getName().equalsIgnoreCase(card.getChildAt(0).getContentDescription().toString()));
                }
            });
        }

        btn.setOnClickListener(view14 -> {
            //we need to check if there is symptoms and the courage level
            if (!selectedSymptoms.isEmpty()) {

                //we save the data into the Room Persistence Database
                for (Symptom s : selectedSymptoms) {
                    viewModel.insertSymptom(s);
                }

                //reset ui
                GridLayout symptomGrid1 = requireView().findViewById(R.id.symptomGrid);
                for (int i = 0; i < symptomGrid1.getChildCount(); i++) {
                    CardView card = (CardView) symptomGrid1.getChildAt(i);
                    if (card.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.violeta)) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.negroGris));
                    }
                }

                btn.setEnabled(false);

                //Success alert dialog
                this.sendAlert(view);
            }
        });
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
}