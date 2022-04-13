package com.aro.asistente_crohn.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aro.asistente_crohn.R;

public class HomeFragment extends Fragment {

    TextView displayName;

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
    }

    public void generateClickeableLayouts(){

        RelativeLayout cardSymptoms = (RelativeLayout) ((HomeActivity) requireActivity()).findViewById(R.id.card_symptoms);
        cardSymptoms.setOnClickListener(view -> ((HomeActivity) requireActivity()).openFragment(new SymptomsFragment()));

        RelativeLayout cardFood = (RelativeLayout) ((HomeActivity) requireActivity()).findViewById(R.id.card_food);
        cardFood.setOnClickListener(view -> ((HomeActivity) requireActivity()).openFragment(new FoodFragment()));
    }
}