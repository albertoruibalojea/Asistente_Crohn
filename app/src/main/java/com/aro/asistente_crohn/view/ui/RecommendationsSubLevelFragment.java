package com.aro.asistente_crohn.view.ui;

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
import android.widget.TextView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.Recommendation;
import com.aro.asistente_crohn.view.viewmodel.ItemViewModel;
import com.aro.asistente_crohn.view.viewmodel.RecommendationListAdapter;
import com.aro.asistente_crohn.view.viewmodel.SymptomListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendationsSubLevelFragment extends Fragment {

    String type;

    public RecommendationsSubLevelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommendations_sublevel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!type.isEmpty()){
            ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);

            viewModel.getAllRecommendations().observe(getViewLifecycleOwner(), recommendationsList -> {
                List<Recommendation> cacheRecommendationsList = new ArrayList<>();
                if (recommendationsList != null) {
                    cacheRecommendationsList.addAll(recommendationsList);
                }

                TextView textType = view.findViewById(R.id.textType);

                if(!cacheRecommendationsList.isEmpty()){
                    if(type.equalsIgnoreCase("INF")){
                        textType.setText(R.string.recommendations_titleINF);
                        this.getMyType(viewModel, view, cacheRecommendationsList, "INF/");

                    } else if(type.equalsIgnoreCase("NUT")){
                        textType.setText(R.string.recommendations_titleNUT);
                        this.getMyType(viewModel, view, cacheRecommendationsList, "NUT/");

                    } else if(type.equalsIgnoreCase("GRA")){
                        textType.setText(R.string.recommendations_titleGRA);
                        this.getMyType(viewModel, view, cacheRecommendationsList, "GRA/");

                    } else if(type.equalsIgnoreCase("BOT")){
                        textType.setText(R.string.recommendations_titleBOT);
                        this.getMyType(viewModel, view, cacheRecommendationsList, "BOT/");

                    }
                }
            });
        }
    }

    public void getMyType(ItemViewModel viewModel, View view, List<Recommendation> cacheList, String type){

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        List<Recommendation> sameTypeRecommendationsList = cacheList.stream()
                // Filter by any condition
                .filter(field -> field.getTitle().startsWith(type))
                // Collect your filtered fields
                .collect(Collectors.toList());

        RecommendationListAdapter adapter = new RecommendationListAdapter(sameTypeRecommendationsList, viewModel, view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}