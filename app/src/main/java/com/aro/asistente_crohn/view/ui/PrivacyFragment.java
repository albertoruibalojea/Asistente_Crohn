package com.aro.asistente_crohn.view.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aro.asistente_crohn.R;

public class PrivacyFragment extends Fragment {

    private Button buttonWeb;

    public PrivacyFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_privacy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.buttonWeb = (Button) view.findViewById(R.id.buttonWeb);

        buttonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos un intent implícito para abrir el navegador con una URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://linktr.ee/asistentecrohn"));
                // Comprobamos que haya una actividad que pueda manejar el intent
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Iniciamos el intent desde el fragmento
                    startActivity(intent);
                }
            }
        });
    }
}