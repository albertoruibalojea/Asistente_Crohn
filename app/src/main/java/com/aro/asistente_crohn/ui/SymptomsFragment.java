package com.aro.asistente_crohn.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.database.DateConverter;
import com.aro.asistente_crohn.model.Health;
import com.aro.asistente_crohn.model.ItemViewModel;
import com.aro.asistente_crohn.model.Symptom;
import com.aro.asistente_crohn.model.SymptomListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SymptomsFragment extends Fragment {

    final Date[] date = {Calendar.getInstance().getTime()};
    final Date[] before = {Calendar.getInstance().getTime()};
    final Date[] after = {Calendar.getInstance().getTime()};

    public SymptomsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_symptoms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);


        CardView cardViewRegistries = view.findViewById(R.id.card_addRegistries);
        cardViewRegistries.setOnClickListener(view1 -> ((HomeActivity) requireActivity()).openFragment(new SymptomsRegistryFragment(), Calendar.getInstance().getTime()));

        ImageView addSymptomsImg = view.findViewById(R.id.addSymptoms);
        addSymptomsImg.setOnClickListener(view1 -> ((HomeActivity) requireActivity()).openFragment(new SymptomsRegistryFragment(), date[0]));


        //Set default date
        this.setDates(view);

        //Listener when the user sets a date in Calendar
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setDate(DateConverter.fromDate(date[0]));
        calendarView.setOnDateChangeListener((calendarView1, year, month, dayOfMonth) -> {

            //Set date
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            date[0] = calendar.getTime();

            setDates(view);

            //Observer from Repository to lookup the LiveData
            viewModel.getSelectedDaySymptoms(before[0], after[0]).observe(requireActivity(), symptomList -> setSelectedDay(view, viewModel, symptomList));
        });

        //Default view is the actual Date
        viewModel.getTodaySymptoms().observe(requireActivity(), symptomList -> {
            date[0] = Calendar.getInstance().getTime();
            calendarView.setDate(DateConverter.fromDate(date[0]));
            setDates(view);
            setSelectedDay(view, viewModel, symptomList);
        });
    }

    private void setSelectedDay(View view, ItemViewModel viewModel, List<Symptom> symptomList){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        List<Symptom> cacheSymptomList = new ArrayList<>();
        if (!symptomList.isEmpty()) {
            cacheSymptomList.addAll(symptomList);
        }

        SymptomListAdapter adapter = new SymptomListAdapter(cacheSymptomList, viewModel, view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setDates(View view){
        before[0].setDate(date[0].getDate());before[0].setHours(00); before[0].setMinutes(00); before[0].setSeconds(00);
        after[0].setDate(date[0].getDate());after[0].setHours(23); after[0].setMinutes(59); after[0].setSeconds(59);

        TextView dateTextView = view.findViewById(R.id.dateTextView);
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("es", "ES"));
        dateTextView.setText(simpleDateFormat.format(before[0]));
    }
}