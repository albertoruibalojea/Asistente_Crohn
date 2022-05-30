package com.aro.asistente_crohn.view.viewmodel;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecommendationsListAdapter extends RecyclerView.Adapter<RecommendationsListAdapter.ViewHolder> implements Filterable {

    private List<Recommendation> listdata;
    private List<Recommendation> listFilter;
    private View view;

    public RecommendationsListAdapter(List<Recommendation> listdata, View view) {
        this.listdata = listdata;
        this.view = view;
        this.listFilter = listdata;
    }

    @Override
    public RecommendationsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.recommendation_item, parent, false);
        return new RecommendationsListAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(RecommendationsListAdapter.ViewHolder holder, int position) {
        if(listFilter.isEmpty()){
            holder.title.setText("No hay recomendaciones disponibles");
        } else {
            holder.title.setText(listFilter.get(position).getTitle().substring(4));

            holder.relativeLayout.setOnClickListener(view -> {
                String title = listFilter.get(position).getTitle();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                if(title.startsWith("GRA/") || title.startsWith("NUT/")){
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(listFilter.get(position).getDescription())));
                } else {
                    Fragment myFragment = new WebFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", listFilter.get(position).getDescription());
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView, myFragment).addToBackStack(null).commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = listdata.size();
                    filterResults.values = listdata;

                }else{
                    List<Recommendation> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(Recommendation itemsModel:listdata){
                        if(itemsModel.getTitle().toLowerCase(Locale.ROOT).contains(searchStr) || itemsModel.getTitle().substring(4).toLowerCase(Locale.ROOT).startsWith(searchStr)){
                            resultsModel.add(itemsModel);

                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }


                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                listFilter = (List<Recommendation>) results.values;
                notifyDataSetChanged();

            }
        };
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
