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
import android.widget.GridLayout;
import android.widget.TextView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.Health;
import com.aro.asistente_crohn.model.ItemViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

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

        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("es", "ES"));
        TextView textDate = view.findViewById(R.id.textDate);
        textDate.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));

        //CardView cardViewRegistries = view.findViewById(R.id.card_viewRegistries);
        //cardViewRegistries.setOnClickListener(view1 -> ((HomeActivity) requireActivity()).openFragment(new HealthRegistryFragment(), Calendar.getInstance().getTime()));

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
}