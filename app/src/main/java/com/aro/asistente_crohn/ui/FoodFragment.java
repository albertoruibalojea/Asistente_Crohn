package com.aro.asistente_crohn.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.Food;
import com.aro.asistente_crohn.model.FoodListAdapter;
import com.aro.asistente_crohn.model.ItemViewModel;
import com.aro.asistente_crohn.model.Symptom;
import com.aro.asistente_crohn.model.SymptomListAdapter;

import java.util.ArrayList;
import java.util.List;

public class FoodFragment extends Fragment{

    List<String> foodNameList;


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

        CardView cardViewRegistries = view.findViewById(R.id.card_viewRegistries);
        cardViewRegistries.setOnClickListener(view1 -> ((HomeActivity) requireActivity()).openFragment(new FoodRegistryFragment()));

        CardView cardViewForbidden = view.findViewById(R.id.card_viewForbiddenFood);
        cardViewForbidden.setOnClickListener(view1 -> ((HomeActivity) requireActivity()).openFragment(new FoodForbiddenFragment()));


        foodNameList = new ArrayList<>();
        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        //Observer from Repository to lookup the LiveData
        viewModel.getAllRepoFoods().observe(requireActivity(), foodsList -> {
            if (foodsList != null) {
                for(int i=0; i< foodsList.size(); i++){
                    foodNameList.add(foodsList.get(i).getName());
                }
            }

            //Creating the instance of ArrayAdapter containing list of language names
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity().getBaseContext(),android.R.layout.select_dialog_item,foodNameList);
            //Getting the instance of AutoCompleteTextView
            AutoCompleteTextView actv =  (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
            actv.setThreshold(1);//will start working from first character
            actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
            actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Food food = new Food(adapter.getItem(i));

                    //add Food to DAO
                    viewModel.insertFood(food);
                    adapter.notifyDataSetChanged();
                    actv.performCompletion();
                }
            });
        });

        //Observer from Repository to lookup the LiveData
        viewModel.getTodayFoods().observe(requireActivity(), foods -> {
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            List<Food> cacheFoodsList = new ArrayList<>();
            if (foods != null) {
                cacheFoodsList.addAll(foods);
            }
            FoodListAdapter adapter = new FoodListAdapter(cacheFoodsList, viewModel, view);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        });
    }

    public void sendAlert(View view, String title, String description){
        //Success alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.notification_dialog, viewGroup, false);

        TextView t = dialogView.findViewById(R.id.title);
        t.setText(title);
        t = dialogView.findViewById(R.id.description);
        t.setText(description);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}