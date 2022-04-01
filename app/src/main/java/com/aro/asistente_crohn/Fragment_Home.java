package com.aro.asistente_crohn;

import static android.content.Context.MODE_PRIVATE;
import android.content.SharedPreferences;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Fragment_Home extends Fragment {

    TextView displayName;

    public Fragment_Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FragmentFirst.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Home newInstance() {
        Fragment_Home fragment = new Fragment_Home();
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //Get the username and show it on Home screen
        displayName = (TextView) rootView.findViewById(R.id.displayName);
        SharedPreferences preferences = getActivity().getSharedPreferences("ASISTENTE_CROHN_PREFS", MODE_PRIVATE);
        displayName.setText(preferences.getString("username", null));

        this.generateClickeableLayouts(rootView);

        return rootView;
    }

    public void generateClickeableLayouts(View rootView){
        /*RelativeLayout card_state = (RelativeLayout) rootView.findViewById(R.id.card_state);
        card_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Home_Activity) getActivity()).openFragment(new Fragment_State());
            }
        });*/

        RelativeLayout card_symptoms = (RelativeLayout) rootView.findViewById(R.id.card_symptoms);
        card_symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Home_Activity) getActivity()).openFragment(new Fragment_Symptoms());
            }
        });
    }
}