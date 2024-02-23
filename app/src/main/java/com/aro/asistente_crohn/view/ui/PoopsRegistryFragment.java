package com.aro.asistente_crohn.view.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.Poop;
import com.aro.asistente_crohn.model.Symptom;
import com.aro.asistente_crohn.view.viewmodel.ItemViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PoopsRegistryFragment extends Fragment {

    String paramDate;

    Poop poop = new Poop();

    public PoopsRegistryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paramDate = getArguments().getString("date");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_poop_registry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss zzz", new Locale("es", "ES"));
        Date date = Calendar.getInstance().getTime();

        if(!paramDate.isEmpty()){
            try {
                date = simpleDateFormat.parse(paramDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //we need the actual registry date
        Date finalDate = date;
        Date before = Calendar.getInstance().getTime();
        Date after = Calendar.getInstance().getTime();
        before.setDate(date.getDate());before.setHours(00); before.setMinutes(00); before.setSeconds(00);
        after.setDate(date.getDate());after.setHours(23); after.setMinutes(59); after.setSeconds(59);

        ImageView infoBtn = view.findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(this::openInfo);

        simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("es", "ES"));
        TextView textDate = view.findViewById(R.id.textDate);
        textDate.setText(simpleDateFormat.format(date));

        Button btn = view.findViewById(R.id.btn);
        btn.setEnabled(true);

        GridLayout poopTypeGrid = view.findViewById(R.id.poopTypeGrid);
        for (int i = 0; i < poopTypeGrid.getChildCount(); i++) {
            CardView card = (CardView) poopTypeGrid.getChildAt(i);
            card.setOnClickListener(view12 -> {
                //Put the actual type and delete the previous one
                for (int j = 0; j < poopTypeGrid.getChildCount(); j++) {
                    CardView card1 = (CardView) poopTypeGrid.getChildAt(j);
                    if (card1.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.negroGris)) {
                        card1.setCardBackgroundColor(getResources().getColor(R.color.marron));


                        //add info to Poop

                        poop.setRegisteredDate(finalDate);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(finalDate);
                        calendar.add(Calendar.DAY_OF_YEAR, 90);
                        poop.setLimitDate(calendar.getTime());

                        String type = card.getChildAt(0).getContentDescription().toString();
                        if(type.equalsIgnoreCase("Muy estreñido")) {
                            poop.setType(1);
                        } else if(type.equalsIgnoreCase("Estreñido")) {
                            poop.setType(2);
                        } else if(type.equalsIgnoreCase("Normal")) {
                            poop.setType(3);
                        } else if(type.equalsIgnoreCase("Perfecta")) {
                            poop.setType(4);
                        } else if(type.equalsIgnoreCase("Falta de fibra")) {
                            poop.setType(5);
                        } else if(type.equalsIgnoreCase("Posible diarrea")) {
                            poop.setType(6);
                        } else if(type.equalsIgnoreCase("Diarrea")) {
                            poop.setType(7);
                        }

                        //unselect previous type
                        for (int k = 0; j < poopTypeGrid.getChildCount(); k++) {
                            CardView cardn = (CardView) poopTypeGrid.getChildAt(k);
                            if (cardn.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.marron)) {
                                cardn.setCardBackgroundColor(getResources().getColor(R.color.negroGris));
                            }
                        }
                    }
                }
            });
        }

        //same thing but for poopColorGrid
        GridLayout poopColorGrid = view.findViewById(R.id.poopColorGrid);
        for (int i = 0; i < poopColorGrid.getChildCount(); i++) {
            CardView card = (CardView) poopColorGrid.getChildAt(i);
            card.setOnClickListener(view12 -> {
                //Put the actual color and delete the previous one
                for (int j = 0; j < poopColorGrid.getChildCount(); j++) {
                    CardView card1 = (CardView) poopColorGrid.getChildAt(j);
                    if (card1.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.negroGris)) {
                        card1.setCardBackgroundColor(getResources().getColor(R.color.marron));

                        //add info to Poop
                        String color = card.getChildAt(0).getContentDescription().toString();
                        if(color.equalsIgnoreCase("Marrón")) {
                            poop.setColor("brown");
                        } else if(color.equalsIgnoreCase("Blanca")) {
                            poop.setColor("white");
                        } else if(color.equalsIgnoreCase("Negra")) {
                            poop.setColor("black");
                        } else if(color.equalsIgnoreCase("Amarilla")) {
                            poop.setColor("yellow");
                        } else if(color.equalsIgnoreCase("Verde")) {
                            poop.setColor("green");
                        } else if(color.equalsIgnoreCase("Roja")) {
                            poop.setColor("red");
                        }


                        //unselect previous color
                        for (int k = 0; j < poopColorGrid.getChildCount(); k++) {
                            CardView cardn = (CardView) poopColorGrid.getChildAt(k);
                            if (cardn.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.marron)) {
                                cardn.setCardBackgroundColor(getResources().getColor(R.color.negroGris));
                            }
                        }
                    }
                }
            });
        }

        //Weight checkboxes
        CheckBox checkBox1 = view.findViewById(R.id.checkbox1);
        CheckBox checkBox2 = view.findViewById(R.id.checkbox2);
        CheckBox checkBox3 = view.findViewById(R.id.checkbox3);

        checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                // add info to Poop
                poop.setWeight(1);
            }
        });

        checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBox1.setChecked(false);
                checkBox3.setChecked(false);
                // add info to Poop
                poop.setWeight(2);
            }
        });

        checkBox3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                // add info to Poop
                poop.setWeight(3);
            }
        });


        //urgency, painful and blood checkboxes can be selected multiple
        CheckBox urgency = view.findViewById(R.id.checkbox4);
        CheckBox painful = view.findViewById(R.id.checkbox5);
        CheckBox blood = view.findViewById(R.id.checkbox6);

        urgency.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // add info to Poop
                poop.setUrgency(true);
            } else {
                // add info to Poop
                poop.setUrgency(false);
            }
        });

        painful.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // add info to Poop
                poop.setPainful(true);
            } else {
                // add info to Poop
                poop.setPainful(false);
            }
        });

        blood.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // add info to Poop
                poop.setBlood(true);
            } else {
                // add info to Poop
                poop.setBlood(false);
            }
        });


        btn.setOnClickListener(view14 -> {
            //on this if, check if type, color and weight were selected by the user
            if (poop.getType() != null && poop.getWeight() != null) {

                //we save the data into the Room Persistence Database
                viewModel.insertPoop(poop);

                //reset ui
                GridLayout typeGrid1 = requireView().findViewById(R.id.poopTypeGrid);
                for (int i = 0; i < typeGrid1.getChildCount(); i++) {
                    CardView card = (CardView) typeGrid1.getChildAt(i);
                    if (card.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.marron)) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.negroGris));
                    }
                }

                GridLayout colorGrid1 = requireView().findViewById(R.id.poopColorGrid);
                for (int i = 0; i < colorGrid1.getChildCount(); i++) {
                    CardView card = (CardView) colorGrid1.getChildAt(i);
                    if (card.getCardBackgroundColor().getDefaultColor() == getResources().getColor(R.color.marron)) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.negroGris));
                    }
                }

                //Success alert dialog
                this.sendAlert(view);
            }
        });
    }

    public void sendAlert(View view){
        //Success alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_notification, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.findViewById(R.id.buttonOk).setOnClickListener(view1 -> alertDialog.dismiss());
    }

    public void openInfo(View view){
        //Success alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.fragment_symptoms_registry_meaning, viewGroup, false);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.findViewById(R.id.dismiss).setOnClickListener(view1 -> alertDialog.dismiss());
    }
}
