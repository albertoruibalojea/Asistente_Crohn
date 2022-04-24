package com.aro.asistente_crohn.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aro.asistente_crohn.R;
import com.aro.asistente_crohn.model.Food;
import com.aro.asistente_crohn.model.ItemViewModel;

public class AlertDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_alert);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Integer foodId = extras.getInt("FOOD_ID");
            String description = extras.getString("DESCRIPTION");

            TextView textDescription = findViewById(R.id.description);
            textDescription.setText(description);

            ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);

            Button btn1= (Button) findViewById(R.id.buttonCancel);
            btn1.setOnClickListener(v -> finish());

            Button btn2= (Button) findViewById(R.id.buttonOk);
            btn2.setOnClickListener(v -> {
                viewModel.getFoodById(foodId).observe(AlertDetails.this, food -> {
                    if(food != null){
                        food.setForbidden(true);
                        viewModel.updateFood(food);
                    }
                });

                finish();
            });



        }
    }
}