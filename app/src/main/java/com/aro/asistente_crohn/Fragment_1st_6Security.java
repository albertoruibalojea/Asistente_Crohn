package com.aro.asistente_crohn;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Fragment_1st_6Security extends Fragment {

    public Fragment_1st_6Security() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment Fragment_1st_6Security.
     */
    public static Fragment_1st_6Security newInstance() {
        Fragment_1st_6Security fragment = new Fragment_1st_6Security();
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
                Intent terms = new Intent(getActivity(), FirstTime_7Terms_Activity.class);
                startActivity(terms);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ProgressBar progressBar = ((Activity_1stTime)getActivity()).progressBar;
        progressBar.setProgress(70, false);

        TextView progressText = ((Activity_1stTime)getActivity()).progressText;
        progressText.setText("Guía de Inicio    -   70% completado");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_1st_6_security, container, false);
    }
}