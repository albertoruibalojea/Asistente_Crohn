package com.aro.asistente_crohn.ui;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.Food;
import com.aro.asistente_crohn.model.ItemViewModel;
import com.aro.asistente_crohn.model.Symptom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView displayName;
    List<String> notifiedFood;
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
        SharedPreferences preferences = ((HomeActivity) requireActivity()).getSharedPreferences("ASISTENTE_CROHN_PREFS", MODE_PRIVATE);
        displayName.setText(preferences.getString("username", null));

        this.generateClickeableLayouts();

        //Check whether or not symptoms or food changes to detect Health issues
        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        notifiedFood = new ArrayList<>();
        viewModel.getTodaySymptoms().observe(getViewLifecycleOwner(), todaysSymptomList -> relateSymptomsFood(viewModel, todaysSymptomList));
    }

    public void relateSymptomsFood(ItemViewModel viewModel, List<Symptom> todaysSymptomList){
        List<Symptom> cacheTodaySymptomList = new ArrayList<>();
        if (!todaysSymptomList.isEmpty()) {
            cacheTodaySymptomList.addAll(todaysSymptomList);
        }

        viewModel.getTodayFoods().observe(getViewLifecycleOwner(), todayFoodList -> {
            List<Food> cacheTodaysFoodList = new ArrayList<>();
            if (!todayFoodList.isEmpty()) {
                cacheTodaysFoodList.addAll(todayFoodList);
            }

            //For each symptom from today, we get the days when the user had each symptom
            for(Symptom s : cacheTodaySymptomList){
                viewModel.getBySymptom(s.getName()).observe(getViewLifecycleOwner(), sameSymptomList -> {
                    List<Symptom> cacheSameSymptomList = new ArrayList<>();
                    if (!sameSymptomList.isEmpty()) {
                        cacheSameSymptomList.addAll(sameSymptomList);
                    }

                    //We iterate the days when the user had this symptom
                    for(Symptom d : cacheSameSymptomList){
                        Date before = (Date) d.getRegisteredDate().clone();
                        Date after = (Date) d.getRegisteredDate().clone();
                        before.setHours(00); before.setMinutes(00); before.setSeconds(00);
                        after.setHours(23); after.setMinutes(59); after.setSeconds(59);

                        //For each day with the same symptoms, we look for a common food
                        viewModel.getSelectedDayFoods(before, after).observe(getViewLifecycleOwner(), selectedDayFoodList -> {
                            List<Food> cacheSelectedFoodList = new ArrayList<>();
                            if (!selectedDayFoodList.isEmpty()) {
                                cacheSelectedFoodList.addAll(selectedDayFoodList);
                            }

                            //Boolean to avois sending the same notification to the user until the food is set to forbidden true
                            boolean ableToCompare = true;
                            if(!notifiedFood.isEmpty()){
                                for(String f : notifiedFood){
                                    for(Food f2 : cacheSelectedFoodList){
                                        if(f.equalsIgnoreCase(f2.getName())){
                                            ableToCompare = false;
                                        }
                                    }
                                }
                            }

                            if(ableToCompare){
                                //And now we just compare the 2 Food arrays (today and other) looking for similarities
                                for(Food f1 : cacheTodaysFoodList){
                                    for(Food f2 : cacheSelectedFoodList){
                                        if(!f1.getEatenDate().equals(f2.getEatenDate()) && f1.getName().equalsIgnoreCase(f2.getName()) && Boolean.TRUE.equals(!f1.getForbidden())){
                                            //It means the same food was eaten and the same symptom was there!!
                                            String description = "Has tenido el síntoma " + s.getName() + " al comer " +
                                                    f1.getName() + " durante más de una ocasión. Considera añadirlo a tu lista.";
                                            createNotification("Nuevo alimento desaconsejado", description, f1);
                                            notifiedFood.add(f1.getName());
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

    public void createNotification(String title, String description, Food f1){

        int num = (int) System.currentTimeMillis();

        NotificationChannel channel= new NotificationChannel(FOOD_ALERT,FOOD_ALERT, NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager =requireActivity().getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(requireActivity(), AlertDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("FOOD_ID", f1.getId());
        intent.putExtra("DESCRIPTION", description);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireActivity(), num, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireActivity(),FOOD_ALERT);
        builder.setContentTitle(title);
        builder.setContentText(f1.getName());
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentIntent(pendingIntent);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(description));
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(requireActivity());
        managerCompat.notify(num,builder.build());
    }

    public void generateClickeableLayouts(){

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