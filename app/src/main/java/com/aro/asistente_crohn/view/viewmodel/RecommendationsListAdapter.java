package com.aro.asistente_crohn.view.viewmodel;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.Food;
import com.aro.asistente_crohn.model.Recommendation;
import com.aro.asistente_crohn.view.ui.WebFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecommendationsListAdapter extends RecyclerView.Adapter<RecommendationsListAdapter.ViewHolder>{

    private List<Recommendation> listdata;
    private ItemViewModel viewModel;
    private View view;

    public RecommendationsListAdapter(List<Recommendation> listdata, ItemViewModel viewModel, View view) {
        this.listdata = listdata;
        this.viewModel = viewModel;
        this.view = view;
    }

    @Override
    public RecommendationsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.recommendation_item, parent, false);
        return new RecommendationsListAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(RecommendationsListAdapter.ViewHolder holder, int position) {
        if(listdata.isEmpty()){
            holder.title.setText("No hay recomendaciones disponibles");
        } else {
            holder.title.setText(listdata.get(position).getTitle().substring(4));
            
            holder.relativeLayout.setOnClickListener(view -> {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new WebFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", listdata.get(position).getDescription());
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView, myFragment).addToBackStack(null).commit();
            });
        }
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ConstraintLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.title);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
