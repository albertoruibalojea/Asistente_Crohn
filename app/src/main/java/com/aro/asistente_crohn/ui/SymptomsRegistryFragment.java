package com.aro.asistente_crohn.ui;

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

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.ItemViewModel;
import com.aro.asistente_crohn.model.MyListAdapter;
import com.aro.asistente_crohn.model.Symptom;

import java.util.ArrayList;
import java.util.List;

public class SymptomsRegistryFragment extends Fragment {

    private ItemViewModel viewModel;
    private RecyclerView recyclerView;

    public SymptomsRegistryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_symptoms_registry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        viewModel.getAllSymptoms().observe(getActivity(), new Observer<List<Symptom>>() {
            @Override
            public void onChanged(List<Symptom> symptomList) {
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
                List<Symptom> cacheSymptomList = new ArrayList<>();
                if (symptomList != null) {
                    cacheSymptomList.addAll(symptomList);
                }
                MyListAdapter adapter = new MyListAdapter(cacheSymptomList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }
        });

    }
}