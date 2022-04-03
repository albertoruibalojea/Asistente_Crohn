package com.aro.asistente_crohn.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.aro.asistente_crohn.R;

import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{

    private List<Symptom> listdata;

    // RecyclerView recyclerView;
    public MyListAdapter(List<Symptom> listdata) {
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(listdata.size() == 0){
            holder.symptoms.setText("No hay síntomas registrados");
        } else {
            holder.symptoms.setText(listdata.get(position).getName());
            holder.date.setText(listdata.get(position).getRegisteredDate().toString());
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
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.symptoms = (TextView) itemView.findViewById(R.id.symptoms);
            relativeLayout = (ConstraintLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}
