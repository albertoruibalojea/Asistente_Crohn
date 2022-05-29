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

    public static final String PREFERENCES = "com.aro.asistente_crohn_preferences";
    public static final String USERNAME = "username";
    public static final String DAYS_TO_ANALYZE = "daysToAnalyze";
    public static final String TYPE = "pattern";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        SharedPreferences preferences = ((HomeActivity) requireActivity()).getSharedPreferences(PREFERENCES, MODE_PRIVATE);

        Preference privacy = findPreference("privacy");
        assert privacy != null;
        privacy.setOnPreferenceClickListener(preference -> {
            ((HomeActivity) requireActivity()).openFragment(new PrivacyFragment());
            return true;
        });

        Preference feedback = findPreference("feedback");
        assert feedback != null;
        feedback.setOnPreferenceClickListener(preference -> {
            Intent email= new Intent(Intent.ACTION_SENDTO);
            email.setData(Uri.parse("mailto:asistente_crohn@outlook.es"));
            email.putExtra(Intent.EXTRA_SUBJECT, "[FEEDBACK APP] app:android-"+ BuildConfig.VERSION_NAME);
            startActivity(email);
            return true;
        });

        Preference delete = findPreference("delete");
        assert delete != null;
        delete.setOnPreferenceClickListener(preference -> {
            notifyBeforeDeletingData(requireView());
            return true;
        });

        EditTextPreference username = (EditTextPreference) findPreference(USERNAME);
        assert username != null;
        username.setText(preferences.getString(USERNAME, null));
        username.setOnBindEditTextListener(editText -> {
            //saving the username
            SharedPreferences.Editor editor = ((HomeActivity) requireActivity()).getSharedPreferences(PREFERENCES, MODE_PRIVATE).edit();

            if(Objects.requireNonNull(username.getText()).length() > 0){
                editor.putString(USERNAME, username.getText());
            } else {
                editor.putString(USERNAME, "Usuario/a");
            }

            editor.apply();
        });

        EditTextPreference daysToAnalyze = (EditTextPreference) findPreference(DAYS_TO_ANALYZE);
        assert daysToAnalyze != null;
        daysToAnalyze.setText(preferences.getString(DAYS_TO_ANALYZE, null));
        daysToAnalyze.setOnBindEditTextListener(editText -> {
            //saving the number
            SharedPreferences.Editor editor = ((HomeActivity) requireActivity()).getSharedPreferences(PREFERENCES, MODE_PRIVATE).edit();

            int day = Integer.parseInt(Objects.requireNonNull(daysToAnalyze.getText()));
            if(day > 2){
                editor.putString(DAYS_TO_ANALYZE, daysToAnalyze.getText());
            }

            editor.apply();
        });

        Preference type = findPreference(TYPE);
        assert type != null;
        type.setOnPreferenceClickListener(preference -> {
            setPattern(requireView(), requireContext(), preferences);
            return true;
        });

        SwitchPreferenceCompat alerts = (SwitchPreferenceCompat) findPreference("app_alerts");
        assert alerts != null;
        alerts.setOnPreferenceClickListener(preference -> {
            SharedPreferences.Editor editor = ((HomeActivity) requireActivity()).getSharedPreferences(PREFERENCES, MODE_PRIVATE).edit();
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.spinner_values, R.layout.spinner_item_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);


        int spinnerPosition = adapter.getPosition(this.getActualPattern(preferences));
        //set the default according to value
        spinner.setSelection(spinnerPosition);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = SymptomConstants.TYPE_ILEOCOLITIS;
                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Ileitis")) {
                    name = SymptomConstants.TYPE_ILEITIS;
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Colitis")) {
                    name = SymptomConstants.TYPE_COLITIS;
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Gastrointestinal alta")) {
                    name = SymptomConstants.TYPE_UPPER_TRACT;
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Perianal")) {
                    name = SymptomConstants.TYPE_PERIANAL;
                }

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(TYPE, name);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //empty
            }
        });

        alertDialog.findViewById(R.id.buttonOk).setOnClickListener(view1 -> alertDialog.dismiss());
    }

    public String getActualPattern(SharedPreferences preferences){
        if(preferences.getString(TYPE, null).equalsIgnoreCase("Ileocolitis")){
            return SymptomConstants.TYPE_ILEOCOLITIS;
        } else if(preferences.getString(TYPE, null).equalsIgnoreCase("Ileitis")){
            return SymptomConstants.TYPE_ILEITIS;
        } else if(preferences.getString(TYPE, null).equalsIgnoreCase("Colitis")){
            return SymptomConstants.TYPE_COLITIS;
        } else if(preferences.getString(TYPE, null).equalsIgnoreCase("Gastrointestinal alta")){
            return SymptomConstants.TYPE_UPPER_TRACT;
        } else if(preferences.getString(TYPE, null).equalsIgnoreCase("Perianal")){
            return SymptomConstants.TYPE_PERIANAL;
        }

        return null;
    }
}