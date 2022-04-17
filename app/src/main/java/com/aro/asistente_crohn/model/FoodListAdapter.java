package com.aro.asistente_crohn.model;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.aro.asistente_crohn.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder>{

    private List<Food> listdata;
    private ItemViewModel viewModel;
    private View view;

    public FoodListAdapter(List<Food> listdata, ItemViewModel viewModel, View view) {
        this.listdata = listdata;
        this.viewModel = viewModel;
        this.view = view;
    }

    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.food_item, parent, false);
        return new FoodListAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(FoodListAdapter.ViewHolder holder, int position) {
        if(listdata.isEmpty()){
            holder.foods.setText("No hay alimentos registrados");
        } else {
            holder.foods.setText(listdata.get(position).getName());

            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("es", "ES"));
            holder.date.setText(simpleDateFormat.format(listdata.get(position).getEatenDate()));

            Food food = listdata.get(position);

            holder.deleteImg.setOnClickListener(view -> {
                viewModel.deleteFood(food);
                this.sendAlert("Eliminado", "Registro eliminado correctamente");
                notifyDataSetChanged();
            });

            holder.forbiddenImg.setOnClickListener(view -> {
                if(!food.getForbidden()){
                    food.setForbidden(true);
                    viewModel.updateFood(food);
                    this.sendAlert("Añadido", "Nuevo alimento prohibido");
                    notifyDataSetChanged();
                } else {
                    food.setForbidden(false);
                    viewModel.updateFood(food);
                    this.sendAlert("Eliminado", "Este alimento ya no está prohibido");
                    notifyDataSetChanged();
                }
            });
        }
    }

    public void sendAlert(String title, String description){
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

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView foods;
        public ImageView deleteImg;
        public ImageView forbiddenImg;
        public ConstraintLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            foods =  itemView.findViewById(R.id.foods);
            deleteImg = itemView.findViewById(R.id.deleteImg);
            forbiddenImg = itemView.findViewById(R.id.forbiddenImg);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
