package com.aro.asistente_crohn.view.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aro.asistente_crohn.R;

public class RecommendationsFragment extends Fragment {

    public RecommendationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommendations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView cardConsult = view.findViewById(R.id.card_openINF);
        cardConsult.setOnClickListener(view1 -> ((HomeActivity) requireActivity()).openFragment(new RecommendationsSubLevelFragment(), "INF"));

        CardView cardNutrition = view.findViewById(R.id.card_openNUT);
        cardNutrition.setOnClickListener(view1 -> ((HomeActivity) requireActivity()).openFragment(new RecommendationsSubLevelFragment(), "NUT"));

        CardView cardGraphicRes = view.findViewById(R.id.card_openGRA);
        cardGraphicRes.setOnClickListener(view1 -> ((HomeActivity) requireActivity()).openFragment(new RecommendationsSubLevelFragment(), "GRA"));

        CardView cardTests = view.findViewById(R.id.card_openBOT);
        cardTests.setOnClickListener(view1 -> ((HomeActivity) requireActivity()).openFragment(new RecommendationsSubLevelFragment(), "BOT"));
    }
}