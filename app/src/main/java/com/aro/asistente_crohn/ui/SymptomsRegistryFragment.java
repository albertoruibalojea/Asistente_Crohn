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
import java.util.Objects;

public class SymptomsRegistryFragment extends Fragment {

    public SymptomsRegistryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_symptoms_registry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        //Observer from Repository to lookup the LiveData
        viewModel.getAllSymptoms().observe(requireActivity(), symptomList -> {
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            List<Symptom> cacheSymptomList = new ArrayList<>();
            if (symptomList != null) {
                cacheSymptomList.addAll(symptomList);
            }
            MyListAdapter adapter = new MyListAdapter(cacheSymptomList, viewModel);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        });
    }
}