package com.aro.asistente_crohn.view.viewmodel;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.Poop;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PoopListAdapter extends RecyclerView.Adapter<PoopListAdapter.ViewHolder>{

    private List<Poop> listdata;
    private ItemViewModel viewModel;
    private View view;

    public PoopListAdapter(List<Poop> listdata, ItemViewModel viewModel, View view) {
        this.listdata = listdata;
        this.viewModel = viewModel;
        this.view = view;
    }

    @Override
    public PoopListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.poop_item, parent, false);
        return new PoopListAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(PoopListAdapter.ViewHolder holder, int position) {
        if(listdata.isEmpty()){
            holder.poops.setText("No hay deposiciones registradas");
        } else {
            Poop poop = listdata.get(position);

            Integer type = poop.getType();
            if (type == 1) {
                holder.poops.setText("Muy estreñido");
            } else if (type == 2) {
                holder.poops.setText("Estreñido");
            } else if (type == 3) {
                holder.poops.setText("Normal");
            } else if (type == 4) {
                holder.poops.setText("Perfecta");
            } else if (type == 5) {
                holder.poops.setText("Falta de fibra");
            } else if (type == 6) {
                holder.poops.setText("Posible diarrea");
            } else if (type == 7) {
                holder.poops.setText("Diarrea");
            }

            String color = poop.getColor();
            if (color.equalsIgnoreCase("brown")) {
                holder.color.setText("Marrón");
            } else if (color.equalsIgnoreCase("white")) {
                holder.color.setText("Blanca");
            } else if (color.equalsIgnoreCase("black")) {
                holder.color.setText("Negra");
            } else if (color.equalsIgnoreCase("yellow")) {
                holder.color.setText("Amarilla");
            } else if (color.equalsIgnoreCase("green")) {
                holder.color.setText("Verde");
            } else if (color.equalsIgnoreCase("red")) {
                holder.color.setText("Roja");
            }

            Integer weight = poop.getWeight();
            if (weight == 1) {
                holder.weight.setText("Pequeña");
            } else if (weight == 2) {
                holder.weight.setText("Mediana");
            } else if (weight == 3) {
                holder.weight.setText("Grande");
            }

            holder.urgency.setVisibility(poop.isUrgency() ? View.VISIBLE : View.GONE);
            holder.painful.setVisibility(poop.isPainful() ? View.VISIBLE : View.GONE);
            holder.blood.setVisibility(poop.isBlood() ? View.VISIBLE : View.GONE);

            holder.deleteImg.setOnClickListener(view -> {
                viewModel.deletePoop(poop);
                this.sendAlert("Eliminado", "Registro eliminado correctamente");
                notifyDataSetChanged();
            });
        }
    }

    public void sendAlert(String title, String description){
        //Success alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_notification, viewGroup, false);

        TextView t = dialogView.findViewById(R.id.title);
        t.setText(title);
        t = dialogView.findViewById(R.id.description);
        t.setText(description);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.findViewById(R.id.buttonOk).setOnClickListener(alert -> alertDialog.dismiss());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView poops;
        private TextView color;
        private TextView weight;
        private ImageView deleteImg;
        private ImageView urgency;
        private ImageView painful;
        private ImageView blood;
        private ConstraintLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            poops =  itemView.findViewById(R.id.poops);
            color =  itemView.findViewById(R.id.color);
            weight =  itemView.findViewById(R.id.weight);
            deleteImg = itemView.findViewById(R.id.deleteImg);
            urgency = itemView.findViewById(R.id.urgency);
            painful = itemView.findViewById(R.id.painful);
            blood = itemView.findViewById(R.id.blood);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
