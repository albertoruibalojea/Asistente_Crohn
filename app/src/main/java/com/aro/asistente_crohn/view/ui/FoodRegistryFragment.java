package com.aro.asistente_crohn.view.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.Food;
import com.aro.asistente_crohn.model.FoodRepo;
import com.aro.asistente_crohn.view.viewmodel.FoodListAdapter;
import com.aro.asistente_crohn.view.viewmodel.IgnoreAccentsArrayAdapter;
import com.aro.asistente_crohn.view.viewmodel.ItemViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FoodRegistryFragment extends Fragment {

    List<String> foodNameList;
    String paramDate;

    public FoodRegistryFragment() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_registry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss zzz", new Locale("es", "ES"));
        Date date = Calendar.getInstance().getTime();
        Date before = Calendar.getInstance().getTime();
        Date after = Calendar.getInstance().getTime();

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

        before.setDate(date.getDate());before.setMonth(date.getMonth()); before.setYear(date.getYear());before.setHours(00); before.setMinutes(00); before.setSeconds(00);
        after.setDate(date.getDate());after.setMonth(date.getMonth()); after.setYear(date.getYear());after.setHours(23); after.setMinutes(59); after.setSeconds(59);


        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        foodNameList = new ArrayList<>();

        //Observer from Repository to lookup the LiveData
        Date finalDate = date;
        viewModel.getAllRepoFoods().observe(getViewLifecycleOwner(), foodsList -> {
            if (foodsList != null) {
                for(int i=0; i< foodsList.size(); i++){
                    foodNameList.add(foodsList.get(i).getName());
                }
            }

            //Creating the instance of ArrayAdapter containing list of language names
            IgnoreAccentsArrayAdapter<String> adapter = new IgnoreAccentsArrayAdapter<>(requireActivity().getBaseContext(),android.R.layout.select_dialog_item,foodNameList);
            //Getting the instance of AutoCompleteTextView
            AutoCompleteTextView actv =  (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
            actv.setThreshold(1);//will start working from first character

            actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
            actv.setOnItemClickListener((adapterView, view1, i, l) -> {
                Food food = new Food(adapter.getItem(i));
                food.setEatenDate(finalDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(finalDate);
                calendar.add(Calendar.DAY_OF_YEAR, 90);
                food.setLimitDate(calendar.getTime());

                //add Food to DAO
                viewModel.insertFood(food);
                adapter.notifyDataSetChanged();
                actv.performCompletion();
                actv.setText("");
            });
        });

        Button buttonCreateFood = view.findViewById(R.id.buttonCreateFood);
        buttonCreateFood.setOnClickListener(view12 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view12.getContext());
            ViewGroup viewGroup = view12.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(view12.getContext()).inflate(R.layout.dialog_newfood, viewGroup, false);
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            EditText newFood = alertDialog.findViewById(R.id.newFood);
            alertDialog.findViewById(R.id.buttonOk).setOnClickListener(view1 -> {
                if(!newFood.getText().toString().isEmpty() && newFood.getText().toString().length() > 0){
                    FoodRepo newFoodRepo = new FoodRepo();
                    newFoodRepo.setName(newFood.getText().toString());
                    viewModel.insertFoodRepo(newFoodRepo);
                }

                alertDialog.dismiss();
            });
        });

        //Observer from Repository to lookup the LiveData
        viewModel.getSelectedDayFoods(before, after).observe(getViewLifecycleOwner(), foodsList -> setSelectedDay(view, viewModel, foodsList));
    }

    private void setSelectedDay(View view, ItemViewModel viewModel, List<Food> foodsList){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        List<Food> cacheFoodList = new ArrayList<>();
        if (!foodsList.isEmpty()) {
            cacheFoodList.addAll(foodsList);
        }

        FoodListAdapter adapter = new FoodListAdapter(cacheFoodList, viewModel, view, false);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}