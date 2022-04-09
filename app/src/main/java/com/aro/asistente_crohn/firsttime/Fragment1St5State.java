package com.aro.asistente_crohn.firsttime;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aro.asistente_crohn.R;


public class Fragment1St5State extends Fragment {

    public Fragment1St5State() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button btn = requireActivity().findViewById(R.id.btn);
        btn.setOnClickListener(view -> ((Activity1stTime) requireActivity()).openFragment(new Fragment1st6Security()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ProgressBar progressBar = ((Activity1stTime) requireActivity()).progressBar;
        progressBar.setProgress(55, false);

        TextView progressText = ((Activity1stTime) requireActivity()).progressText;
        progressText.setText(R.string.guia_inicio_55_completado);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_1st_5_state, container, false);
    }
}