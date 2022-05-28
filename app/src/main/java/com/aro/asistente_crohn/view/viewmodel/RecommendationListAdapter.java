package com.aro.asistente_crohn.view.viewmodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.Recommendation;
import com.aro.asistente_crohn.view.ui.WebFragment;

import java.util.List;

public class RecommendationListAdapter extends RecyclerView.Adapter<RecommendationListAdapter.ViewHolder>{

    private List<Recommendation> listdata;
    private ItemViewModel viewModel;
    private View view;

    public RecommendationListAdapter(List<Recommendation> listdata, ItemViewModel viewModel, View view) {
        this.listdata = listdata;
        this.viewModel = viewModel;
        this.view = view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.recommendation_item, parent, false);
        return new RecommendationListAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(listdata.isEmpty()){
            holder.title.setText("No hay recomendaciones disponibles");
        } else {
            Recommendation recommendation = listdata.get(position);
            if(recommendation.getTitle().startsWith("INF/") || recommendation.getTitle().startsWith("NUT/")
                    || recommendation.getTitle().startsWith("GRA/") || recommendation.getTitle().startsWith("BOT/")){
                recommendation.setTitle(recommendation.getTitle().substring(4));
            }

            holder.card.setOnClickListener(view -> {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new WebFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", recommendation.getDescription());
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView, myFragment).addToBackStack(null).commit();
            });
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private CardView card;
        private ConstraintLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.title);
            card = itemView.findViewById(R.id.card_recommendation_item);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
