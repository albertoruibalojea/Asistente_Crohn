package com.aro.asistente_crohn.view.ui;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.aro.asistente_crohn.BuildConfig;
import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.service.expert.SymptomConstants;
import com.aro.asistente_crohn.view.viewmodel.ItemViewModel;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        SharedPreferences preferences = ((HomeActivity) requireActivity()).getSharedPreferences("com.aro.asistente_crohn_preferences", MODE_PRIVATE);

        Preference privacy = (Preference) findPreference("privacy");
        assert privacy != null;
        privacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(@NonNull Preference preference) {
                ((HomeActivity) requireActivity()).openFragment(new PrivacyFragment());
                return true;
            }
        });

        Preference feedback = (Preference) findPreference("feedback");
        assert feedback != null;
        feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Intent email= new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto:asistente_crohn@outlook.es"));
                email.putExtra(Intent.EXTRA_SUBJECT, "[FEEDBACK APP] app:android-"+ BuildConfig.VERSION_NAME);
                startActivity(email);
                return true;
            }
        });

        Preference delete = (Preference) findPreference("delete");
        assert delete != null;
        delete.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(@NonNull Preference preference) {
                notifyBeforeDeletingData(requireView());
                return true;
            }
        });

        EditTextPreference username = (EditTextPreference) findPreference("username");
        assert username != null;
        username.setText(preferences.getString("username", null));
        username.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                //saving the username
                SharedPreferences.Editor editor = ((HomeActivity) requireActivity()).getSharedPreferences("com.aro.asistente_crohn_preferences", MODE_PRIVATE).edit();

                if(Objects.requireNonNull(username.getText()).length() > 0){
                    editor.putString("username", username.getText());
                } else {
                    editor.putString("username", "Usuario/a");
                }

                editor.apply();
            }
        });

        EditTextPreference daysToAnalyze = (EditTextPreference) findPreference("daysToAnalyze");
        assert daysToAnalyze != null;
        daysToAnalyze.setText(preferences.getString("daysToAnalyze", null));
        daysToAnalyze.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                //saving the number
                SharedPreferences.Editor editor = ((HomeActivity) requireActivity()).getSharedPreferences("com.aro.asistente_crohn_preferences", MODE_PRIVATE).edit();

                int day = Integer.parseInt(Objects.requireNonNull(daysToAnalyze.getText()));
                if(day > 2){
                    editor.putString("daysToAnalyze", daysToAnalyze.getText());
                }

                editor.apply();
            }
        });

        Preference pattern = (Preference) findPreference("pattern");
        assert pattern != null;
        pattern.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(@NonNull Preference preference) {
                setPattern(requireView(), requireContext(), preferences);
                return true;
            }
        });

        SwitchPreferenceCompat alerts = (SwitchPreferenceCompat) findPreference("app_alerts");
        assert alerts != null;
        alerts.setOnPreferenceClickListener(preference -> {
            SharedPreferences.Editor editor = ((HomeActivity) requireActivity()).getSharedPreferences("com.aro.asistente_crohn_preferences", MODE_PRIVATE).edit();
            editor.putBoolean("app_alerts", alerts.isChecked());
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

    public void setPattern(View view, Context context, SharedPreferences preferences) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.select_pattern, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Spinner spinner = alertDialog.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);


        int spinnerPosition = adapter.getPosition(this.getActualPattern(preferences));
        //set the default according to value
        spinner.setSelection(spinnerPosition);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = SymptomConstants.PATTERN_ILEOCOLITIS;
                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Ileitis")) {
                    name = SymptomConstants.PATTERN_ILEITIS;
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Colitis")) {
                    name = SymptomConstants.PATTERN_COLITIS;
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Gastrointestinal alta")) {
                    name = SymptomConstants.PATTERN_UPPER_TRACT;
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Perianal")) {
                    name = SymptomConstants.PATTERN_PERIANAL;
                }

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("pattern", name);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alertDialog.findViewById(R.id.buttonOk).setOnClickListener(view1 -> alertDialog.dismiss());
    }

    public String getActualPattern(SharedPreferences preferences){
        if(preferences.getString("pattern", null).equalsIgnoreCase("Ileocolitis")){
            return SymptomConstants.PATTERN_ILEOCOLITIS;
        } else if(preferences.getString("pattern", null).equalsIgnoreCase("Ileitis")){
            return SymptomConstants.PATTERN_ILEITIS;
        } else if(preferences.getString("pattern", null).equalsIgnoreCase("Colitis")){
            return SymptomConstants.PATTERN_COLITIS;
        } else if(preferences.getString("pattern", null).equalsIgnoreCase("Gastrointestinal alta")){
            return SymptomConstants.PATTERN_UPPER_TRACT;
        } else if(preferences.getString("pattern", null).equalsIgnoreCase("Perianal")){
            return SymptomConstants.PATTERN_PERIANAL;
        }

        return null;
    }
}