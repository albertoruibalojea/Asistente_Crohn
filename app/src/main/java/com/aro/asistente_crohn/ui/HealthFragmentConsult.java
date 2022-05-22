package com.aro.asistente_crohn.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.database.DateConverter;
import com.aro.asistente_crohn.expert.SymptomConstants;
import com.aro.asistente_crohn.model.Health;
import com.aro.asistente_crohn.model.ItemViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HealthFragmentConsult  extends Fragment {

    final Date[] date = {Calendar.getInstance().getTime()};
    final Date[] before = {Calendar.getInstance().getTime()};
    final Date[] after = {Calendar.getInstance().getTime()};

    public HealthFragmentConsult(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_health_consult, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        //Set default date
        this.setDates(view);

        //Listener when the user sets a date in Calendar
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setDate(DateConverter.fromDate(date[0]));
        calendarView.setMaxDate(System.currentTimeMillis());
        calendarView.setOnDateChangeListener((calendarView1, year, month, dayOfMonth) -> {

            //Set date
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            date[0] = calendar.getTime();

            setDates(view);

            setData(viewModel, view);
        });
    }

    private void setData(ItemViewModel viewModel, View view){

        SharedPreferences preferences = ((HomeActivity) requireActivity()).getSharedPreferences("com.aro.asistente_crohn_preferences", MODE_PRIVATE);

        //Observer from Repository to lookup the LiveData
        viewModel.getSelectedDayHealth(before[0], after[0]).observe(getViewLifecycleOwner(), healthList -> {
            List<Health> cacheTodayHealthList = new ArrayList<>();
            Health health = new Health();
            if (!healthList.isEmpty()) {
                cacheTodayHealthList = healthList;
                health = cacheTodayHealthList.get(0);
            }

            TextView courageValue = view.findViewById(R.id.valueCourage);
            String emoji = "😁";
            if(health.getCourage()==0) emoji="😭";
            else if(health.getCourage()==1) emoji="😔";
            else if(health.getCourage()==2) emoji="😑";
            else if(health.getCourage()==3) emoji="😊";
            courageValue.setText(emoji);

            TextView patternValue = view.findViewById(R.id.infoPattern);
            patternValue.setText(this.getActualPattern(preferences.getString("pattern", null)));

            TextView patternPositivityValue = view.findViewById(R.id.infoPatternPositivity);
            if(health.getRelatedSymptoms().equalsIgnoreCase(preferences.getString("pattern", null))){
                patternPositivityValue.setText(R.string.positive);
            } else patternPositivityValue.setText(R.string.negative);

            TextView positivityValue = view.findViewById(R.id.infoPositivity);
            if(health.getCrohnActive()) {
                positivityValue.setText(R.string.positive);
            } else positivityValue.setText(R.string.negative);
        });
    }

    private void setDates(View view){
        before[0].setDate(date[0].getDate());before[0].setMonth(date[0].getMonth()); before[0].setYear(date[0].getYear());before[0].setHours(00); before[0].setMinutes(00); before[0].setSeconds(00);
        after[0].setDate(date[0].getDate());after[0].setMonth(date[0].getMonth()); after[0].setYear(date[0].getYear());after[0].setHours(23); after[0].setMinutes(59); after[0].setSeconds(59);

        TextView dateTextView = view.findViewById(R.id.dateTextView);
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("es", "ES"));
        dateTextView.setText(simpleDateFormat.format(before[0]));
    }

    private String getActualPattern(String pattern){
        if(pattern.equalsIgnoreCase(SymptomConstants.PATTERN_ILEOCOLITIS)){
            return "Enfermedad de Crohn -> Ileocolitis";
        } else if(pattern.equalsIgnoreCase(SymptomConstants.PATTERN_ILEITIS)){
            return "Enfermedad de Crohn -> Ileitis";
        } else if(pattern.equalsIgnoreCase(SymptomConstants.PATTERN_COLITIS)){
            return "Enfermedad de Crohn -> Colitis";
        } else if(pattern.equalsIgnoreCase(SymptomConstants.PATTERN_UPPER_TRACT)){
            return "Enfermedad de Crohn -> Gastrointestinal alta";
        } else if(pattern.equalsIgnoreCase(SymptomConstants.PATTERN_PERIANAL)){
            return "Enfermedad de Crohn -> Perianal";
        } else {
            return "Enfermedad de Crohn -> No identificado";
        }
    }
}
