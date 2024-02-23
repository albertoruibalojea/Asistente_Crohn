package com.aro.asistente_crohn.view.ui;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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

        buttonWeb.setOnClickListener(v -> {
            // Check if the fragment is attached to an activity
            if (isAdded() && getActivity() != null) {
                // Create an implicit intent to open the browser with a URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://linktr.ee/ibdassistant"));
                // Check if there is an activity that can handle the intent
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Start the intent from the fragment
                    startActivity(intent);
                }
            }
        });

        ImageView twitterIcon = view.findViewById(R.id.twitterIcon);
        ImageView instagramIcon = view.findViewById(R.id.instagramIcon);
        ImageView mailIcon = view.findViewById(R.id.mailIcon);

        twitterIcon.setOnClickListener(v -> openUrl("https://twitter.com/asistente_crohn"));
        instagramIcon.setOnClickListener(v -> openUrl("https://instagram.com/ibdassistant"));
        mailIcon.setOnClickListener(v -> openEmailClient("asistente_crohn@outlook.es"));
    }

    private void openUrl(String url) {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }

    private void openEmailClient(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}