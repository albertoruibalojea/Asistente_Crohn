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

public class Fragment_1st_4Recommendations extends Fragment {

    public Fragment_1st_4Recommendations() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment Fragment_1st_4Recommendations.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_1st_4Recommendations newInstance() {
        Fragment_1st_4Recommendations fragment = new Fragment_1st_4Recommendations();
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
                ((Activity_1stTime)getActivity()).openFragment(new Fragment_1st_5State());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ProgressBar progressBar = ((Activity_1stTime)getActivity()).progressBar;
        progressBar.setProgress(40, false);

        TextView progressText = ((Activity_1stTime)getActivity()).progressText;
        progressText.setText("Guía de Inicio    -   40% completado");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_1st_4_recommendations, container, false);
    }
}