package com.aro.asistente_crohn.ui;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.aro.asistente_crohn.BuildConfig;
import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.ItemViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        SharedPreferences preferences = ((HomeActivity) requireActivity()).getSharedPreferences("com.aro.asistente_crohn_preferences", MODE_PRIVATE);

        Preference privacy = (Preference) findPreference("privacy");
        privacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                ((HomeActivity) requireActivity()).openFragment(new PrivacyFragment());
                return true;
            }
        });

        Preference feedback = (Preference) findPreference("feedback");
        feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent email= new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto:asistente_crohn@outlook.es"));
                email.putExtra(Intent.EXTRA_SUBJECT, "[FEEDBACK APP] app:android-"+ BuildConfig.VERSION_NAME);
                startActivity(email);
                return true;
            }
        });

        Preference delete = (Preference) findPreference("delete");
        delete.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                notifyBeforeDeletingData(getView());
                return true;
            }
        });

        EditTextPreference username = (EditTextPreference) findPreference("username");
        username.setText(preferences.getString("username", null));
        username.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                //saving the username
                SharedPreferences.Editor editor = ((HomeActivity) requireActivity()).getSharedPreferences("com.aro.asistente_crohn_preferences", MODE_PRIVATE).edit();

                if(username.getText().length() > 0){
                    editor.putString("username", username.getText().toString());
                } else {
                    editor.putString("username", "Usuario/a");
                }

                editor.apply();
            }
        });

        SwitchPreferenceCompat alerts = (SwitchPreferenceCompat) findPreference("app_alerts");
        alerts.setOnPreferenceClickListener(preference -> {
            SharedPreferences.Editor editor = ((HomeActivity) requireActivity()).getSharedPreferences("com.aro.asistente_crohn_preferences", MODE_PRIVATE).edit();
            if(alerts.isChecked()){
               editor.putBoolean("app_alerts", true);
            } else {
                editor.putBoolean("app_alerts", false);
            }

            editor.apply();
            return true;
        });
    }

    public void notifyBeforeDeletingData(View view){
        //Success alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_notification_2buttons, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.findViewById(R.id.buttonCancel).setOnClickListener(view1 -> alertDialog.dismiss());
        alertDialog.findViewById(R.id.buttonOk).setOnClickListener(view1 -> {
            ItemViewModel viewModel = new ViewModelProvider(((HomeActivity) requireActivity())).get(ItemViewModel.class);
            viewModel.deleteAllDBData();
            ((HomeActivity) requireActivity()).finish();
        });
    }

}