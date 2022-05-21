package com.aro.asistente_crohn.ui;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.expert.SymptomConstants;
import com.aro.asistente_crohn.model.Food;
import com.aro.asistente_crohn.model.Health;
import com.aro.asistente_crohn.model.ItemViewModel;
import com.aro.asistente_crohn.model.Symptom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeFragment extends Fragment {

    TextView displayName;
    public static final String FOOD_ALERT = "FOOD_ALERT";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get the username and show it on Home screen
        displayName = (TextView) ((HomeActivity) requireActivity()).findViewById(R.id.displayName);
        SharedPreferences preferences = ((HomeActivity) requireActivity()).getSharedPreferences("com.aro.asistente_crohn_preferences", MODE_PRIVATE);
        displayName.setText(preferences.getString("username", null));
        if (preferences.getString("daysToAnalyze", null) == null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("daysToAnalyze", "3");
            editor.apply();
        }
        if(preferences.getString("pattern", null) == null){
            this.setPattern(view, getContext(), preferences);
        }


        this.generateClickeableLayouts();

        //Check whether or not symptoms or food changes to detect Health issues
        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.getTodaySymptoms().observe(getViewLifecycleOwner(), todaysSymptomList -> relateSymptomsFood(viewModel, todaysSymptomList));
    }

    public void relateSymptomsFood(ItemViewModel viewModel, List<Symptom> todaysSymptomList) {
        List<Symptom> cacheTodaySymptomList = new ArrayList<>();
        if (!todaysSymptomList.isEmpty()) {
            cacheTodaySymptomList.addAll(todaysSymptomList);
        }

        viewModel.getTodayHealth().observe(getViewLifecycleOwner(), todayHealth -> {
            if (todayHealth.isEmpty()) {
                //it means it does not exist, and we must create it
                Health health = new Health();
                health.setCourage(health.getCourage());
                viewModel.insertHealth(health);
            }
        });

        viewModel.getTodayFoods().observe(getViewLifecycleOwner(), todayFoodList -> {
            List<Food> cacheTodaysFoodList = new ArrayList<>();
            if (!todayFoodList.isEmpty()) {
                cacheTodaysFoodList.addAll(todayFoodList);
            }

            //For each symptom from today, we get the days when the user had each symptom
            for (Symptom s : cacheTodaySymptomList) {
                viewModel.getBySymptom(s.getName()).observe(getViewLifecycleOwner(), sameSymptomList -> {
                    List<Symptom> cacheSameSymptomList = new ArrayList<>();
                    if (!sameSymptomList.isEmpty()) {
                        cacheSameSymptomList.addAll(sameSymptomList);
                    }

                    AtomicInteger count = new AtomicInteger();

                    //We iterate the days when the user had this symptom
                    for (Symptom d : cacheSameSymptomList) {
                        Date before = (Date) d.getRegisteredDate().clone();
                        Date after = (Date) d.getRegisteredDate().clone();
                        before.setHours(00);
                        before.setMinutes(00);
                        before.setSeconds(00);
                        after.setHours(23);
                        after.setMinutes(59);
                        after.setSeconds(59);

                        //For each day with the same symptoms, we look for a common food
                        viewModel.getSelectedDayFoods(before, after).observe(getViewLifecycleOwner(), selectedDayFoodList -> {
                            List<Food> cacheSelectedFoodList = new ArrayList<>();
                            if (!selectedDayFoodList.isEmpty()) {
                                cacheSelectedFoodList.addAll(selectedDayFoodList);
                            }


                            for (Food f1 : cacheTodaysFoodList) {
                                //Boolean to avois sending the same notification to the user until the food is set to forbidden true
                                boolean ableToCompare = true;
                                if (!HomeActivity.notifiedFood.isEmpty()) {
                                    for (String f : HomeActivity.notifiedFood) {
                                        for (Food f1a : cacheTodaysFoodList) {
                                            if (f.equalsIgnoreCase(f1a.getName())) {
                                                ableToCompare = false;
                                            }
                                        }
                                    }
                                }

                                if (ableToCompare) {
                                    for (Food f2 : cacheSelectedFoodList) {
                                        if (!f1.getEatenDate().equals(f2.getEatenDate()) && f1.getName().equalsIgnoreCase(f2.getName()) && Boolean.TRUE.equals(!f1.getForbidden())) {
                                            count.getAndIncrement();
                                            if (count.get() == 5) {
                                                //It means the same food was eaten and the same symptom was there!!
                                                String description = "Has tenido el síntoma " + s.getName() + " al comer " +
                                                        f1.getName() + " durante más de 5 ocasiones. Considera añadirlo a tu lista.";
                                                SharedPreferences preferences = ((HomeActivity) requireActivity()).getSharedPreferences("com.aro.asistente_crohn_preferences", MODE_PRIVATE);
                                                if (preferences.getBoolean("app_alerts", true)) {
                                                    createNotification("Nuevo alimento desaconsejado", description, f1);
                                                    HomeActivity.notifiedFood.add(f1.getName());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public void createNotification(String title, String description, Food f1) {

        int num = (int) System.currentTimeMillis();

        NotificationChannel channel = new NotificationChannel(FOOD_ALERT, FOOD_ALERT, NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = requireActivity().getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(requireActivity(), AlertDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("FOOD_ID", f1.getId());
        intent.putExtra("DESCRIPTION", description);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireActivity(), num, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireActivity(), FOOD_ALERT);
        builder.setContentTitle(title);
        builder.setContentText(f1.getName());
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentIntent(pendingIntent);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(description));
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(requireActivity());
        managerCompat.notify(num, builder.build());
    }

    public void setPattern(View view, Context context, SharedPreferences preferences) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.select_pattern, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Spinner spinner = alertDialog.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = SymptomConstants.PATTERN_GENERIC;
                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase(SymptomConstants.PATTERN_SMALL_BOWEL)) {
                    name = SymptomConstants.PATTERN_SMALL_BOWEL;
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase(SymptomConstants.PATTERN_COLON)) {
                    name = SymptomConstants.PATTERN_COLON;
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase(SymptomConstants.PATTERN_UPPER_TRACT)) {
                    name = SymptomConstants.PATTERN_UPPER_TRACT;
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase(SymptomConstants.PATTERN_PERIANAL)) {
                    name = SymptomConstants.PATTERN_PERIANAL;
                }

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("pattern", name);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("pattern", SymptomConstants.PATTERN_GENERIC);
                editor.apply();
            }
        });

        alertDialog.findViewById(R.id.buttonOk).setOnClickListener(view1 -> alertDialog.dismiss());
    }

    public void generateClickeableLayouts() {

        RelativeLayout cardHealth = (RelativeLayout) ((HomeActivity) requireActivity()).findViewById(R.id.card_health);
        cardHealth.setOnClickListener(view -> ((HomeActivity) requireActivity()).openFragment(new HealthFragment()));

        RelativeLayout cardSymptoms = (RelativeLayout) ((HomeActivity) requireActivity()).findViewById(R.id.card_symptoms);
        cardSymptoms.setOnClickListener(view -> ((HomeActivity) requireActivity()).openFragment(new SymptomsFragment()));

        RelativeLayout cardFood = (RelativeLayout) ((HomeActivity) requireActivity()).findViewById(R.id.card_food);
        cardFood.setOnClickListener(view -> ((HomeActivity) requireActivity()).openFragment(new FoodFragment()));

        ImageView settings = (ImageView) ((HomeActivity) requireActivity()).findViewById(R.id.settingsView);
        settings.setOnClickListener(view -> ((HomeActivity) requireActivity()).openFragment(new SettingsFragment()));
    }
}