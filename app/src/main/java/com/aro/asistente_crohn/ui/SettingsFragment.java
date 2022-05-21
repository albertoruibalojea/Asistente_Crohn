package com.aro.asistente_crohn.ui;

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
import com.aro.asistente_crohn.expert.SymptomConstants;
import com.aro.asistente_crohn.model.ItemViewModel;

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
        spinner.setOnItemClickListener((adapterView, view1, i, l) -> {
            String name = SymptomConstants.PATTERN_GENERIC;
            if (adapter.getItem(i).toString().equalsIgnoreCase(SymptomConstants.PATTERN_SMALL_BOWEL)) {
                name = SymptomConstants.PATTERN_SMALL_BOWEL;
            } else if (adapter.getItem(i).toString().equalsIgnoreCase(SymptomConstants.PATTERN_COLON)) {
                name = SymptomConstants.PATTERN_COLON;
            } else if (adapter.getItem(i).toString().equalsIgnoreCase(SymptomConstants.PATTERN_UPPER_TRACT)) {
                name = SymptomConstants.PATTERN_UPPER_TRACT;
            } else if (adapter.getItem(i).toString().equalsIgnoreCase(SymptomConstants.PATTERN_PERIANAL)) {
                name = SymptomConstants.PATTERN_PERIANAL;
            }

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("pattern", name);
            editor.apply();
        });

        alertDialog.findViewById(R.id.buttonOk).setOnClickListener(view1 -> alertDialog.dismiss());
    }

    public String getActualPattern(SharedPreferences preferences){
        if(preferences.getString("pattern", null).equalsIgnoreCase("Genérico")){
            return SymptomConstants.PATTERN_GENERIC;
        } else if(preferences.getString("pattern", null).equalsIgnoreCase("Intestino delgado")){
            return SymptomConstants.PATTERN_SMALL_BOWEL;
        } else if(preferences.getString("pattern", null).equalsIgnoreCase("Colon")){
            return SymptomConstants.PATTERN_COLON;
        } else if(preferences.getString("pattern", null).equalsIgnoreCase("Estómago y tracto superior")){
            return SymptomConstants.PATTERN_UPPER_TRACT;
        } else if(preferences.getString("pattern", null).equalsIgnoreCase("Perianal")){
            return SymptomConstants.PATTERN_PERIANAL;
        }

        return null;
    }
}