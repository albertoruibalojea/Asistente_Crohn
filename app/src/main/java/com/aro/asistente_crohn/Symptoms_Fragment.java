package com.aro.asistente_crohn;

import android.icu.text.AlphabeticIndex;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.aro.asistente_crohn.model.ItemViewModel;
import com.aro.asistente_crohn.model.MyListAdapter;
import com.aro.asistente_crohn.model.Symptom;

import java.util.List;

public class Symptoms_Fragment extends Fragment {


    private ItemViewModel viewModel;
    private RecyclerView recyclerView;

    public Symptoms_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment Symptoms_Fragment.
     */
    public static Symptoms_Fragment newInstance() {
        Symptoms_Fragment fragment = new Symptoms_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_symptoms, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        System.out.println(viewModel.getAllSymptoms().getValue());
        System.out.println(viewModel.getAllSymptoms().getValue().get(0));
        System.out.println(viewModel.getAllSymptoms().getValue().size());

        Symptom[] myListData = new Symptom[100];
        for(int i=0; i < viewModel.allSymptoms.getValue().size(); i++){
            myListData[i] = viewModel.allSymptoms.getValue().get(i);
        }

        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);



        /*viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MyListAdapter adapter = new MyListAdapter(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        viewModel.getAllSymptoms().observe(getActivity(), new Observer<List<Symptom>>() {
            @Override
            public void onChanged(List<Symptom> records) {
                adapter.updateWith(records);
            }
        });*/

        return rootView;
    }
}