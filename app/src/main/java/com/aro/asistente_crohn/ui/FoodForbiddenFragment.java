package com.aro.asistente_crohn.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.Food;
import com.aro.asistente_crohn.model.FoodListAdapter;
import com.aro.asistente_crohn.model.FoodRepo;
import com.aro.asistente_crohn.model.IgnoreAccentsArrayAdapter;
import com.aro.asistente_crohn.model.ItemViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FoodForbiddenFragment extends Fragment {

    List<String> foodNameList;

    public FoodForbiddenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_forbidden, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        foodNameList = new ArrayList<>();

        //Observer from Repository to lookup the LiveData
        viewModel.getForbiddenFoods().observe(getViewLifecycleOwner(), forbiddenList -> {
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            List<Food> cacheFoodList = new ArrayList<>();
            if (!forbiddenList.isEmpty()) {
                cacheFoodList.addAll(forbiddenList);
            } else {
                this.sendAlert(view, "¿Qué te hace daño?", "Añade lo que te hace daño sobre el icono X en un alimento registrado");
            }

            FoodListAdapter adapter = new FoodListAdapter(cacheFoodList, viewModel, view);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);

            //Search foods and add to forbidden
            viewModel.getAllRepoFoods().observe(getViewLifecycleOwner(), new Observer<List<FoodRepo>>() {
                @Override
                public void onChanged(List<FoodRepo> foodRepos) {
                    List<FoodRepo> foodRepoList = new ArrayList<>();
                    if (!foodRepos.isEmpty()) {
                        foodRepoList.addAll(foodRepos);
                    }

                    for (int i = 0; i < foodRepoList.size(); i++) {
                        String name = foodRepoList.get(i).getName();
                        Iterator it = cacheFoodList.iterator();
                        while (it.hasNext()) {
                            Food foodCheck = (Food) it.next();
                            if (foodCheck.getName().equalsIgnoreCase(foodRepoList.get(i).getName())) {
                                name = foodRepoList.get(i).getName().toUpperCase() + " 🚫";
                            }
                        }

                        foodNameList.add(name);
                    }

                    //Creating the instance of ArrayAdapter containing list of language names
                    IgnoreAccentsArrayAdapter<String> adapter2 = new IgnoreAccentsArrayAdapter<>(requireActivity().getBaseContext(), android.R.layout.select_dialog_item, foodNameList);
                    //Getting the instance of AutoCompleteTextView
                    AutoCompleteTextView actv = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
                    actv.setThreshold(1);//will start working from first character

                    actv.setAdapter(adapter2);//setting the adapter data into the AutoCompleteTextView
                    actv.setOnItemClickListener((adapterView, view1, i, l) -> {

                        Food food = new Food(adapter2.getItem(i));
                        Date date = Calendar.getInstance().getTime();

                        date.setYear(1989);
                        date.setMonth(12);
                        date.setDate(13);
                        food.setEatenDate(date);
                        date.setYear(2200);
                        date.setMonth(1);
                        date.setDate(1);
                        food.setLimitDate(date);

                        boolean flag = false;
                        Iterator it = cacheFoodList.iterator();
                        while (it.hasNext()) {
                            Food foodUpdate = (Food) it.next();
                            if (food.getName().contains("🚫")) {
                                String[] nameWithoutIcon = food.getName().split(" 🚫");
                                food.setName(nameWithoutIcon[0]);
                            }
                            if (foodUpdate.getName().equalsIgnoreCase(food.getName())) {
                                foodUpdate.setForbidden(false);
                                viewModel.updateFood(foodUpdate);
                                adapter2.notifyDataSetChanged();
                                actv.performCompletion();
                                flag = true;
                            }
                        }

                        if (!flag) {
                            food.setForbidden(true);

                            //add Food to DAO
                            viewModel.insertFood(food);
                            adapter2.notifyDataSetChanged();
                            actv.performCompletion();
                        }

                    });
                }

            });
        });
    }

    public void sendAlert(View view, String title, String description){
        //Success alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_notification, viewGroup, false);

        TextView t = dialogView.findViewById(R.id.title);
        t.setText(title);
        t = dialogView.findViewById(R.id.description);
        t.setText(description);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.findViewById(R.id.buttonOk).setOnClickListener(view1 -> alertDialog.dismiss());
    }
}