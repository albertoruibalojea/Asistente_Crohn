package com.aro.asistente_crohn.firsttime;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aro.asistente_crohn.R;

public class Fragment1st6Security extends Fragment {

    public Fragment1st6Security() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button btn = requireActivity().findViewById(R.id.btn);
        btn.setOnClickListener(view -> {
            Intent terms = new Intent(getActivity(), FirstTime7TermsActivity.class);
            startActivity(terms);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ProgressBar progressBar = ((Activity1stTime) requireActivity()).progressBar;
        progressBar.setProgress(70, false);

        TextView progressText = ((Activity1stTime) requireActivity()).progressText;
        progressText.setText(R.string.guia_inicio_70_completado);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_1st_6_security, container, false);
    }
}