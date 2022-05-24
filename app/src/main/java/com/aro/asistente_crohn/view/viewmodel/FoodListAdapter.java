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
import com.aro.asistente_crohn.model.Food;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

            Food food = listdata.get(position);

            holder.deleteImg.setOnClickListener(view -> {
                viewModel.deleteFood(food);
                this.sendAlert("Eliminado", "Registro eliminado correctamente");
                notifyDataSetChanged();
            });

            if(Boolean.TRUE.equals(food.getForbidden())){
                //we update the icon
                holder.forbiddenImg.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
            }

            holder.forbiddenImg.setOnClickListener(view -> {
                if(Boolean.FALSE.equals(food.getForbidden())){
                    food.setForbidden(true);
                    Date date = (Date) food.getEatenDate().clone();
                    date.setYear(2200); date.setMonth(1); date.setDate(1);
                    food.setLimitDate(date);
                    viewModel.updateFood(food);
                    this.sendAlert("Añadido", "Nuevo alimento desaconsejado");
                    notifyDataSetChanged();
                } else {
                    food.setForbidden(false);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(food.getEatenDate());
                    calendar.add(Calendar.DAY_OF_YEAR, 90);
                    viewModel.updateFood(food);
                    this.sendAlert("Eliminado", "Este alimento ya no está desaconsejado");
                    notifyDataSetChanged();
                }
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
        private TextView foods;
        private ImageView deleteImg;
        private ImageView forbiddenImg;
        private ConstraintLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            foods =  itemView.findViewById(R.id.foods);
            deleteImg = itemView.findViewById(R.id.deleteImg);
            forbiddenImg = itemView.findViewById(R.id.forbiddenImg);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
