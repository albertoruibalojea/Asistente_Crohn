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
import com.aro.asistente_crohn.model.Symptom;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SymptomListAdapter extends RecyclerView.Adapter<SymptomListAdapter.ViewHolder> {

    private List<Symptom> listdata;
    private ItemViewModel viewModel;
    private View view;

    public SymptomListAdapter(List<Symptom> listdata, ItemViewModel viewModel, View view) {
        this.listdata = listdata;
        this.viewModel = viewModel;
        this.view = view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.symptom_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (listdata.isEmpty()) {
            holder.symptoms.setText("No hay síntomas registrados");
        } else {
            Symptom symptom = listdata.get(position);

            holder.symptoms.setText(symptom.getName());

            // Get the current date and time
            Date currentDate = symptom.getRegisteredDate();

            // Format the date and time
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String formattedDateTime = formatter.format(currentDate);

            // Set the formatted date and time to the TextView
            holder.symptomDateTime.setText(formattedDateTime);

            holder.deleteImg.setOnClickListener(view -> {
                viewModel.deleteSymptom(symptom);
                this.sendAlert("Eliminado", "Registro eliminado correctamente");
            });
        }
    }

    public void sendAlert(String title, String description) {
        // Success alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_notification, viewGroup,
                false);

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
        public TextView symptomDateTime;
        private TextView symptoms;
        private ImageView deleteImg;
        private ConstraintLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            symptoms = itemView.findViewById(R.id.symptoms);
            symptomDateTime = itemView.findViewById(R.id.symptomDateTime);
            deleteImg = itemView.findViewById(R.id.deleteImg);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
