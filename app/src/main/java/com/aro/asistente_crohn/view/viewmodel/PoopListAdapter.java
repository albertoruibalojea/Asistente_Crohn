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
            holder.poops.setText(listdata.get(position).getType());

            Poop poop = listdata.get(position);

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
        private ImageView deleteImg;
        private ConstraintLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            poops =  itemView.findViewById(R.id.poops);
            deleteImg = itemView.findViewById(R.id.deleteImg);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
