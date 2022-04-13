package com.aro.asistente_crohn.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.Food;

import java.util.ArrayList;

public class FoodFragment extends Fragment implements SearchView.OnQueryTextListener{

    ListView list;
    ListViewAdapter adapter;
    SearchView searchBox;
    String[] foodNameList;
    ArrayList<Food> arraylist = new ArrayList<Food>();
    boolean flag = false;

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

        // Generate sample data

        foodNameList = new String[]{"Lion", "Tiger", "Dog",
                "Cat", "Tortoise", "Rat", "Elephant", "Fox",
                "Cow","Donkey","Monkey"};

        // Locate the ListView in listview_main.xml
        list = (ListView) requireActivity().findViewById(R.id.listview);

        for (int i = 0; i < foodNameList.length; i++) {
            Food animalNames = new Food(foodNameList[i]);
            // Binds all strings into an array
            arraylist.add(animalNames);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(requireActivity(), arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter((ListAdapter) adapter);
        list.setVisibility(View.INVISIBLE);

        // Locate the EditText in listview_main.xml
        searchBox = (SearchView) requireActivity().findViewById(R.id.searchBox);
        searchBox.setOnQueryTextListener(this);
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag){
                    // means true
                    list.setVisibility(View.INVISIBLE);
                    flag = false;
                }
                else{
                    list.setVisibility(View.VISIBLE);
                    flag = true;
                }

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String text = s;
        adapter.filter(text);
        return false;
    }
}