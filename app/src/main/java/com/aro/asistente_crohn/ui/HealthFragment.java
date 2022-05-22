package com.aro.asistente_crohn.ui;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.expert.CrohnAnalyzer;
import com.aro.asistente_crohn.expert.SymptomConstants;
import com.aro.asistente_crohn.model.Health;
import com.aro.asistente_crohn.model.ItemViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HealthFragment extends Fragment {

    public HealthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_health, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        SharedPreferences preferences = ((HomeActivity) requireActivity()).getSharedPreferences("com.aro.asistente_crohn_preferences", MODE_PRIVATE);

        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("es", "ES"));
        TextView textDate = view.findViewById(R.id.textDate);
        textDate.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));

        CardView cardViewRegistries = view.findViewById(R.id.card_viewRegistries);
        cardViewRegistries.setOnClickListener(view1 -> ((HomeActivity) requireActivity()).openFragment(new HealthFragmentConsult(), Calendar.getInstance().getTime()));

        viewModel.getTodayHealth().observe(getViewLifecycleOwner(), healthList -> {
            List<Health> cacheTodayHealthList = new ArrayList<>();
            Health health = new Health();
            if (!healthList.isEmpty()) {
                cacheTodayHealthList = healthList;
                health = cacheTodayHealthList.get(0);
            }

            TextView courageValue = view.findViewById(R.id.valueCourage);
            String emoji = "😁";
            if(health.getCourage()==0) emoji="😭";
            else if(health.getCourage()==1) emoji="😔";
            else if(health.getCourage()==2) emoji="😑";
            else if(health.getCourage()==3) emoji="😊";
            courageValue.setText(emoji);

            TextView patternValue = view.findViewById(R.id.infoPattern);
            patternValue.setText(this.getActualPattern(preferences.getString("pattern", null)));

            TextView patternPositivityValue = view.findViewById(R.id.infoPatternPositivity);
            if(health.getRelatedSymptoms().equalsIgnoreCase(preferences.getString("pattern", null))){
                patternPositivityValue.setText(R.string.positive);
            } else patternPositivityValue.setText(R.string.negative);

            TextView positivityValue = view.findViewById(R.id.infoPositivity);
            if(health.getCrohnActive()) {
                positivityValue.setText(R.string.positive);
            } else positivityValue.setText(R.string.negative);

            //Calling expert
            CrohnAnalyzer analyzer = new CrohnAnalyzer(viewModel, getViewLifecycleOwner(), preferences);
            analyzer.analyze();
        });

        CardView cardAddCourage = view.findViewById(R.id.card_addCourage);
        cardAddCourage.setOnClickListener(view12 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view12.getContext());
            ViewGroup viewGroup = view12.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(view12.getContext()).inflate(R.layout.dialog_courage, viewGroup, false);
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.findViewById(R.id.buttonOk).setOnClickListener(view1 -> alertDialog.dismiss());
            TextView date = alertDialog.findViewById(R.id.date);
            date.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));


            Date before = Calendar.getInstance().getTime();
            Date after = Calendar.getInstance().getTime();
            before.setHours(00); before.setMinutes(00); before.setSeconds(00);
            after.setHours(23); after.setMinutes(59); after.setSeconds(59);

            viewModel.getSelectedDayHealth(before, after).observe(getViewLifecycleOwner(), healthList -> {
                List<Health> cacheHealthList = new ArrayList<>();
                GridLayout courageGrid = alertDialog.findViewById(R.id.courageGrid);
                Health health = new Health();
                if (!healthList.isEmpty()) {
                    cacheHealthList.addAll(healthList);

                    TextView text2 = (TextView) courageGrid.getChildAt(cacheHealthList.get(0).getCourage());
                    text2.setTextSize(44);
                }

                for (int i = 0; i < courageGrid.getChildCount(); i++) {
                    TextView text = (TextView) courageGrid.getChildAt(i);
                    text.setOnClickListener(view13 -> {

                        text.setTextSize(44);

                        //we need to reajust other child in case they were selected before
                        for (int i1 = 0; i1 < courageGrid.getChildCount(); i1++) {
                            TextView text2 = (TextView) courageGrid.getChildAt(i1);
                            if (!text2.equals(text)) {
                                text2.setTextSize(32);
                            }
                        }

                        //Adding actual courage to Health
                        //relatedSymptoms arraylist in Health only shows symptoms if crohnActive==true
                        health.setCourage(Integer.parseInt(text.getContentDescription().toString()));

                        if(cacheHealthList.isEmpty()){
                            viewModel.insertHealth(health);
                        } else {
                            //we need to replace courage
                            Health health1 = cacheHealthList.get(0);
                            health1.setCourage(health.getCourage());
                            viewModel.updateHealth(health1);
                        }
                    });
                }
            });
        });
    }

    private String getActualPattern(String pattern){
        if(pattern.equalsIgnoreCase(SymptomConstants.PATTERN_ILEOCOLITIS)){
            return "Enfermedad de Crohn -> Ileocolitis";
        } else if(pattern.equalsIgnoreCase(SymptomConstants.PATTERN_ILEITIS)){
            return "Enfermedad de Crohn -> Ileitis";
        } else if(pattern.equalsIgnoreCase(SymptomConstants.PATTERN_COLITIS)){
            return "Enfermedad de Crohn -> Colitis";
        } else if(pattern.equalsIgnoreCase(SymptomConstants.PATTERN_UPPER_TRACT)){
            return "Enfermedad de Crohn -> Gastrointestinal alta";
        } else if(pattern.equalsIgnoreCase(SymptomConstants.PATTERN_PERIANAL)){
            return "Enfermedad de Crohn -> Perianal";
        } else {
            return "Enfermedad de Crohn -> Actualiza más tarde";
        }
    }
}