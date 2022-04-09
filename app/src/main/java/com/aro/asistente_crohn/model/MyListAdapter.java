package com.aro.asistente_crohn.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.aro.asistente_crohn.R;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{

    private List<Symptom> listdata;

    public MyListAdapter(List<Symptom> listdata) {
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.symptom_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(listdata.isEmpty()){
            holder.symptoms.setText("No hay síntomas registrados");
        } else {
            holder.symptoms.setText(listdata.get(position).getName());

            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("es", "ES"));
            holder.date.setText(simpleDateFormat.format(new Date()));
        }
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView symptoms;
        public ConstraintLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            symptoms =  itemView.findViewById(R.id.symptoms);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
