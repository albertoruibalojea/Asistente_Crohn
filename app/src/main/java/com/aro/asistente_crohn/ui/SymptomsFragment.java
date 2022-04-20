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
    final Date[] before = date;
    final Date[] after = date;

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

        //Set default date
        this.setDates(view);

        //Listener when the user sets a date in Calendar
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                date[0] = Calendar.getInstance().getTime();
                //Set date
                setDates(view);

                //Observer from Repository to lookup the LiveData
                viewModel.getSelectedDaySymptoms(before[0], after[0]).observe(requireActivity(), symptomList -> {
                    RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
                    List<Symptom> cacheSymptomList = new ArrayList<>();
                    if (!symptomList.isEmpty()) {
                        cacheSymptomList.addAll(symptomList);
                    } else {
                        sendAlert(view, "¡Qué gustazo!", "No hay síntomas registrados para esta fecha");
                    }
                    SymptomListAdapter adapter = new SymptomListAdapter(cacheSymptomList, viewModel, view);
                    recyclerView.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);
                });
            }
        });

        calendarView.setDate(DateConverter.fromDate(date[0]));
    }

    public void sendAlert(View view, String title, String description){
        //Success alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.notification_dialog, viewGroup, false);

        TextView t = dialogView.findViewById(R.id.title);
        t.setText(title);
        t = dialogView.findViewById(R.id.description);
        t.setText(description);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    public void setDates(View view){
        before[0] = date[0];
        before[0].setHours(00); before[0].setMinutes(00); before[0].setSeconds(00);
        after[0] = date[0];
        after[0].setHours(23); after[0].setMinutes(59); after[0].setSeconds(59);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("es", "ES"));
        dateTextView.setText(simpleDateFormat.format(before[0]));
    }
}