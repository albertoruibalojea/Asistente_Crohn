package com.aro.asistente_crohn.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.Food;
import com.aro.asistente_crohn.model.ItemViewModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class FoodFragment extends Fragment{

    String[] foodNameList;


    public FoodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        //Observer from Repository to lookup the LiveData
        viewModel.getAllFoods().observe(requireActivity(), foodsList -> {
            if (foodsList != null) {
                foodNameList = foodsList.toArray(new String[foodsList.size()]);
            }

            //Creating the instance of ArrayAdapter containing list of language names
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireActivity(),android.R.layout.select_dialog_item,foodNameList);
            //Getting the instance of AutoCompleteTextView
            AutoCompleteTextView actv =  (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
            actv.setThreshold(1);//will start working from first character
            actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        });

        /*foodNameList = new String[]{"Lion", "Tiger", "Dog",
                "Cat", "Tortoise", "Rat", "Elephant", "Fox",
                "Cow","Donkey","Monkey"};*/


    }
}