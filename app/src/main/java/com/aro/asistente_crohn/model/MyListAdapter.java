package com.aro.asistente_crohn.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
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
    private ItemViewModel viewModel;

    public MyListAdapter(List<Symptom> listdata, ItemViewModel viewModel) {
        this.listdata = listdata;
        this.viewModel = viewModel;
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
            holder.date.setText(simpleDateFormat.format(listdata.get(position).getRegisteredDate()));

            Symptom symptom = listdata.get(position);

            holder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.deleteSymptom(symptom);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView symptoms;
        public ImageView deleteImg;
        public ConstraintLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            symptoms =  itemView.findViewById(R.id.symptoms);
            deleteImg = itemView.findViewById(R.id.deleteImg);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
