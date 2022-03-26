package com.aro.asistente_crohn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Fragment_1st_3Food extends Fragment {

    public Fragment_1st_3Food() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment Fragment_1st_3Food.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_1st_3Food newInstance() {
        Fragment_1st_3Food fragment = new Fragment_1st_3Food();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button btn = ((Activity_1stTime)getActivity()).findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity_1stTime)getActivity()).openFragment(new Fragment_1st_4Recommendations());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ProgressBar progressBar = ((Activity_1stTime)getActivity()).progressBar;
        progressBar.setProgress(25, false);

        TextView progressText = ((Activity_1stTime)getActivity()).progressText;
        progressText.setText("Guía de Inicio    -   25% completado");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_1st_3_food, container, false);
    }
}